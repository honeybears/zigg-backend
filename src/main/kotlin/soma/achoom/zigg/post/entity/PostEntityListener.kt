package soma.achoom.zigg.post.entity

import jakarta.persistence.PrePersist
import jakarta.persistence.PreRemove

class PostEntityListener {
    @PreRemove
    fun onPreDelete(post:Post) {
        post.detach()
        for (likes in post.likes) {
            likes.user.likedPosts.remove(likes)
        }
        for (scraps in post.scraps) {
            scraps.user.scrapedPosts.remove(scraps)
        }
    }
    @PrePersist
    fun onPrePersist(post:Post) {
        post.board.posts.add(post)
    }
}