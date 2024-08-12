package soma.achoom.zigg.v0.feedback.entity

import jakarta.persistence.*
import soma.achoom.zigg.global.util.BaseEntity
import soma.achoom.zigg.v0.history.entity.History
import soma.achoom.zigg.v0.space.entity.SpaceUser
import soma.achoom.zigg.v0.feedback.dto.FeedbackType
import java.util.*

@Entity
data class Feedback(
    @Id
    var feedbackId: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    var feedbackType: FeedbackType? = FeedbackType.USER,

    var feedbackTimeline: String?,

    var feedbackMessage: String?,

    @ManyToOne
    @JoinColumn(name = "history_id")
    var history: History?,

    @ManyToOne
    @JoinColumn(name = "creator_id", unique = false)
    var feedbackCreator: SpaceUser?,

    @OneToMany(mappedBy = "feedback", cascade = [CascadeType.ALL], orphanRemoval = true)
    var recipients: MutableSet<FeedbackRecipient> = mutableSetOf(),

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false

) : BaseEntity() {
    override fun hashCode(): Int {
        return Objects.hash(feedbackId)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val feedback = other as Feedback
        return feedbackId == feedback.feedbackId
                && feedbackType == feedback.feedbackType
                && feedbackTimeline == feedback.feedbackTimeline
                && feedbackMessage == feedback.feedbackMessage
                && feedbackCreator == feedback.feedbackCreator
                && createAt == feedback.createAt
                && updateAt == feedback.updateAt
                && isDeleted == feedback.isDeleted
    }

}