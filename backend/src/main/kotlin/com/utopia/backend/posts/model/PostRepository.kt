package com.utopia.backend.posts.model

import com.utopia.backend.generics.beans.SpringManaged
import org.reactivestreams.Publisher
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
/*
    This interface is initialized by the Spring Framework.
*/
/*
    This establishes a connection to the mysql server with info from backend/src/main/resources/application.properties
*/
@Repository
interface PostRepository<ID> : ReactiveCrudRepository<Post, ID> {
    /*
        Spring creates the methods, all we have to do is declare the query and the method name/parameters/return type.
    */
    // Trying to create Like-ip log. If we get a key duplicate (composite keys on ip, and post_id) then we
    // Simply update(toggle) the existing one.

    // Then we
    @Query("INSERT INTO who_liked_what VALUES(inet_aton(:ip), :post_id, true) ON DUPLICATE KEY UPDATE liked = NOT liked;" +
            "UPDATE post SET likes = if((post_id, (SELECT liked FROM who_liked_what WHERE (ip, post_id) = (inet_aton(:ip), :post_id))) = (:post_id, true), likes+1, likes-1) WHERE post_id = :post_id; " +
            "SELECT * FROM post WHERE post_id = :post_id;")
    fun toggleLike(
        @Param("post_id") post_id: ID,
        @Param("ip") ip: String): Mono<Post>

    @Query("SELECT liked FROM who_liked_what WHERE ip = inet_aton(:ip) AND post_id = :post_id;")
    fun checkIfLiked(@Param("ip") ip: String, @Param("post_id") post_id: Long): Mono<Boolean>

    @Query("SELECT EXISTS(SELECT * FROM who_liked_what WHERE (ip, post_id) = (inet_aton(:ip), :post_id));")
    fun checkIfLogged(@Param("ip") ip: String, @Param("post_id") post_id: ID): Mono<Boolean>

    @Query("INSERT INTO path_to_post VALUES(:post_id, CONCAT('/posts/', :post_id));")
    fun createPostPath(@Param("post_id") post_id: ID): Mono<Post>

    @Query("SELECT relative_path FROM path_to_post ORDER BY post_id DESC LIMIT :amount;")
    fun getAmountOfPostPaths(@Param("amount") amount: Long): Flux<PathToPost>
}
