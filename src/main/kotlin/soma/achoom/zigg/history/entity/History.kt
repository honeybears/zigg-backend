package soma.achoom.zigg.history.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.feedback.entity.Feedback
import java.util.UUID

@Entity
class History(
    @Id
    var historyId: UUID = UUID.randomUUID(),

    var historyName: String?,

    var historyVideoKey: String,

//    @ManyToOne
//    @JoinColumn(name = "space_id")
//    @JsonBackReference
//    var space: soma.achoom.zigg.space.entity.Space,

    var historyVideoThumbnailUrl: String,

    var videoDuration: String? = null,
    @OneToMany( cascade = [CascadeType.ALL], orphanRemoval = true)
    var feedbacks: MutableSet<Feedback> = mutableSetOf(),

) : BaseEntity() {
}