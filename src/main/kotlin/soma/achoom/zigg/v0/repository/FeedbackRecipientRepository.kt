package soma.achoom.zigg.v0.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.v0.model.FeedbackRecipient
import java.util.UUID

interface FeedbackRecipientRepository : JpaRepository<FeedbackRecipient, UUID> {

}