package com.utopia.backend.posts.model

import org.springframework.data.annotation.Id

data class PathToPost(
        @Id val post_id: Long,
        val the_path: String
) {
    override fun toString(): String {
        return "PathToPost(post_id=$post_id, relative_path='$the_path')"
    }
}
