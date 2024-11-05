package soma.achoom.zigg.comment.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.comment.entity.Comment
import soma.achoom.zigg.comment.entity.CommentLike
import soma.achoom.zigg.user.entity.User

interface CommentLikeRepository : JpaRepository<CommentLike,Long> {
    fun findCommentLikeByCommentAndUser(comment: Comment, user: User): CommentLike?
}