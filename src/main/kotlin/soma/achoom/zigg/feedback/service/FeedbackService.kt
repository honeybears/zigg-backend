package soma.achoom.zigg.feedback.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.ai.dto.GenerateAiFeedbackRequestDto
import soma.achoom.zigg.global.infra.gcs.GCSService
import soma.achoom.zigg.ai.GeminiModelType
import soma.achoom.zigg.ai.dto.GenerateAIFeedbackResponseDto
import soma.achoom.zigg.ai.service.AIService
import soma.achoom.zigg.feedback.dto.FeedbackRequestDto
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.feedback.dto.FeedbackType
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.feedback.exception.FeedbackNotFoundException
import soma.achoom.zigg.feedback.repository.FeedbackRepository
import soma.achoom.zigg.history.exception.HistoryNotFoundException
import soma.achoom.zigg.history.repository.HistoryRepository
import soma.achoom.zigg.space.exception.SpaceNotFoundException
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.spaceuser.repository.SpaceUserRepository
import soma.achoom.zigg.space.service.SpaceService
import soma.achoom.zigg.spaceuser.exception.SpaceUserNotFoundInSpaceException
import soma.achoom.zigg.user.service.UserService

import java.util.UUID
import java.util.concurrent.CompletableFuture

@Service
class FeedbackService @Autowired constructor(
    private val historyRepository: HistoryRepository,
    private val feedbackRepository: FeedbackRepository,
    private val spaceUserRepository: SpaceUserRepository,
    private val spaceRepository: SpaceRepository,
    private val aiService: AIService,
    private val gcsService: GCSService,
    private val spaceService: SpaceService,
    private val userService: UserService
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
            FeedbackResponseDto.from(it)
        }
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
        return FeedbackResponseDto.from(feedback)
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
        feedback.isDeleted = true
        feedbackRepository.save(feedback)

        val newFeedbackResponseDto = createFeedback(authentication, spaceId, historyId, feedbackRequestDto)

        return newFeedbackResponseDto
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
        return FeedbackResponseDto.from(feedback)
    }

    fun generateAIFeedbackResponse(historyId: UUID, generateAIFeedbackResponseDto: GenerateAIFeedbackResponseDto){

        val aiFeedbackToFeedbackList = mutableListOf<Feedback>()
        for(feedbacks in generateAIFeedbackResponseDto.timelineFeedbacks){
            val feedback = Feedback(
                feedbackId = UUID.randomUUID(),
                feedbackType = FeedbackType.AI,
                feedbackTimeline = feedbacks.time,
                history = historyRepository.findHistoryByHistoryId(historyId) ?: throw HistoryNotFoundException(),
                feedbackMessage = feedbacks.message,
                feedbackCreator = null
            )
            aiFeedbackToFeedbackList.add(feedback)
            feedbackRepository.save(feedback)
        }
    }

    suspend fun generateAIFeedbackRequest(
        authentication: Authentication, spaceId: UUID, historyId: UUID
    ) : CompletableFuture<Void> = withContext(Dispatchers.IO) {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw SpaceNotFoundException()
        space.referenceVideoKey ?: throw Exception("Reference video is not exist")

        val aiFeedback = aiService.generateAIFeedbackRequest(
            GenerateAiFeedbackRequestDto(
                modelName = GeminiModelType.FLASH,
                referenceVideoKey = space.referenceVideoKey!!,
                comparisonVideoKey = history.historyVideoKey,
                bucketName = gcsService.bucketName,
                historyId = historyId,
                token = user.jwtToken
            )

        )
        return@withContext CompletableFuture.completedFuture(null)
    }
}

