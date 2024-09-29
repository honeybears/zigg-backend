package soma.achoom.zigg.history.entity

import jakarta.persistence.*
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.entity.Video
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.space.entity.Space
import java.util.UUID

@Entity
class History(
    @Id
    var historyId: UUID = UUID.randomUUID(),

    var historyName: String?,

    @OneToOne
    var historyVideoKey: Video,

    @OneToOne
    var historyVideoThumbnailUrl : Image,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var feedbacks: MutableSet<Feedback> = mutableSetOf(),

    ) : BaseEntity() {
}