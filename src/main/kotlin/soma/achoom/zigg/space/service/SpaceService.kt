package soma.achoom.zigg.space.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.global.ResponseDtoGenerator
import soma.achoom.zigg.history.dto.HistoryResponseDto
import soma.achoom.zigg.s3.service.S3Service
import soma.achoom.zigg.space.dto.SpaceReferenceUrlRequestDto
import soma.achoom.zigg.space.dto.SpaceRequestDto
import soma.achoom.zigg.space.dto.SpaceResponseDto
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.exception.SpaceNotFoundException
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.spaceuser.dto.SpaceUserResponseDto
import soma.achoom.zigg.spaceuser.entity.SpaceRole
import soma.achoom.zigg.spaceuser.entity.SpaceUser
import soma.achoom.zigg.spaceuser.exception.LowSpacePermissionException
import soma.achoom.zigg.spaceuser.exception.SpaceUserNotFoundInSpaceException
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.service.UserService
import java.util.UUID


@Service
class SpaceService @Autowired constructor(
    private val spaceRepository: SpaceRepository,
    private val userService: UserService,
    private val s3Service: S3Service,
    private val responseDtoGenerator: ResponseDtoGenerator
)  {
    @Value("\${space.default.image.url}")
    private lateinit var defaultSpaceImageUrl: String

    fun createSpace(
        authentication: Authentication,
        spaceRequestDto: SpaceRequestDto
    ): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)
        val inviteUser = spaceRequestDto.spaceUsers.map {
            userService.findUserByNickName(it.userNickname!!)
        }

        val space = Space.create(
            spaceImageUrl = spaceRequestDto.spaceImageUrl ?: defaultSpaceImageUrl,
            spaceName = spaceRequestDto.spaceName,
            users = inviteUser.toMutableSet(),

            admin = user
        )

        spaceRepository.save(space)
        return responseDtoGenerator.generateSpaceResponseShortDto(space)
    }

    fun getSpaces(authentication: Authentication): List<SpaceResponseDto> {
        val user = userService.authenticationToUser(authentication)
        val spaceList = spaceRepository.findSpaceByUserAndAccepted(user)
        return spaceList.map {
            responseDtoGenerator.generateSpaceResponseShortDto(it)
        }
    }

    fun getSpace(authentication: Authentication, spaceId: UUID): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
        return responseDtoGenerator.generateSpaceResponseShortDto(space)
    }

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
        return responseDtoGenerator.generateSpaceResponseShortDto(space)
    }

    fun deleteSpace(authentication: Authentication, spaceId: UUID) {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUserRoleIsAdmin(user, space)

        spaceRepository.delete(space)
    }
    fun addReferenceUrl(authentication: Authentication, spaceId: UUID, spaceReferenceUrlRequestDto: SpaceReferenceUrlRequestDto): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
        space.referenceVideoUrl = spaceReferenceUrlRequestDto.referenceUrl

        spaceRepository.save(space)

        return responseDtoGenerator.generateSpaceResponseShortDto(space)
    }
    fun validateSpaceUserRoleIsAdmin(user: User , space:Space): SpaceUser {
        space.spaceUsers.find {
            it.user == user && it.spaceRole == SpaceRole.ADMIN
        }?.let {
            return it
        }
        throw LowSpacePermissionException()
    }
    fun validateSpaceUser(user:User, space: Space): SpaceUser {
        space.spaceUsers.find {
            it.user == user
        }?.let {
            return it
        }
        throw SpaceUserNotFoundInSpaceException()
    }
}