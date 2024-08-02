package soma.achoom.zigg.v0.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.v0.model.FeedbackRecipient

interface FeedbackRecipientRepository : JpaRepository<FeedbackRecipient, Long> {

}