package soma.achoom.zigg.feedback.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.feedback.entity.FeedbackRecipient
import java.util.UUID

interface FeedbackRecipientRepository : JpaRepository<FeedbackRecipient, UUID> {
    fun findAllByFeedback(feedback: Feedback): List<FeedbackRecipient>
}