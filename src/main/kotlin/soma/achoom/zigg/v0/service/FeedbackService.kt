package soma.achoom.zigg.v0.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.dto.request.FeedbackRequestDto
import soma.achoom.zigg.v0.dto.response.FeedbackResponseDto
import soma.achoom.zigg.v0.exception.UnKnownFeedback
import soma.achoom.zigg.v0.exception.UnknownHistory
import soma.achoom.zigg.v0.exception.UnknownSpace
import soma.achoom.zigg.v0.repository.*

@Service
class FeedbackService @Autowired constructor(
    private val historyRepository: HistoryRepository,
    private val feedbackRepository: FeedbackRepository,
    private val spaceUserRepository: SpaceUserRepository,
    private val spaceRepository: SpaceRepository,
) : SpaceAsset() {

    fun getFeedbacks(
        authentication: Authentication, spaceId: Long, historyId: Long
    ): List<FeedbackResponseDto> {
        val user = getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw UnknownSpace()

        validateSpaceUser(user, space)

        val history = historyRepository.findHistoryByHistoryId(historyId) ?: throw UnknownHistory()
        val feedbacks = history.feedbacks.toMutableSet()
        return feedbacks.filter { !it.isDeleted }.map {
            FeedbackResponseDto.from(it)
        }
    }

    fun createFeedback(
        authentication: Authentication, spaceId: Long, historyId: Long, feedbackRequestDto: FeedbackRequestDto
    ): FeedbackResponseDto {
        val user = getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw UnknownSpace()

        val spaceUser = validateSpaceUser(user, space)

        val history = historyRepository.findHistoryByHistoryId(historyId) ?: throw UnknownHistory()

        val feedbackRecipient = feedbackRequestDto.recipientId.map {
            spaceUserRepository.findSpaceUserBySpaceUserId(it) ?: throw UnKnownFeedback()
        }.toMutableSet()

        val feedback = feedbackRequestDto.toFeedBack(history, spaceUser, feedbackRecipient)
        feedbackRepository.save(feedback)
        return FeedbackResponseDto.from(feedback)
    }

    fun updateFeedback(
        authentication: Authentication,
        spaceId: Long,
        historyId: Long,
        feedbackId: Long,
        feedbackRequestDto: FeedbackRequestDto
    ): FeedbackResponseDto {
        val user = getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw UnknownSpace()

        validateSpaceUserRoleIsAdmin(user, space)

        historyRepository.findHistoryByHistoryId(historyId) ?: throw UnknownHistory()
        val feedback = feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw UnKnownFeedback()
        feedback.isDeleted = true
        feedbackRepository.save(feedback)

        val newFeedbackResponseDto = createFeedback(authentication, spaceId, historyId, feedbackRequestDto)

        return newFeedbackResponseDto
    }

    fun deleteFeedback(
        authentication: Authentication, spaceId: Long, historyId: Long, feedbackId: Long
    ) {
        val user = getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw UnknownSpace()

        validateSpaceUser(user, space)


        historyRepository.findHistoryByHistoryId(historyId) ?: throw UnknownHistory()
        val feedback = feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw UnKnownFeedback()

        feedback.isDeleted = true
        feedbackRepository.save(feedback)
    }

    fun getFeedback(
        authentication: Authentication, spaceId: Long, historyId: Long, feedbackId: Long
    ): FeedbackResponseDto {
        val user = getAuthUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw UnknownSpace()

        validateSpaceUser(user, space)


        historyRepository.findHistoryByHistoryId(historyId) ?: throw UnknownHistory()
        val feedback = feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw UnKnownFeedback()
        return FeedbackResponseDto.from(feedback)
    }


}

