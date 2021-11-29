package com.utopia.backend.posts.web.functional

import com.utopia.backend.generics.beans.SpringManaged
import com.utopia.backend.generics.errors.BadRequestException
import com.utopia.backend.posts.model.PathToPost
import com.utopia.backend.posts.model.PathToPostRepository
import com.utopia.backend.posts.model.Post
import com.utopia.backend.posts.model.PostRepository
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import org.apache.catalina.Server
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.lang.NumberFormatException
import java.net.URI
import kotlin.io.path.Path

@Component
class PostHandler(
        val postRepository: PostRepository<Long>,
        val pathToPostRepository: PathToPostRepository<Long>,
) : SpringManaged() {

    suspend fun getAll(request: ServerRequest): ServerResponse {
        return ServerResponse.ok().bodyAndAwait(pathToPostRepository.getAllWithLimit(10L).asFlow())
    }

    suspend fun getOne(request: ServerRequest): ServerResponse {
        var ip: String = request.remoteAddress().orElseThrow().address.toString().replace("/", "")
        if(ip == "0:0:0:0:0:0:0:1") ip = "127.0.0.1" // "0:0:0:0:0:0:0:1" does not work with inet_aton() in mysql
        try {
            return ServerResponse
                    .ok()
                    .bodyAndAwait(postRepository.findById(request.pathVariable("id").toLong()).asFlow())
        } catch (e: NumberFormatException) {
            throw BadRequestException("id path variable needs to be a number.")
        }
    }

    suspend fun createOne(request: ServerRequest): ServerResponse {
        val postedPost = request.bodyToMono(Post::class.java).awaitFirst()
        if(validatePost(postedPost)) {
            val post: Post = postRepository.save(postedPost).awaitFirst() // We need to wait for the save to finish.
            postRepository.createPostPath(post.postId)
            // Before we can put the post into the response. Or we will save it twice. IDK why.
            return ServerResponse.created(URI("/posts/${post.postId}")).body(BodyInserters.fromValue(post)).awaitFirst()
        }
        throw BadRequestException("Title or content cannot be empty")
    }

    private fun validatePost(post: Post): Boolean {
        return (post.title != "" && post.content != "")
    }

    suspend fun like(request: ServerRequest): ServerResponse {
        val id: Long = request.pathVariable("id").toLong()
        var ip: String = request.remoteAddress().orElseThrow().address.toString().replace("/", "")
        if(ip == "0:0:0:0:0:0:0:1") ip = "127.0.0.1" // "0:0:0:0:0:0:0:1" does not work with inet_aton() in mysql
        val post: Mono<Post> = postRepository.existsById(id).flatMap { // Checking if post exists.
            if(it) {
                return@flatMap postRepository.toggleLike(id, ip)
                // If it exists toggleLike (new ip log will be created if one does not exist)
            } else {
                throw BadRequestException("Post does not exist $id")
                // Throw a BadRequest exception if it does not exist. (Triggers a Http 400 response)
            }
        }
        return ServerResponse.ok().bodyAndAwait(post.asFlow())
    }
}