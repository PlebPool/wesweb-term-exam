package com.utopia.backend.posts.model.wholikedwhat

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface WhoLikedWhatRepository: ReactiveCrudRepository<WhoLikedWhat, Long> {
    @Query("SELECT liked FROM who_liked_what WHERE (ip, post_id) = (inet_aton(:ip), :post_id)")
    fun getByIpAndId(
        @Param("ip") ip: String,
        @Param("post_id") post_id: Long): Mono<Boolean>
    @Query("SELECT count(*)>0 FROM wesweb_term_exam.who_liked_what WHERE (ip, post_id) = (inet_aton(:ip), :post_id);")
    fun checkIfExistsByIpAndId(
        @Param("ip") ip: String,
        @Param("post_id") post_id: Long
    ): Mono<Boolean>
}