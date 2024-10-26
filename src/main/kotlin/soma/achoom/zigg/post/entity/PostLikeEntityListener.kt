package soma.achoom.zigg.post.entity

import jakarta.persistence.PrePersist
import jakarta.persistence.PreRemove

class PostLikeEntityListener {
    @PrePersist
    fun prePersist(postLike: PostLike) {
        postLike.post.likeCnt++
    }
    @PreRemove
    fun preDestroy(postLike: PostLike) {
        postLike.post.likeCnt--
    }
}