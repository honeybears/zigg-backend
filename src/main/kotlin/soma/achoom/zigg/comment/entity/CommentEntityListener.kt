package soma.achoom.zigg.comment.entity

import jakarta.persistence.PrePersist
import javax.annotation.PreDestroy

class CommentEntityListener {
    @PrePersist
    fun prePersist(comment: Comment) {
        comment.parentComment?.replies?.add(comment)
        comment.post.commentCnt++
    }
    @PreDestroy
    fun preDestroy(comment: Comment) {
        comment.parentComment?.replies?.remove(comment)
        comment.post.commentCnt--
    }
}