package com.utopia.backend.posts.model.wholikedwhat

/*
    This is a domain class that allows us to handle the likes column as an object.
    If liked == true, then we increment the likes column of whatever ID it's targeting.
    Example:
            {
                Liked: true
            }
*/

data class WhoLikedWhat(
        val ip: Int,
        var post_id: Long,
        val liked: Boolean
)