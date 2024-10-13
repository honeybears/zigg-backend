package soma.achoom.zigg.post.entity

import jakarta.persistence.PreRemove

class PostEntityListener {
    @PreRemove
    fun onPreDelete(post:Post) {
        post.detach()
    }
}