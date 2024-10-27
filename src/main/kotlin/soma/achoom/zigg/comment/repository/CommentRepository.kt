package soma.achoom.zigg.comment.repository

import org.jetbrains.annotations.TestOnly
import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.comment.entity.Comment
import soma.achoom.zigg.user.entity.User

interface CommentRepository : JpaRepository<Comment,Long> {
    @TestOnly
    fun findCommentsByCreator(user: User): List<Comment>
}