package soma.achoom.zigg.comment.entity

import jakarta.persistence.PrePersist
import jakarta.persistence.PreRemove

class CommentLikeEntityListener {
    @PrePersist
    fun prePersist(commentLike: CommentLike) {
        commentLike.comment?.likes = commentLike.comment?.likes?.plus(1) ?: 1
    }
    @PreRemove
    fun preRemove(commentLike: CommentLike) {
        commentLike.comment?.likes = commentLike.comment?.likes?.minus(1) ?: 0
    }
}