package com.utopia.backend.posts.model

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface PathToPostRepository<ID>: ReactiveCrudRepository<PathToPost, ID> {

    @Query("SELECT * FROM path_to_post ORDER BY post_id DESC LIMIT :amount")
    fun getAllWithLimit(@Param("amount") amount: Long): Flux<PathToPost>
}