package soma.achoom.zigg.v0.history.service

import soma.achoom.zigg.global.annotation.AuthenticationValidate



import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.v0.space.exception.SpaceNotFoundException
import soma.achoom.zigg.global.util.BaseService
import soma.achoom.zigg.global.infra.gcs.GCSService
import soma.achoom.zigg.v0.ai.GeminiModelType
import soma.achoom.zigg.v0.ai.dto.GenerateAIFeedbackResponseDto
import soma.achoom.zigg.v0.ai.dto.GenerateAiFeedbackRequestDto
import soma.achoom.zigg.v0.ai.dto.GenerateThumbnailRequestDto
import soma.achoom.zigg.v0.ai.service.AIService
import soma.achoom.zigg.v0.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.v0.feedback.dto.FeedbackType
import soma.achoom.zigg.v0.feedback.entity.Feedback
import soma.achoom.zigg.v0.feedback.repository.FeedbackRepository
import soma.achoom.zigg.v0.history.dto.HistoryRequestDto
import soma.achoom.zigg.v0.history.dto.HistoryResponseDto
import soma.achoom.zigg.v0.history.entity.History
import soma.achoom.zigg.v0.history.exception.HistoryNotFoundException
import soma.achoom.zigg.v0.history.repository.HistoryRepository
import soma.achoom.zigg.v0.space.repository.SpaceRepository

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class HistoryService @Autowired constructor(
    private val historyRepository: HistoryRepository,
    private val spaceRepository: SpaceRepository,
    private val gcsService: GCSService,
    private val aiService: AIService,
    @Value("\${gcp.bucket.name}")
    private val bucketName: String,
    private val feedbackRepository: FeedbackRepository,
) : BaseService() {

    @OptIn(ExperimentalCoroutinesApi::class)
    @AuthenticationValidate
    suspend fun createHistory(
        authentication: Authentication,
        spaceId: UUID,
        historyRequestDto: HistoryRequestDto
    ): HistoryResponseDto = coroutineScope {

        getAuthUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()


        val bucketKey = gcsService.convertPreSignedUrlToGeneralKey(historyRequestDto.historyVideoUrl)


        val response = async{
            return@async aiService.createThumbnailRequest(
                GenerateThumbnailRequestDto(
                    bucketName = bucketName,
                    historyVideoKey = bucketKey
                )
            )
        }

        val uuid = getLastPathSegment(bucketKey).split(".")[0]

        response.await()
        val history = History(
            historyId = UUID.fromString(uuid),
            historyVideoKey = bucketKey,
            historyName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")),
            space = space,
            historyVideoThumbnailUrl = response.getCompleted().historyThumbnailKey
        )

        historyRepository.save(history)

        return@coroutineScope HistoryResponseDto(
            historyId = history.historyId,
            historyName = history.historyName,
            historyVideoPreSignedUrl = gcsService.getPreSignedGetUrl(history.historyVideoKey),
            feedbacks = history.feedbacks.map { feedback ->
                FeedbackResponseDto.from(feedback)
            }.toMutableSet(),
            historyVideoThumbnailPreSignedUrl = gcsService.getPreSignedGetUrl(history.historyVideoThumbnailUrl!!),
            createdAt = history.createAt
        )
    }

    fun getHistories(authentication: Authentication, spaceId: UUID): List<HistoryResponseDto> {
        getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val histories = historyRepository.findHistoriesBySpace(space)
        return histories.filter { !it.isDeleted }.map {
            HistoryResponseDto(
                historyId = it.historyId,
                historyName = it.historyName,
                historyVideoPreSignedUrl = gcsService.getPreSignedGetUrl(it.historyVideoKey),
                feedbacks = it.feedbacks.map { feedback ->
                    FeedbackResponseDto.from(feedback)
                }.toMutableSet(),
                historyVideoThumbnailPreSignedUrl = gcsService.getPreSignedGetUrl(it.historyVideoThumbnailUrl!!),
                createdAt = it.createAt

            )
        }
    }

    fun getHistory(authentication: Authentication, spaceId: UUID, historyId: UUID): HistoryResponseDto {
        getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw SpaceNotFoundException()
        return HistoryResponseDto(
            historyId = history.historyId,
            historyName = history.historyName,
            historyVideoPreSignedUrl = gcsService.getPreSignedGetUrl(history.historyVideoKey),
            feedbacks = history.feedbacks.map { feedback ->
                FeedbackResponseDto.from(feedback)
            }.toMutableSet(),
            historyVideoThumbnailPreSignedUrl = gcsService.getPreSignedGetUrl(history.historyVideoThumbnailUrl!!),
            createdAt = history.createAt

        )
    }

    suspend fun updateHistory(
        authentication: Authentication,
        spaceId: UUID,
        historyId: UUID,
        historyRequestDto: HistoryRequestDto
    ): HistoryResponseDto  = coroutineScope{
        getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw SpaceNotFoundException()
        history.isDeleted = true
        historyRepository.save(history)
        val newHistory = createHistory(authentication, spaceId, historyRequestDto)
        return@coroutineScope newHistory
    }

    fun deleteHistory(authentication: Authentication, spaceId: UUID, historyId: UUID) {
        getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw SpaceNotFoundException()
        history.isDeleted = true
        historyRepository.save(history)
    }
    private fun getLastPathSegment(path: String):String{
        return path.split("/").last()
    }
    suspend fun generateAIFeedbackRequest(
        authentication: Authentication, spaceId: UUID, historyId: UUID, feedbackId: UUID
    ) = coroutineScope {
        val user = getAuthUser(authentication)

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
                bucketName = bucketName,
                historyId = historyId
            )

        )
        return@coroutineScope
    }
    fun generateAIFeedbackResponse(generateAIFeedbackResponseDto: GenerateAIFeedbackResponseDto){

        val aiFeedbackToFeedbackList = mutableListOf<Feedback>()
        for(feedbacks in generateAIFeedbackResponseDto.timelineFeedback){
            val feedback = Feedback(
                feedbackId = UUID.randomUUID(),
                feedbackType = FeedbackType.AI,
                feedbackTimeline = feedbacks.time,
                history = historyRepository.findHistoryByHistoryId(generateAIFeedbackResponseDto.historyId) ?: throw HistoryNotFoundException(),
                feedbackMessage = feedbacks.message,
                feedbackCreator = null
            )
            aiFeedbackToFeedbackList.add(feedback)
            feedbackRepository.save(feedback)
        }
    }
}