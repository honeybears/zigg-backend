package soma.achoom.zigg.history.service

import soma.achoom.zigg.global.annotation.AuthenticationValidate



import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.global.infra.gcs.GCSService
import soma.achoom.zigg.ai.dto.GenerateThumbnailRequestDto
import soma.achoom.zigg.ai.service.AIService
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.history.dto.HistoryRequestDto
import soma.achoom.zigg.history.dto.HistoryResponseDto
import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.history.repository.HistoryRepository
import soma.achoom.zigg.space.exception.SpaceNotFoundException
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.user.service.UserService
import java.util.UUID

@Service
class HistoryService @Autowired constructor(
    private val historyRepository: HistoryRepository,
    private val spaceRepository: SpaceRepository,
    private val gcsService: GCSService,
    private val aiService: AIService,
    private val userService: UserService,
) {

    @AuthenticationValidate
    fun createHistory(
        authentication: Authentication,
        spaceId: UUID,
        historyRequestDto: HistoryRequestDto
    ): HistoryResponseDto  {

        userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()


        val bucketKey = gcsService.convertPreSignedUrlToGeneralKey(historyRequestDto.historyVideoUrl)


        val response = runBlocking {
            aiService.createThumbnailRequest(
                GenerateThumbnailRequestDto(
                    bucketName = gcsService.bucketName,
                    historyVideoKey = bucketKey
                )
            )
        }
        val uuid = bucketKey.split("/").last().split(".")[0]

//        val uuid = getLastPathSegment(bucketKey).split(".")[0]

        val history = History(
            historyId = UUID.fromString(uuid),
            historyVideoKey = bucketKey,
            historyName = historyRequestDto.historyName,
            space = space,
            videoDuration = historyRequestDto.videoDuration,
            historyVideoThumbnailUrl = response.historyThumbnailKey
        )

        historyRepository.save(history)

        return HistoryResponseDto(
            historyId = history.historyId,
            historyName = history.historyName,
            historyVideoPreSignedUrl = gcsService.getPreSignedGetUrl(history.historyVideoKey),
            feedbacks = history.feedbacks.map { feedback ->
                FeedbackResponseDto.from(feedback)
            }.toMutableSet(),
            historyVideoThumbnailPreSignedUrl = gcsService.getPreSignedGetUrl(history.historyVideoThumbnailUrl!!),
            createdAt = history.createAt,
            videoDuration = history.videoDuration
        )
    }

    fun getHistories(authentication: Authentication, spaceId: UUID): List<HistoryResponseDto> {
        userService.authenticationToUser(authentication)
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
                createdAt = it.createAt,
                videoDuration = it.videoDuration
            )
        }
    }

    fun getHistory(authentication: Authentication, spaceId: UUID, historyId: UUID): HistoryResponseDto {
        userService.authenticationToUser(authentication)
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
            createdAt = history.createAt,
            videoDuration = history.videoDuration
        )
    }

    fun updateHistory(
        authentication: Authentication,
        spaceId: UUID,
        historyId: UUID,
        historyRequestDto: HistoryRequestDto
    ): HistoryResponseDto  {
        userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw SpaceNotFoundException()
        history.isDeleted = true
        historyRepository.save(history)
        val newHistory = createHistory(authentication, spaceId, historyRequestDto)
        return newHistory
    }

    fun deleteHistory(authentication: Authentication, spaceId: UUID, historyId: UUID) {
        userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw SpaceNotFoundException()
        history.isDeleted = true
            historyRepository.save(history)

    }

}