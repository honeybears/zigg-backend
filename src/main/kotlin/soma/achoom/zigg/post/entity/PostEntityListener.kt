package soma.achoom.zigg.post.entity

import jakarta.persistence.PrePersist
import jakarta.persistence.PreRemove

class PostEntityListener {
    @PrePersist
    fun prePersist(post: Post) {
        post.board.postCount++
    }
    @PreRemove
    fun preRemove(post: Post) {
        post.board.postCount--
    }
}