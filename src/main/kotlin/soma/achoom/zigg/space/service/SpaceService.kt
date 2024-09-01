package soma.achoom.zigg.space.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.firebase.dto.FCMEvent
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.space.dto.SpaceReferenceUrlRequestDto
import soma.achoom.zigg.space.dto.SpaceRequestDto
import soma.achoom.zigg.space.dto.SpaceResponseDto
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.exception.SpaceNotFoundException
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.spaceuser.entity.SpaceRole
import soma.achoom.zigg.spaceuser.entity.SpaceUser
import soma.achoom.zigg.spaceuser.exception.LowSpacePermissionException
import soma.achoom.zigg.spaceuser.exception.SpaceUserNotFoundInSpaceException
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.service.UserService
import java.util.UUID


@Service
class SpaceService(
    private val spaceRepository: SpaceRepository,
    private val userService: UserService,
    private val responseDtoManager: ResponseDtoManager,
    private val applicationEventPublisher: ApplicationEventPublisher

) {
    @Value("\${space.default.image.url}")
    private lateinit var defaultSpaceImageUrl: String

    @Transactional(readOnly = false)
    fun createSpace(
        authentication: Authentication,
        spaceRequestDto: SpaceRequestDto
    ): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)
        val inviteUser = spaceRequestDto.spaceUsers.map {
            userService.findUserByNickName(it.userNickname!!)
        }.toMutableSet()
        val space = Space.create(
            spaceImageUrl = spaceRequestDto.spaceImageUrl?.let {
                it.split("?")[0].split("/").subList(3, spaceRequestDto.spaceImageUrl.split("?")[0].split("/").size)
                    .joinToString("/")
            } ?: defaultSpaceImageUrl,
            spaceName = spaceRequestDto.spaceName,
            users = inviteUser.toMutableSet(),
            admin = user
        )

        spaceRepository.save(space)
        applicationEventPublisher.publishEvent(
            FCMEvent(
                users = inviteUser,
                title = "새로운 공간이 생성되었습니다.",
                body = spaceRequestDto.spaceName,
                data = mapOf("spaceId" to space.spaceId.toString()),
                android = null,
                apns = null
            )
        )
        return responseDtoManager.generateSpaceResponseShortDto(space)
    }

    @Transactional(readOnly = true)
    fun getSpaces(authentication: Authentication): List<SpaceResponseDto> {
        val user = userService.authenticationToUser(authentication)
        val spaceList = spaceRepository.findSpaceByUserAndAccepted(user)
        return spaceList.map {
            responseDtoManager.generateSpaceResponseShortDto(it)
        }
    }

    @Transactional(readOnly = true)
    fun getSpace(authentication: Authentication, spaceId: UUID): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
        return responseDtoManager.generateSpaceResponseShortDto(space)
    }

    @Transactional(readOnly = false)
    fun updateSpace(
        authentication: Authentication,
        spaceId: UUID,
        spaceRequestDto: SpaceRequestDto
    ): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUserRoleIsAdmin(user, space)


        spaceRepository.save(space)
        space.spaceName = spaceRequestDto.spaceName
        spaceRequestDto.spaceImageUrl?.let {
            space.spaceImageKey = it
        }
        return responseDtoManager.generateSpaceResponseShortDto(space)
    }

    @Transactional(readOnly = false)
    fun deleteSpace(authentication: Authentication, spaceId: UUID) {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUserRoleIsAdmin(user, space)

        spaceRepository.delete(space)
    }

    @Transactional(readOnly = false)
    fun addReferenceUrl(
        authentication: Authentication,
        spaceId: UUID,
        spaceReferenceUrlRequestDto: SpaceReferenceUrlRequestDto
    ): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
        space.referenceVideoUrl = spaceReferenceUrlRequestDto.referenceUrl

        spaceRepository.save(space)

        return responseDtoManager.generateSpaceResponseShortDto(space)
    }

    @Transactional(readOnly = false)
    fun validateSpaceUserRoleIsAdmin(user: User, space: Space): SpaceUser {
        space.spaceUsers.find {
            it.user == user && it.spaceRole == SpaceRole.ADMIN
        }?.let {
            return it
        }
        throw LowSpacePermissionException()
    }

    @Transactional(readOnly = false)
    fun validateSpaceUser(user: User, space: Space): SpaceUser {
        space.spaceUsers.find {
            it.user == user
        }?.let {
            return it
        }
        throw SpaceUserNotFoundInSpaceException()
    }
}