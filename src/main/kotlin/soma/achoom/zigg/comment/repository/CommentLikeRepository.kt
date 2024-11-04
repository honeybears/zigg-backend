package soma.achoom.zigg.comment.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.comment.entity.CommentLike

interface CommentLikeRepository : JpaRepository<CommentLike,Long> {

}