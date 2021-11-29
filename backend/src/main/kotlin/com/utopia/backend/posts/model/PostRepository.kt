package com.utopia.backend.posts.model

import com.utopia.backend.generics.beans.SpringManaged
import org.reactivestreams.Publisher
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
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
    @Query("UPDATE post SET likes = likes + 1 WHERE id = :post_id;" +
            "INSERT INTO who_liked_what VALUES(inet_aton(:ip), :post_id, :liked)" +
            "ON DUPLICATE KEY UPDATE liked=:liked;" +
            "SELECT * FROM post WHERE id = :post_id;")
    fun like(
        @Param("post_id") post_id: ID,
        @Param("ip") ip: String,
        @Param("liked") liked: Boolean): Mono<Post>

    @Query("INSERT INTO who_liked_what VALUES(inet_aton(:ip), :post_id, :liked)" +
            "ON DUPLICATE KEY UPDATE liked=:liked;" +
            "UPDATE post SET likes = likes - 1 WHERE id = :post_id;" +
            "SELECT * FROM post WHERE id = :post_id;")
    fun unlike(@Param("post_id") post_id: ID,
               @Param("ip") ip: String,
               @Param("liked") liked: Boolean): Mono<Post>

    @Query("SELECT liked FROM who_liked_what WHERE ip = inet.aton(:ip) AND post_id = :post_id")
    fun checkIfLiked(ip: String, post_id: Long): Mono<Boolean>
}
