package soma.achoom.zigg.history.entity

import jakarta.persistence.*
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.entity.Video
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.feedback.entity.Feedback
import java.util.UUID

@Entity
class History(
    @Id
    var historyId: UUID = UUID.randomUUID(),

    var name: String?,

    @OneToOne(cascade = [CascadeType.PERSIST])
    var videoKey: Video,

    @OneToOne(cascade = [CascadeType.PERSIST])
    var videoThumbnailUrl: Image,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var feedbacks: MutableSet<Feedback> = mutableSetOf(),

    ) : BaseEntity() {
}