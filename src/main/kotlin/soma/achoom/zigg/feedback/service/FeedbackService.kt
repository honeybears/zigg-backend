package soma.achoom.zigg.feedback.service


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.feedback.dto.FeedbackRequestDto
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.feedback.exception.FeedbackNotFoundException
import soma.achoom.zigg.feedback.repository.FeedbackRepository
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.history.exception.HistoryNotFoundException
import soma.achoom.zigg.history.repository.HistoryRepository
import soma.achoom.zigg.space.exception.SpaceNotFoundException
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.space.repository.SpaceUserRepository
import soma.achoom.zigg.space.service.SpaceService
import soma.achoom.zigg.space.exception.SpaceUserNotFoundInSpaceException
import soma.achoom.zigg.user.service.UserService

import java.util.UUID

@Service
class FeedbackService @Autowired constructor(
    private val historyRepository: HistoryRepository,
    private val feedbackRepository: FeedbackRepository,
    private val spaceUserRepository: SpaceUserRepository,
    private val spaceRepository: SpaceRepository,
    private val spaceService: SpaceService,
    private val userService: UserService,
    private val responseDtoManager: ResponseDtoManager
) {
    @Transactional(readOnly = true)
    fun getFeedbacks(authentication: Authentication, spaceId: UUID, historyId: UUID): List<Feedback> {
        val user = userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        spaceService.validateSpaceUser(user, space)

        val history = historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        return history.feedbacks.toList()
    }

    @Transactional(readOnly = false)
    fun createFeedback(authentication: Authentication, spaceId: UUID, historyId: UUID, feedbackRequestDto: FeedbackRequestDto): Feedback {
        val user = userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        val spaceUser = spaceService.validateSpaceUser(user, space)

        val history = historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()

        val feedbackRecipient = feedbackRequestDto.recipientId.map {
            spaceUserRepository.findSpaceUserBySpaceUserId(it) ?: throw SpaceUserNotFoundInSpaceException()
        }.toMutableSet()

        val feedback = Feedback(
            feedbackTimeline = feedbackRequestDto.feedbackTimeline,
            feedbackMessage = feedbackRequestDto.feedbackMessage,
            feedbackCreator = spaceUser,
        )

        feedback.recipients.addAll(feedbackRecipient)
        history.feedbacks.add(feedback)
        historyRepository.save(history)

        return feedback
    }

    @Transactional(readOnly = false)
    fun updateFeedback(authentication: Authentication, spaceId: UUID, historyId: UUID, feedbackId: UUID, feedbackRequestDto: FeedbackRequestDto): Feedback {
        val user = userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        spaceService.validateSpaceUser(user, space)

        historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        val feedback = feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw FeedbackNotFoundException()

        feedback.recipients.addAll(
            feedbackRequestDto.recipientId.map {
                spaceUserRepository.findSpaceUserBySpaceUserId(it) ?: throw SpaceUserNotFoundInSpaceException()
            }
        )
        feedback.feedbackTimeline = feedbackRequestDto.feedbackTimeline
        feedback.feedbackMessage = feedbackRequestDto.feedbackMessage

        feedbackRepository.save(feedback)

        return feedback
    }

    @Transactional(readOnly = false)
    fun deleteFeedback(authentication: Authentication, spaceId: UUID, historyId: UUID, feedbackId: UUID) {
        val user = userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        spaceService.validateSpaceUser(user, space)

        val history = historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        val feedback = feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw FeedbackNotFoundException()

        history.feedbacks.remove(feedback)

        historyRepository.save(history)
    }

    @Transactional(readOnly = true)
    fun getFeedback(authentication: Authentication, spaceId: UUID, historyId: UUID, feedbackId: UUID): FeedbackResponseDto {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        spaceService.validateSpaceUser(user, space)

        historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        val feedback = feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw FeedbackNotFoundException()

        return responseDtoManager.generateFeedbackResponseDto(feedback)
    }
}

