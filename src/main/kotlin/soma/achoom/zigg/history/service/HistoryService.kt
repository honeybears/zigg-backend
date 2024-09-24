package soma.achoom.zigg.history.service

import soma.achoom.zigg.auth.annotation.AuthenticationValidation


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.entity.Video
import soma.achoom.zigg.content.repository.ImageRepository
import soma.achoom.zigg.content.repository.VideoRepository
import soma.achoom.zigg.history.dto.HistoryRequestDto
import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.history.exception.GuestHistoryCreateLimitationException
import soma.achoom.zigg.history.exception.HistoryNotFoundException
import soma.achoom.zigg.history.repository.HistoryRepository
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.exception.SpaceNotFoundException
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.entity.UserRole
import soma.achoom.zigg.user.service.UserService
import java.util.UUID

@Service
class HistoryService @Autowired constructor(
    private val historyRepository: HistoryRepository,
    private val spaceRepository: SpaceRepository,
    private val userService: UserService,
    private val imageRepository: ImageRepository,
    private val videoRepository: VideoRepository
) {

    @AuthenticationValidation
    @Transactional(readOnly = false)
    fun createHistory(
        authentication: Authentication,
        spaceId: UUID,
        historyRequestDto: HistoryRequestDto
    ): History {

        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        val historyVideo = Video(

            videoUploader = user,
            videoDuration = historyRequestDto.videoDuration,
            videoKey = historyRequestDto.historyVideoUrl.split("?")[0].split("/")
                .subList(3, historyRequestDto.historyVideoUrl.split("?")[0].split("/").size).joinToString("/")
            )

        val historyThumbnailImage = Image(
            imageUploader = user,
            imageKey = historyRequestDto.historyThumbnailUrl.split("?")[0].split("/")
                .subList(3, historyRequestDto.historyThumbnailUrl.split("?")[0].split("/").size).joinToString("/")
        )

        videoRepository.save(historyVideo)
        imageRepository.save(historyThumbnailImage)


        val history = History(
            historyId = UUID.fromString(
                historyRequestDto.historyVideoUrl.split("?")[0].split("/").last().split(".")[0]
            ),
            historyVideoKey = historyVideo,
            historyName = historyRequestDto.historyName,
            historyVideoThumbnailUrl = historyThumbnailImage
        )
        space.histories.add(history)
        spaceRepository.save(space)
        return history
    }

    @AuthenticationValidation
    @Transactional(readOnly = true)
    fun getHistories(authentication: Authentication, spaceId: UUID): MutableSet<History> {
        userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val histories = space.histories
        return histories
    }

    @Transactional(readOnly = true)
    fun getHistory(authentication: Authentication, spaceId: UUID, historyId: UUID): History {
        userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        return historyRepository.findHistoryByHistoryId(historyId)
            ?: throw HistoryNotFoundException()
    }

    @Transactional(readOnly = false)
    fun updateHistory(
        authentication: Authentication,
        spaceId: UUID,
        historyId: UUID,
        historyRequestDto: HistoryRequestDto
    ): History {
        userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw HistoryNotFoundException()

        history.historyName = historyRequestDto.historyName
        return historyRepository.save(history)
    }

    @Transactional(readOnly = false)
    fun deleteHistory(authentication: Authentication, spaceId: UUID, historyId: UUID) {
        userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw SpaceNotFoundException()

        space.histories.remove(history)
        spaceRepository.save(space)
    }

    private fun checkGuestHistoryLimit(user: User, space: Space) {
        if (user.role == UserRole.GUEST && space.histories.size == 3) {
            throw GuestHistoryCreateLimitationException()
        }
    }
}