package soma.achoom.zigg.v0.feedback.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.feedback.repository.FeedbackRepository
import soma.achoom.zigg.v0.feedback.dto.FeedbackRequestDto
import soma.achoom.zigg.v0.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.v0.feedback.exception.FeedbackNotFoundException
import soma.achoom.zigg.v0.history.exception.HistoryNotFoundException
import soma.achoom.zigg.v0.space.exception.SpaceNotFoundException
import soma.achoom.zigg.v0.space.exception.SpaceUserNotFoundInSpaceException
import soma.achoom.zigg.v0.history.repository.HistoryRepository
import soma.achoom.zigg.global.util.SpaceAsset
import soma.achoom.zigg.v0.space.repository.SpaceRepository
import soma.achoom.zigg.v0.space.repository.SpaceUserRepository
import java.util.UUID

@Service
class FeedbackService @Autowired constructor(
    private val historyRepository: HistoryRepository,
    private val feedbackRepository: FeedbackRepository,
    private val spaceUserRepository: SpaceUserRepository,
    private val spaceRepository: SpaceRepository,
) : SpaceAsset() {

    fun getFeedbacks(
        authentication: Authentication, spaceId: UUID, historyId: UUID
    ): List<FeedbackResponseDto> {
        val user = getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)

        val history = historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        val feedbacks = history.feedbacks.toMutableSet()
        return feedbacks.filter { !it.isDeleted }.map {
            FeedbackResponseDto.from(it)
        }
    }

    fun createFeedback(
        authentication: Authentication, spaceId: UUID, historyId: UUID, feedbackRequestDto: FeedbackRequestDto
    ): FeedbackResponseDto {
        val user = getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        val spaceUser = validateSpaceUser(user, space)

        val history = historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()

        val feedbackRecipient = feedbackRequestDto.recipientId.map {
            spaceUserRepository.findSpaceUserBySpaceUserId(it) ?: throw SpaceUserNotFoundInSpaceException()
        }.toMutableSet()

        val feedback = feedbackRequestDto.toFeedBack(history, spaceUser, feedbackRecipient)
        feedbackRepository.save(feedback)
        return FeedbackResponseDto.from(feedback)
    }

    fun updateFeedback(
        authentication: Authentication,
        spaceId: UUID,
        historyId: UUID,
        feedbackId: UUID,
        feedbackRequestDto: FeedbackRequestDto
    ): FeedbackResponseDto {
        val user = getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUserRoleIsAdmin(user, space)

        historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        val feedback = feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw FeedbackNotFoundException()
        feedback.isDeleted = true
        feedbackRepository.save(feedback)

        val newFeedbackResponseDto = createFeedback(authentication, spaceId, historyId, feedbackRequestDto)

        return newFeedbackResponseDto
    }

    fun deleteFeedback(
        authentication: Authentication, spaceId: UUID, historyId: UUID, feedbackId: UUID
    ) {
        val user = getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)


        historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        val feedback = feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw FeedbackNotFoundException()

        feedback.isDeleted = true
        feedbackRepository.save(feedback)
    }

    fun getFeedback(
        authentication: Authentication, spaceId: UUID, historyId: UUID, feedbackId: UUID
    ): FeedbackResponseDto {
        val user = getAuthUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)


        historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        val feedback = feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw FeedbackNotFoundException()
        return FeedbackResponseDto.from(feedback)
    }


}

