package soma.achoom.zigg.v0.history.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import soma.achoom.zigg.v0.feedback.entity.Feedback
import soma.achoom.zigg.global.util.BaseEntity
import soma.achoom.zigg.v0.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.v0.history.dto.HistoryResponseDto
import soma.achoom.zigg.v0.space.entity.Space
import java.util.UUID

@Entity
data class History(
    @Id
    var historyId: UUID = UUID.randomUUID(),

    var historyName: String?,

    var historyVideoKey: String,

    @ManyToOne
    @JoinColumn(name = "space_id")
    @JsonBackReference
    var space: Space,

    var historyVideoThumbnailUrl: String? = null,

    var videoDuration: String? = null,
    @OneToMany(mappedBy = "history", cascade = [CascadeType.ALL], orphanRemoval = true)
    var feedbacks: MutableSet<Feedback> = mutableSetOf(),


    @Column(name = "is_deleted")
    var isDeleted: Boolean = false

) : BaseEntity() {

    override fun hashCode(): Int {
        return historyId.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is History) return false
        return historyId == other.historyId && historyName == other.historyName && space == other.space && feedbacks == other.feedbacks && isDeleted == other.isDeleted
    }
}