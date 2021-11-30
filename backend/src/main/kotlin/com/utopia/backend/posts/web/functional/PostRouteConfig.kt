package com.utopia.backend.posts.web.functional

import com.utopia.backend.generics.beans.SpringManaged
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Suppress("unused")
@Configuration
class PostRouteConfig(
        val handler: PostHandler
) : SpringManaged() {
    @Bean
    fun routes(): RouterFunction<ServerResponse> {
        return coRouter {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/posts") { handler.getAll(it) }
                GET("/posts/{post_id}") { handler.getOne(it) }
                POST("/posts") { handler.createOne(it) }
                PUT("/posts/like/{post_id}") { handler.like(it) }
            }
        }
    }
}