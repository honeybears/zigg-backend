package soma.achoom.zigg.version.v0.feedback.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import soma.achoom.zigg.version.v0.space.entity.SpaceUser
import java.util.*

@Entity
data class FeedbackRecipient(
    @Id
    @JsonBackReference
    var id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    @JsonBackReference
    var feedback: Feedback? = null,

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    var recipient: SpaceUser

){
    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val feedbackRecipient = other as FeedbackRecipient
        return id == feedbackRecipient.id && feedback == feedbackRecipient.feedback && recipient == feedbackRecipient.recipient
    }
}