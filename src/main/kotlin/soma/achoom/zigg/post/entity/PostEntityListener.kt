package soma.achoom.zigg.post.entity

import jakarta.persistence.PreRemove
import jakarta.persistence.PreUpdate
import soma.achoom.zigg.space.entity.Space
import java.time.LocalDateTime
import javax.annotation.PreDestroy

class PostEntityListener {
    @PreRemove
    fun onPreDelete(post:Post) {
        post.detach()
    }
}