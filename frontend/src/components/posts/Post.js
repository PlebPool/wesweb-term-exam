import { useState, useEffect } from 'react'
import axios from 'axios'
import LoadingIcons from 'react-loading-icons'

const Post = ({ postRef }) => {
    /*
    const mock = {
        post_id: 0,
        alias: "alias",
        title: "title",
        content: "content",
        likes: 0,
        liked: false,
        dateOfPost: "date",
        timeOfPost: "time"
    }
    */
    const [liked, setLiked] = useState(false)
    const [post, setPost] = useState()
    const [likes, setLikes] = useState(0)
    const [isLoading, setIsLoading] = useState(true)
    const [post_id, setPost_id] = useState(0)

    useEffect(() => {
        setIsLoading(true)
        setTimeout(() => { // This timeout only exists to show off the cool loading icon.
            axios.get(postRef.the_path).then((res) => {
                //console.log(res.data)
                setPost(res.data)
                setLikes(res.data.likes)
                setLiked(res.data.hasLiked)
                setPost_id(res.data.post_id)
                setIsLoading(false)
            })
        }, 1000);
    }, [postRef])

    const doLike = (like) => {
        console.log(like)
        setLiked(!liked)
        let data = {
            isLike: like
        }
        if(like) setLikes(likes+1)
        else setLikes(likes-1)
        axios.put(`/posts/like/${post_id}`, data).then((res) => {
            console.log(res.data)
        })
    }

    return (
        <div className="post">
            {isLoading ? <LoadingIcons.Puff strokeOpacity={1} stroke="black" fillOpacity={1} speed={.5} /> : <div>
                <header className="postHead">
                    <div className="postInfo">
                        <span>{`${post.alias} | ${post.dateOfPost} | ${post.timeOfPost} |`}</span>
                        <input type="checkbox" onChange={(e) => doLike((e.target.checked))} checked={liked}></input>
                        <span>{`${likes} Likes`}</span>
                    </div>
                    <h1 className="postTitle">{post.title}</h1>
                </header>
                <main className="postContent">
                    {post.content.split("\\n").map((paragraph, i) => {
                    return <p key={i} dangerouslySetInnerHTML={{__html: paragraph}}></p>
                    })}
                </main>
            </div>}
        </div>
    )
}

export default Post