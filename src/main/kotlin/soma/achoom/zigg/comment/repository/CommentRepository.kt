package soma.achoom.zigg.comment.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.comment.entity.Comment
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.user.entity.User

interface CommentRepository : JpaRepository<Comment,Long> {
    fun findCommentsByCreator(user: User): List<Comment>
    fun findCommentsByPost(post: Post): List<Comment>
    fun countCommentsByPost(post: Post): Long
}