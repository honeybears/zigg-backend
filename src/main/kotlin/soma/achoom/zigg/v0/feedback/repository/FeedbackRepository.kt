package soma.achoom.zigg.v0.feedback.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.v0.feedback.entity.Feedback
import java.util.UUID

interface FeedbackRepository : JpaRepository<Feedback,UUID>{
    fun findFeedbackByFeedbackId(feedback: UUID): Feedback?
}