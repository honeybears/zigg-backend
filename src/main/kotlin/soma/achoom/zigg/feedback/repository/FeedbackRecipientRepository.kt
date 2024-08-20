package soma.achoom.zigg.feedback.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.feedback.entity.FeedbackRecipient
import java.util.UUID

interface FeedbackRecipientRepository : JpaRepository<FeedbackRecipient, UUID> {

}