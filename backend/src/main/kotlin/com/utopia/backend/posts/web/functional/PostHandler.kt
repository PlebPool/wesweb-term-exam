package com.utopia.backend.posts.web.functional

import com.utopia.backend.generics.beans.SpringManaged
import com.utopia.backend.generics.errors.BadRequestException
import com.utopia.backend.generics.errors.NotFoundException
import com.utopia.backend.posts.model.Liked
import com.utopia.backend.posts.model.Post
import com.utopia.backend.posts.model.PostRepository
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import reactor.core.publisher.Mono
import java.lang.NumberFormatException
import java.net.URI

@Component
class PostHandler(
        val postRepository: PostRepository<Long>,
) : SpringManaged() {

    suspend fun getAll(request: ServerRequest): ServerResponse {
        return ServerResponse.ok().bodyAndAwait(postRepository.findAll().asFlow())
    }

    suspend fun getOne(request: ServerRequest): ServerResponse {
        try {
            return ServerResponse
                    .ok().bodyAndAwait(postRepository.findById(request.pathVariable("id").toLong()).asFlow())
        } catch (e: NumberFormatException) {
            throw BadRequestException("id path variable needs to be a number.")
        }
    }

    suspend fun createOne(request: ServerRequest): ServerResponse {
        val postedPost = request.bodyToMono(Post::class.java).awaitFirst()
        val post: Post = postRepository.save(postedPost).awaitFirst() // We need to wait for the save to finish.
        // Before we can put the post into the response. Or we will save it twice. IDK why.
        return ServerResponse.created(URI("/posts/${post.id}")).body(BodyInserters.fromValue(post)).awaitFirst()
    }

    suspend fun like(request: ServerRequest): ServerResponse {
        val id: Long = request.pathVariable("id").toLong()
        val liked: Liked = request.bodyToMono(Liked::class.java).awaitFirst()
        val post: Mono<Post> = postRepository.findById(id).map {
            if(liked.isLike) {
                return@map postRepository.like(it.id, "127.0.0.1", true)
            } else {
                return@map postRepository.unlike(it.id, "127.0.0.1", false)
            }
        }.awaitFirst()
        return ServerResponse.ok().bodyAndAwait(post.asFlow())
    }
}