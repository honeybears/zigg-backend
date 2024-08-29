package soma.achoom.zigg.feedback.service


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.feedback.dto.FeedbackRequestDto
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto

import soma.achoom.zigg.feedback.entity.FeedbackRecipient
import soma.achoom.zigg.feedback.exception.FeedbackNotFoundException
import soma.achoom.zigg.feedback.repository.FeedbackRepository
import soma.achoom.zigg.global.ResponseDtoGenerator
import soma.achoom.zigg.history.exception.HistoryNotFoundException
import soma.achoom.zigg.history.repository.HistoryRepository
import soma.achoom.zigg.space.exception.SpaceNotFoundException
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.spaceuser.repository.SpaceUserRepository
import soma.achoom.zigg.space.service.SpaceService
import soma.achoom.zigg.spaceuser.exception.SpaceUserNotFoundInSpaceException
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
    private val responseDtoGenerator: ResponseDtoGenerator
) {

    fun getFeedbacks(
        authentication: Authentication, spaceId: UUID, historyId: UUID
    ): List<FeedbackResponseDto> {
        val user = userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        spaceService.validateSpaceUser(user, space)

        val history = historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        val feedbacks = history.feedbacks.toMutableSet()

        return feedbacks.filter { !it.isDeleted }.map {
            responseDtoGenerator.generateFeedbackResponseDto(it)
        }.toList()
    }


    fun createFeedback(
        authentication: Authentication, spaceId: UUID, historyId: UUID, feedbackRequestDto: FeedbackRequestDto
    ): FeedbackResponseDto {
        val user = userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        val spaceUser = spaceService.validateSpaceUser(user, space)

        val history = historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()

        val feedbackRecipient = feedbackRequestDto.recipientId.map {
            spaceUserRepository.findSpaceUserBySpaceUserId(it) ?: throw SpaceUserNotFoundInSpaceException()
        }.toMutableSet()

        val feedback = feedbackRequestDto.toFeedBack(history, spaceUser, feedbackRecipient)
        feedbackRepository.save(feedback)
        return responseDtoGenerator.generateFeedbackResponseDto(feedback)
    }

    fun updateFeedback(
        authentication: Authentication,
        spaceId: UUID,
        historyId: UUID,
        feedbackId: UUID,
        feedbackRequestDto: FeedbackRequestDto
    ): FeedbackResponseDto {
        val user = userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        spaceService.validateSpaceUserRoleIsAdmin(user, space)

        historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        val feedback = feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw FeedbackNotFoundException()

        feedback.recipients.addAll(
            feedbackRequestDto.recipientId.map {
                val spaceUser =
                    spaceUserRepository.findSpaceUserBySpaceUserId(it) ?: throw SpaceUserNotFoundInSpaceException()
                FeedbackRecipient(
                    feedback = feedback,
                    recipient = spaceUser
                )
            }
        )
        feedback.feedbackTimeline = feedbackRequestDto.feedbackTimeline
        feedback.feedbackMessage = feedbackRequestDto.feedbackMessage

        feedbackRepository.save(feedback)

        return responseDtoGenerator.generateFeedbackResponseDto(feedback)
    }

    fun deleteFeedback(
        authentication: Authentication, spaceId: UUID, historyId: UUID, feedbackId: UUID
    ) {
        val user = userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        spaceService.validateSpaceUser(user, space)


        historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        val feedback = feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw FeedbackNotFoundException()

        feedback.isDeleted = true
        feedbackRepository.save(feedback)
    }

    fun getFeedback(
        authentication: Authentication, spaceId: UUID, historyId: UUID, feedbackId: UUID
    ): FeedbackResponseDto {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        spaceService.validateSpaceUser(user, space)


        historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        val feedback = feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw FeedbackNotFoundException()
        return responseDtoGenerator.generateFeedbackResponseDto(feedback)
    }
}

