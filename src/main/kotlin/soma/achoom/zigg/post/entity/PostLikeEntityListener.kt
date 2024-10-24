package soma.achoom.zigg.post.entity

import jakarta.persistence.PrePersist
import jakarta.persistence.PreRemove

class PostLikeEntityListener {
    @PrePersist
    fun onPrePersist(postLike: PostLike) {
        postLike.user.likedPosts.add(postLike)
        postLike.post.likes.add(postLike)
    }

    @PreRemove
    fun onPreRemove(postLike: PostLike) {
        postLike.user.likedPosts.remove(postLike)
        postLike.post.likes.remove(postLike)
    }
}