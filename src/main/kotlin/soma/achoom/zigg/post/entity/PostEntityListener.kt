package soma.achoom.zigg.post.entity

import jakarta.persistence.PrePersist
import jakarta.persistence.PreRemove

class PostEntityListener {
    @PreRemove
    fun onPreDelete(post:Post) {
        post.board.posts.remove(post)
    }
    @PrePersist
    fun onPrePersist(post:Post) {
        post.board.posts.add(post)
    }
}