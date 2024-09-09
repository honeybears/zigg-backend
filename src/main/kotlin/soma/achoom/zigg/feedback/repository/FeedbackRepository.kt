package soma.achoom.zigg.feedback.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.space.entity.SpaceUser
import java.util.UUID

interface FeedbackRepository : JpaRepository<Feedback, UUID> {
    fun findFeedbackByFeedbackId(feedback: UUID): Feedback?
    fun findFeedbacksByFeedbackCreator(feedbackCreator: SpaceUser): List<Feedback>
    fun deleteAllByFeedbackCreator(feedbackCreator: SpaceUser)
}