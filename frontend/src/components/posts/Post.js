import { useState } from 'react'
import axios from 'axios'

const Post = ({ post }) => {
    const [liked, setLiked] = useState(false)
    //const [post, setPost] = useState(tmpPost)
    const [likes, setLikes] = useState(post.likes)

    const doLike = (like) => {
        console.log(like)
        setLiked(!liked)
        let data = {
            isLike: like
        }
        if(like) setLikes(likes+1)
        else setLikes(likes-1)
        axios.put(`/posts/like/${post.id}`, data).then((res) => {
            console.log(res.data)
        })
    }

    return (
        <div className="post" key={post.id}>
            <header className="postHead">
                <div className="postInfo">
                    <span>{`${post.alias} | ${post.dateOfPost} | ${post.timeOfPost} |`}</span>
                    <input type="checkbox" onChange={(e) => doLike((e.target.checked))} checked={liked}></input>
                    <span>{`${likes} Likes`}</span>
                </div>
                <h1 className="postTitle">{post.title}</h1>
            </header>
            <section className="postContent">
                {post.content.split("\\n").map((paragraph, i) => {
                return <p key={i} dangerouslySetInnerHTML={{__html: paragraph}}></p>
                })}
            </section>
        </div>
    )
}

export default Post
