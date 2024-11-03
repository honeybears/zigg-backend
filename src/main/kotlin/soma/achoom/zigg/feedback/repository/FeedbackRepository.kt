package soma.achoom.zigg.feedback.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.space.entity.SpaceUser
import java.util.UUID

interface FeedbackRepository : JpaRepository<Feedback, Long> {
    fun findFeedbackByFeedbackId(feedback: Long): Feedback?
}