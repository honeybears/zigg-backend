package soma.achoom.zigg.post.entity

import jakarta.persistence.PrePersist
import jakarta.persistence.PreRemove

class PostScrapEntityListener {
    @PrePersist
    fun prePersist(postScrap: PostScrap) {
        postScrap.post.scrapCnt++
    }
    @PreRemove
    fun preDestroy(postScrap: PostScrap) {
        postScrap.post.scrapCnt--
    }
}