package soma.achoom.zigg.space.entity

import jakarta.persistence.EntityListeners
import jakarta.persistence.PreUpdate
import java.time.LocalDateTime

class SpaceEntityListener {
    @PreUpdate
    fun onPreUpdate(space: Space) {
        space.updateAt = LocalDateTime.now()
    }
}