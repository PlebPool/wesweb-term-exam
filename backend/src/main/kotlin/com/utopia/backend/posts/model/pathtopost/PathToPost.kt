package com.utopia.backend.posts.model.pathtopost

import org.springframework.data.annotation.Id

data class PathToPost(
    @Id var post_id: Long,
    val the_path: String
) {
    override fun toString(): String {
        return "PathToPost(post_id=$post_id, relative_path='$the_path')"
    }
}
