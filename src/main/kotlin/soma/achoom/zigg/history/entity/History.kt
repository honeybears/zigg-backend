package soma.achoom.zigg.history.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import soma.achoom.zigg.global.util.BaseEntity
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.space.entity.Space
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
    var space: soma.achoom.zigg.space.entity.Space,

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