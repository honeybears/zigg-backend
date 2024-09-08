package soma.achoom.zigg.feedback.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import soma.achoom.zigg.space.entity.SpaceUser
import java.util.*

@Entity
class FeedbackRecipient(
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
}