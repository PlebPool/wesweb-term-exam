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
    @Query("UPDATE post SET likes = likes + 1 WHERE id = :id; SELECT * FROM post WHERE id = :id")
    fun like(@Param("id") id: ID): Mono<Post>

    @Query("UPDATE post SET likes = likes - 1 WHERE id = :id; SELECT * FROM post WHERE id = :id")
    fun unlike(@Param("id") id: ID): Mono<Post>
}
