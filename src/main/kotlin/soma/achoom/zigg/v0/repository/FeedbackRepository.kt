package soma.achoom.zigg.v0.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.v0.model.Feedback
import java.util.UUID

interface FeedbackRepository : JpaRepository<Feedback,UUID>{
    fun findFeedbackByFeedbackId(feedback: UUID): Feedback?
}