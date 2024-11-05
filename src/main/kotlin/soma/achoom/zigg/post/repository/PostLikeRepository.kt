package soma.achoom.zigg.post.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.entity.PostLike
import soma.achoom.zigg.user.entity.User

interface PostLikeRepository : JpaRepository<PostLike, Long> {
    fun findByUser(user: User):List<PostLike>
    fun findByPost(post: Post):List<PostLike>
    fun existsPostLikeByPostAndUser(post: Post, user: User):Boolean
    fun findByPostAndUser(post: Post, user: User):PostLike?
    fun countPostLikesByPost(post: Post):Long
}