package com.utopia.backend.posts.web.functional

import com.utopia.backend.generics.beans.SpringManaged
import com.utopia.backend.generics.errors.BadRequestException
import com.utopia.backend.posts.model.pathtopost.PathToPost
import com.utopia.backend.posts.model.pathtopost.PathToPostRepository
import com.utopia.backend.posts.model.post.Post
import com.utopia.backend.posts.model.post.PostRepository
import com.utopia.backend.posts.model.wholikedwhat.WhoLikedWhatRepository
import kotlinx.coroutines.reactive.*
import net.minidev.json.JSONObject
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import reactor.core.publisher.Mono
import java.lang.NumberFormatException
import java.net.URI

@Component
class PostHandler(
    val postRepository: PostRepository,
    val pathToPostRepository: PathToPostRepository,
    val whoLikedWhatRepository: WhoLikedWhatRepository
) : SpringManaged() {
    // Returns all the paths for a set amount of posts, in DESC order.
    suspend fun getAll(request: ServerRequest): ServerResponse {
        return ServerResponse.ok().bodyAndAwait(pathToPostRepository.getAllWithLimit(10L).asFlow())
    }
    suspend fun getOne(request: ServerRequest): ServerResponse {
        val postId = request.pathVariable("post_id").toLong() // Gets ID that is requested.
        val ip = getIpFromRequest(request) // Gets the IP of the client.
        try { // Do this with getAll map them sons of bitches.
            val post: Post = postRepository.findById(postId).awaitFirst() // retrieves the post
            val json = parsePostIntoJsonObject(post, ip)
            // Parses the Post object into a JSONObject. With "hasLiked" boolean
            log.info("IP: $ip ID: $postId") // Logs
            return ServerResponse // Returns the response with the JSON.
                    .ok().bodyValueAndAwait(json)
        } catch (e: NumberFormatException) {
            throw BadRequestException("id path variable needs to be a number.")
        }
    }
    /*
        Gets the IP from a ServerRequest.
        It accounts for localhost (previously 127.0.0.1) being 0:0:0:0:0:0:0:1.
        We need to switch it back to avoid an error with inet_aton(ip) in MYSQL.
    */
    private fun getIpFromRequest(request: ServerRequest): String {
        var ip: String = request
                .remoteAddress().orElseThrow().address.toString().replace("/", "")
        if(ip == "0:0:0:0:0:0:0:1") ip = "127.0.0.1" // "0:0:0:0:0:0:0:1" does not work with inet_aton() in mysql
        return ip
    }
    /*
        Parses a Post object into JSONObject, and adds the "hasLiked" property.
        "hasLiked" is true, if request IP has liked the comment being retrieved.
    */
    private suspend fun parsePostIntoJsonObject(post: Post, ip: String): JSONObject {
        val json = JSONObject()
        json["alias"] = post.alias
        json["title"] = post.title
        json["content"] = post.content
        json["likes"] = post.likes
        json["dateOfPost"] = post.dateOfPost
        json["timeOfPost"] = post.timeOfPost
        json["post_id"] = post.post_id
        // Checks if the client IP has liked the post, if no record is found, then we set "hasLiked" to false.
        // if a record is found, then we set "hasLiked" to whatever boolean the record contained.
        json["hasLiked"] = whoLikedWhatRepository.getByIpAndId(ip, post.post_id).awaitFirstOrDefault(false)
        return json
    }
    suspend fun createOne(request: ServerRequest): ServerResponse {
        val postedPost = request.bodyToMono(Post::class.java).awaitFirst()
        if(validatePost(postedPost)) {
            val post: Post = postRepository.save(postedPost).awaitFirst() // We need to wait for the save to finish.
            log.info("Added..." + pathToPostRepository
                    .save(PathToPost(0, "/posts/${post.post_id}")).awaitFirst())
            // Before we can put the post into the response. Or we will save it twice. IDK why.
            return ServerResponse
                .created(URI("/posts/${post.post_id}")).body(BodyInserters.fromValue(post)).awaitFirst()
        }
        throw BadRequestException("Title or content cannot be empty")
    }
    // Checks so that the Post that is about to be saved, is valid.
    private fun validatePost(post: Post): Boolean {
        return (post.title != "" && post.content != "")
    }
    suspend fun like(request: ServerRequest): ServerResponse {
        val id: Long = request.pathVariable("post_id").toLong()
        val ip: String = getIpFromRequest(request)
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