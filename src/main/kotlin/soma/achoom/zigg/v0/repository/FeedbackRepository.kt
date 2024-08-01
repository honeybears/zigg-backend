package soma.achoom.zigg.v0.repository

import org.springframework.data.jpa.repository.JpaRepository
import soma.achoom.zigg.v0.model.Feedback

interface FeedbackRepository : JpaRepository<Feedback,Long>{
    fun findFeedbackByFeedbackId(feedback: Long): Feedback?
}