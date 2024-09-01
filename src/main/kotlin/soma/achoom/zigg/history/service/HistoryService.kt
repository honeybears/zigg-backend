package soma.achoom.zigg.history.service

import soma.achoom.zigg.global.annotation.AuthenticationValidate



import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.history.dto.HistoryRequestDto
import soma.achoom.zigg.history.dto.HistoryResponseDto
import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.history.exception.HistoryNotFoundException
import soma.achoom.zigg.history.repository.HistoryRepository
import soma.achoom.zigg.space.dto.SpaceResponseDto
import soma.achoom.zigg.space.exception.SpaceNotFoundException
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.spaceuser.entity.SpaceRole
import soma.achoom.zigg.spaceuser.entity.SpaceUser
import soma.achoom.zigg.user.repository.UserRepository
import soma.achoom.zigg.user.service.UserService
import java.util.UUID

@Service
class HistoryService @Autowired constructor(
    private val historyRepository: HistoryRepository,
    private val spaceRepository: SpaceRepository,
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val responseDtoManager: ResponseDtoManager,
) {

    @AuthenticationValidate
    @Transactional(readOnly = false)
    fun createHistory(
        authentication: Authentication,
        spaceId: UUID,
        historyRequestDto: HistoryRequestDto
    ): HistoryResponseDto  {

        userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = History(
            historyId = UUID.fromString(historyRequestDto.historyVideoUrl.split("?")[0].split("/").last().split(".")[0]),
            historyVideoKey = historyRequestDto.historyVideoUrl.split("?")[0].split("/").subList(3, historyRequestDto.historyVideoUrl.split("?")[0].split("/").size).joinToString("/"),
            historyName = historyRequestDto.historyName,
            space = space,
            videoDuration = historyRequestDto.videoDuration,
            historyVideoThumbnailUrl = historyRequestDto.historyThumbnailUrl.split("?")[0].split("/").subList(3, historyRequestDto.historyThumbnailUrl.split("?")[0].split("/").size).joinToString("/")
        )

        historyRepository.save(history)
        return responseDtoManager.generateHistoryResponseShortDto(history)
    }
    @AuthenticationValidate
    @Transactional(readOnly = true)
    fun getHistories(authentication: Authentication, spaceId: UUID): List<HistoryResponseDto> {
        userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val histories = historyRepository.findHistoriesBySpace(space)
        return histories.filter { !it.isDeleted }.map {
            responseDtoManager.generateHistoryResponseShortDto(it)
        }.toList()
    }
    @Transactional(readOnly = true)
    fun getHistory(authentication: Authentication, spaceId: UUID, historyId: UUID): HistoryResponseDto {
        userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw SpaceNotFoundException()
        return responseDtoManager.generateHistoryResponseDto(history)
    }
    @Transactional(readOnly = false)
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
            ?: throw HistoryNotFoundException()

        history.historyName = historyRequestDto.historyName
        historyRepository.save(history)
        return responseDtoManager.generateHistoryResponseDto(history)
    }
    @Transactional(readOnly = false)
    fun inviteUserInSpace(authentication: Authentication, spaceId: UUID, userId: List<UUID>): SpaceResponseDto {
        userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        val user = userRepository.findAllById(userId)

        user.forEach {
            val spaceUser = SpaceUser(
                space = space,
                user = it,
                spaceRole = SpaceRole.USER,
            )
            space.spaceUsers.add(spaceUser)
        }
        spaceRepository.save(space)
        return responseDtoManager.generateSpaceResponseShortDto(space)
    }
    @Transactional(readOnly = false)
    fun deleteHistory(authentication: Authentication, spaceId: UUID, historyId: UUID) {
        userService.authenticationToUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()
        val history = historyRepository.findHistoryByHistoryId(historyId)
            ?: throw SpaceNotFoundException()

        historyRepository.delete(history)

    }

}