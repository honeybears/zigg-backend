package soma.achoom.zigg.comment.entity

import jakarta.persistence.PrePersist

class CommentEntityListener {
    @PrePersist
    fun prePersist(comment: Comment) {
        comment.parentComment?.replies?.add(comment)
    }
}