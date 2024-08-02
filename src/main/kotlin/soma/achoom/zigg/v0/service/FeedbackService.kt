package soma.achoom.zigg.v0.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.dto.request.FeedbackRequestDto
import soma.achoom.zigg.v0.dto.response.FeedbackResponseDto
import soma.achoom.zigg.v0.repository.*

@Service
class FeedbackService @Autowired constructor(
    private val historyRepository: HistoryRepository,
    private val feedbackRepository: FeedbackRepository,
    private val spaceUserRepository: SpaceUserRepository,
    private val spaceRepository: SpaceRepository,
    private val feedbackRecipientRepository: FeedbackRecipientRepository
) : SpaceAsset() {
    fun getFeedbacks(
        authentication: Authentication,
        spaceId: Long,
        historyId: Long
    ): List<FeedbackResponseDto> {
        val user = getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw IllegalArgumentException("space not found")

        validateSpaceUser(user, space)

        val history =
            historyRepository.findHistoryByHistoryId(historyId) ?: throw IllegalArgumentException("history not found")
        val feedbacks = history.feedbacks.toMutableSet() ?: mutableSetOf()
        return feedbacks.filter{!it.isDeleted}.map {
            FeedbackResponseDto.from(it)
        }
    }

    fun createFeedback(
        authentication: Authentication,
        spaceId: Long,
        historyId: Long,
        feedbackRequestDto: FeedbackRequestDto
    ): FeedbackResponseDto {
        val user = getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw IllegalArgumentException("space not found")
        val spaceUser = validateSpaceUser(user, space)

        val history = historyRepository.findHistoryByHistoryId(historyId)
                ?: throw IllegalArgumentException("history not found")

        val feedbackRecipient = feedbackRequestDto.recipientId.map {
            spaceUserRepository.findSpaceUserBySpaceUserId(it)
                ?: throw IllegalArgumentException("recipient not found")
        }.toMutableSet()

        val feedback = feedbackRequestDto.toFeedBack(history,spaceUser,feedbackRecipient)
//        history.feedbacks.add(feedback)
//        historyRepository.save(history)
//        return FeedbackResponseDto.from(feedback)
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
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw IllegalArgumentException("space not found")

        val spaceUser = validateSpaceUser(user, space)

        val history =
            historyRepository.findHistoryByHistoryId(historyId) ?: throw IllegalArgumentException("history not found")
        val feedback =
            feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw IllegalArgumentException("feedback not found")
        feedback.isDeleted = true
        feedbackRepository.save(feedback)

        val newFeedbackResponseDto =
            createFeedback(authentication, spaceId, historyId, feedbackRequestDto)

        return newFeedbackResponseDto
    }

    fun deleteFeedback(
        authentication: Authentication,
        spaceId: Long,
        historyId: Long,
        feedbackId: Long
    ) {
        val user = getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw IllegalArgumentException("space not found")

        val spaceUser = validateSpaceUser(user, space)

        val history =
            historyRepository.findHistoryByHistoryId(historyId)
                ?: throw IllegalArgumentException("history not found")
        val feedback =
            feedbackRepository.findFeedbackByFeedbackId(feedbackId)
                ?: throw IllegalArgumentException("feedback not found")

        feedback.isDeleted = true
        feedbackRepository.save(feedback)
    }

    fun getFeedback(
        authentication: Authentication,
        spaceId: Long,
        historyId: Long,
        feedbackId: Long
    ): FeedbackResponseDto {
        val user = getAuthUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw IllegalArgumentException("space not found")

        val spaceUser = validateSpaceUser(user, space)

        val history =
            historyRepository.findHistoryByHistoryId(historyId)
                ?: throw IllegalArgumentException("history not found")
        val feedback =
            feedbackRepository.findFeedbackByFeedbackId(feedbackId)
                ?: throw IllegalArgumentException("feedback not found")
        return FeedbackResponseDto.from(feedback)
    }


}

