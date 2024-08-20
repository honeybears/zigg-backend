package soma.achoom.zigg.version.v0.feedback.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.version.v0.feedback.entity.FeedbackRecipient
import java.util.UUID

interface FeedbackRecipientRepository : JpaRepository<FeedbackRecipient, UUID> {

}