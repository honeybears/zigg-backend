package soma.achoom.zigg.history.service

import soma.achoom.zigg.auth.annotation.AuthenticationValidation


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.entity.Video
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.history.dto.HistoryRequestDto
import soma.achoom.zigg.history.dto.HistoryResponseDto
import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.history.exception.GuestHistoryCreateLimitationException
import soma.achoom.zigg.history.exception.HistoryNotFoundException
import soma.achoom.zigg.history.repository.HistoryRepository
import soma.achoom.zigg.s3.service.S3Service
import soma.achoom.zigg.space.dto.SpaceUserResponseDto
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.exception.SpaceNotFoundException
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.entity.UserRole
import soma.achoom.zigg.user.service.UserService

@Service
class HistoryService @Autowired constructor(
    private val historyRepository: HistoryRepository,
    private val spaceRepository: SpaceRepository,
    private val userService: UserService,
    private val s3Service: S3Service,
) {

    @AuthenticationValidation
    @Transactional(readOnly = false)
    fun createHistory(authentication: Authentication, spaceId: Long, historyRequestDto: HistoryRequestDto): HistoryResponseDto {

        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        val historyVideo = Video.fromUrl(
            uploader = user,
            duration = historyRequestDto.videoDuration,
            videoUrl = historyRequestDto.historyVideoUrl
        )

        val historyThumbnailImage = Image.fromUrl(
            uploader = user,
            imageUrl = historyRequestDto.historyThumbnailUrl
        )

        val history = History(
            videoKey = historyVideo,
            name = historyRequestDto.historyName,
            videoThumbnailUrl = historyThumbnailImage,
        )
        space.addHistory(history)
        spaceRepository.save(space)
        return HistoryResponseDto(
            historyId = history.historyId,
            historyName = history.name,
            historyVideoPreSignedUrl = s3Service.getPreSignedGetUrl(history.videoKey.videoKey),
            historyVideoThumbnailPreSignedUrl = s3Service.getPreSignedGetUrl(history.videoThumbnailUrl.imageKey),
            createdAt = history.createAt,
            feedbacks = null,
            videoDuration = history.videoKey.duration,
            feedbackCount = history.feedbacks.size
        )
    }

    @AuthenticationValidation
    @Transactional(readOnly = true)
    fun getHistories(authentication: Authentication, spaceId: Long): List<HistoryResponseDto> {
        userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val histories = space.histories
        return histories.map { history ->
            HistoryResponseDto(
                historyId = history.historyId,
                historyName = history.name,
                historyVideoPreSignedUrl = s3Service.getPreSignedGetUrl(history.videoKey.videoKey),
                historyVideoThumbnailPreSignedUrl = s3Service.getPreSignedGetUrl(history.videoThumbnailUrl.imageKey),
                createdAt = history.createAt,
                feedbacks = null,
                videoDuration = history.videoKey.duration,
                feedbackCount = history.feedbacks.size
            )
        }.toList()
    }

    @Transactional(readOnly = true)
    fun getHistory(authentication: Authentication, spaceId: Long, historyId: Long): HistoryResponseDto {
        userService.authenticationToUser(authentication)
        spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history =  historyRepository.findHistoryByHistoryId(historyId)
            ?: throw HistoryNotFoundException()
        return HistoryResponseDto(
            historyId = history.historyId,
            historyName = history.name,
            historyVideoPreSignedUrl = s3Service.getPreSignedGetUrl(history.videoKey.videoKey),
            historyVideoThumbnailPreSignedUrl = s3Service.getPreSignedGetUrl(history.videoThumbnailUrl.imageKey),
            createdAt = history.createAt,
            feedbacks = history.feedbacks.map { feedback ->
                FeedbackResponseDto(
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
            }.toMutableSet(),
            videoDuration = history.videoKey.duration,
            feedbackCount = history.feedbacks.size,
        )
    }

    @Transactional(readOnly = false)
    fun updateHistory(authentication: Authentication, spaceId: Long, historyId: Long, historyRequestDto: HistoryRequestDto): HistoryResponseDto {
        userService.authenticationToUser(authentication)
        spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw HistoryNotFoundException()

        history.name = historyRequestDto.historyName
        return HistoryResponseDto(
            historyId = history.historyId,
            historyName = history.name,
            historyVideoPreSignedUrl = s3Service.getPreSignedGetUrl(history.videoKey.videoKey),
            historyVideoThumbnailPreSignedUrl = s3Service.getPreSignedGetUrl(history.videoThumbnailUrl.imageKey),
            createdAt = history.createAt,
            feedbacks = null,
            videoDuration = history.videoKey.duration,
            feedbackCount = history.feedbacks.size,
        )
    }

    @Transactional(readOnly = false)
    fun deleteHistory(authentication: Authentication, spaceId: Long, historyId: Long) {
        userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw SpaceNotFoundException()

        space.histories.remove(history)
        spaceRepository.save(space)
    }

}