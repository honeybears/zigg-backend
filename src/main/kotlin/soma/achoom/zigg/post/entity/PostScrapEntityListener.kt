package soma.achoom.zigg.post.entity

import jakarta.persistence.PrePersist
import jakarta.persistence.PreRemove

class PostScrapEntityListener {
    @PrePersist
    fun onPrePersist(postScrap: PostScrap) {
        postScrap.user.scrapedPosts.add(postScrap)
        postScrap.post.scraps.add(postScrap)
        postScrap.post.scrapCnt++
    }

    @PreRemove
    fun onPreRemove(postScrap: PostScrap) {
        postScrap.user.scrapedPosts.remove(postScrap)
        postScrap.post.scraps.remove(postScrap)
        postScrap.post.scrapCnt--
    }
}