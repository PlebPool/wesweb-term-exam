import React from 'react'
import "./Posts.css"
import Post from './Post'

const Posts = ({ posts }) => {

    return (
        <main className="posts">
            {posts.slice(0).reverse().map((post) => {
            return(
                <Post post={post} key={post.id} />
            )
            })}
        </main>
    )
}

export default Posts