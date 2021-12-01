package com.utopia.backend.posts.model.pathtopost

import com.utopia.backend.posts.model.post.Post
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface PathToPostRepository: ReactiveCrudRepository<PathToPost, Long> {

    @Query("SELECT * FROM path_to_post ORDER BY post_id DESC LIMIT :amount")
    fun getAllWithLimit(@Param("amount") amount: Long): Flux<PathToPost>

    @Query("INSERT INTO path_to_post VALUES(:post_id, CONTCAT('/posts/', :post_id));")
    fun createPostPath(@Param("post_id") post_id: Long): Mono<PathToPost>
}