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
import soma.achoom.zigg.space.dto.SpaceUserResponseDto
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
) {
    @Transactional(readOnly = true)
    fun getFeedbacks(authentication: Authentication, spaceId: Long, historyId: Long): List<FeedbackResponseDto> {
        val user = userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        spaceService.validateSpaceUser(user, space)

        val history = historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        return history.feedbacks.map {
            FeedbackResponseDto(
                feedbackId = it.feedbackId,
                feedbackTimeline = it.timeline,
                feedbackMessage = it.message,
                creatorId = SpaceUserResponseDto(
                    userId = it.creator.user?.userId,
                    userName = it.creator.user?.name,
                    userNickname = it.creator.user?.nickname,
                    profileImageUrl = it.creator.user?.profileImageKey?.imageKey,
                    spaceUserId = it.creator.spaceUserId,
                    spaceRole = it.creator.role,
                ),
                recipientId = it.recipients.map { spaceUser ->
                    SpaceUserResponseDto(
                        userId = spaceUser.user?.userId,
                        userName = spaceUser.user?.name,
                        userNickname = spaceUser.user?.nickname,
                        profileImageUrl = spaceUser.user?.profileImageKey?.imageKey,
                        spaceUserId = spaceUser.spaceUserId,
                        spaceRole = spaceUser.role,
                    )
                }.toMutableSet(),
                feedbackType = it.type
            )
        }
    }

    @Transactional(readOnly = false)
    fun createFeedback(authentication: Authentication, spaceId: Long, historyId: Long, feedbackRequestDto: FeedbackRequestDto): FeedbackResponseDto {
        val user = userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        val spaceUser = spaceService.validateSpaceUser(user, space)

        val history = historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()

        val feedbackRecipient = feedbackRequestDto.recipientId.map {
            spaceUserRepository.findSpaceUserBySpaceUserId(it) ?: throw SpaceUserNotFoundInSpaceException()
        }.toMutableSet()

        val feedback = Feedback(
            timeline = feedbackRequestDto.feedbackTimeline,
            message = feedbackRequestDto.feedbackMessage,
            creator = spaceUser,
        )

        feedback.recipients.addAll(feedbackRecipient)
        history.feedbacks.add(feedback)
        historyRepository.save(history)

        return FeedbackResponseDto(
            feedbackId = feedback.feedbackId,
            feedbackTimeline = feedback.timeline,
            feedbackMessage = feedback.message,
            creatorId = SpaceUserResponseDto(
                userId = feedback.creator.user?.userId,
                userName = feedback.creator.user?.name,
                userNickname = feedback.creator.user?.nickname,
                profileImageUrl = feedback.creator.user?.profileImageKey?.imageKey,
                spaceUserId = feedback.creator.spaceUserId,
                spaceRole = feedback.creator.role,
            ),
            recipientId = feedback.recipients.map {
                SpaceUserResponseDto(
                    userId = it.user?.userId,
                    userName = it.user?.name,
                    userNickname = it.user?.nickname,
                    profileImageUrl = it.user?.profileImageKey?.imageKey,
                    spaceUserId = it.spaceUserId,
                    spaceRole = it.role,
                )
            }.toMutableSet(),
            feedbackType = feedback.type
        )
    }

    @Transactional(readOnly = false)
    fun updateFeedback(authentication: Authentication, spaceId: Long, historyId: Long, feedbackId: Long, feedbackRequestDto: FeedbackRequestDto): FeedbackResponseDto {
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
        feedback.timeline = feedbackRequestDto.feedbackTimeline
        feedback.message = feedbackRequestDto.feedbackMessage

        feedbackRepository.save(feedback)

        return FeedbackResponseDto(
            feedbackId = feedback.feedbackId,
            feedbackTimeline = feedback.timeline,
            feedbackMessage = feedback.message,
            creatorId = SpaceUserResponseDto(
                userId = feedback.creator.user?.userId,
                userName = feedback.creator.user?.name,
                userNickname = feedback.creator.user?.nickname,
                profileImageUrl = feedback.creator.user?.profileImageKey?.imageKey,
                spaceUserId = feedback.creator.spaceUserId,
                spaceRole = feedback.creator.role,
            ),
            recipientId = feedback.recipients.map {
                SpaceUserResponseDto(
                    userId = it.user?.userId,
                    userName = it.user?.name,
                    userNickname = it.user?.nickname,
                    profileImageUrl = it.user?.profileImageKey?.imageKey,
                    spaceUserId = it.spaceUserId,
                    spaceRole = it.role,
                )
            }.toMutableSet(),
            feedbackType = feedback.type
        )
    }

    @Transactional(readOnly = false)
    fun deleteFeedback(authentication: Authentication, spaceId: Long, historyId: Long, feedbackId: Long) {
        val user = userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        spaceService.validateSpaceUser(user, space)

        val history = historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        val feedback = feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw FeedbackNotFoundException()

        history.feedbacks.remove(feedback)

        historyRepository.save(history)
    }

    @Transactional(readOnly = true)
    fun getFeedback(authentication: Authentication, spaceId: Long, historyId: Long, feedbackId: Long): FeedbackResponseDto {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        spaceService.validateSpaceUser(user, space)

        historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException()
        val feedback = feedbackRepository.findFeedbackByFeedbackId(feedbackId) ?: throw FeedbackNotFoundException()

        return FeedbackResponseDto(
            feedbackId = feedback.feedbackId,
            feedbackTimeline = feedback.timeline,
            feedbackMessage = feedback.message,
            creatorId = SpaceUserResponseDto(
                userId = feedback.creator.user?.userId,
                userName = feedback.creator.user?.name,
                userNickname = feedback.creator.user?.nickname,
                profileImageUrl = feedback.creator.user?.profileImageKey?.imageKey,
                spaceUserId = feedback.creator.spaceUserId,
                spaceRole = feedback.creator.role,
            ),
            recipientId = feedback.recipients.map {
                SpaceUserResponseDto(
                    userId = it.user?.userId,
                    userName = it.user?.name,
                    userNickname = it.user?.nickname,
                    profileImageUrl = it.user?.profileImageKey?.imageKey,
                    spaceUserId = it.spaceUserId,
                    spaceRole = it.role,
                )
            }.toMutableSet(),
            feedbackType = feedback.type
        )
    }
}

