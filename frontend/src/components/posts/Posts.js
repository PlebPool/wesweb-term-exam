import "./Posts.css"
import Post from './Post'

const Posts = ({ postRefs }) => {
    return (
        <main className="posts">
            {postRefs.map((postRef) => {
                return <Post postRef={postRef} key={postRef.post_id} />
            })}
        </main>
    )
}

export default Posts