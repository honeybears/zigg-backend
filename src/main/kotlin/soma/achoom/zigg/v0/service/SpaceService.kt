package soma.achoom.zigg.v0.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import soma.achoom.zigg.v0.dto.request.SpaceRequestDto
import soma.achoom.zigg.v0.dto.response.SpaceResponseDto
import soma.achoom.zigg.v0.exception.SpaceNotFoundException
import soma.achoom.zigg.v0.exception.UserNotFoundException
import soma.achoom.zigg.v0.model.Space
import soma.achoom.zigg.v0.model.enums.GCSDataType
import soma.achoom.zigg.v0.repository.HistoryRepository
import soma.achoom.zigg.v0.repository.SpaceRepository
import soma.achoom.zigg.v0.repository.SpaceUserRepository
import java.util.UUID


@Service
class SpaceService @Autowired constructor(
    private val spaceRepository: SpaceRepository,
    private val gcsService: GCSService
) : SpaceAsset() {
    @Value("\${space.default.image.url}")
    private lateinit var defaultSpaceImageUrl: String

    fun createSpace(authentication: Authentication, spaceImage:MultipartFile?, spaceRequestDto: SpaceRequestDto): SpaceResponseDto {
        val user = getAuthUser(authentication)

        val inviteUser = spaceRequestDto.spaceUsers.map {
            userRepository.findUserByUserNickname(it.userNickname!!)
                ?: throw UserNotFoundException()
        }

        val space = Space.createSpace(
            spaceImageUrl = defaultSpaceImageUrl,
            spaceName = spaceRequestDto.spaceName,
            users = inviteUser.toMutableSet(),
            admin = user
        )
        spaceImage?.let {
            space.spaceImageUrl = gcsService.uploadFile(GCSDataType.SPACE_IMAGE,spaceImage, space.spaceId)
        }

        spaceRepository.save(space)

        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.spaceName,
            spaceImageUrl = gcsService.generatePreSignedUrl(space.spaceImageUrl),
            spaceUsers = space.spaceUsers
        )
    }

    fun getSpaces(authentication: Authentication): List<SpaceResponseDto> {
        val user = getAuthUser(authentication)
        val spaceList = spaceRepository.findSpaceByUserAndAccepted(user)
        return spaceList.map { SpaceResponseDto.from(it) }
    }

    fun getSpace(authentication: Authentication, spaceId:UUID): SpaceResponseDto{
        val user = getAuthUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)

        return SpaceResponseDto.from(space)
    }

    fun updateSpace(authentication: Authentication, spaceId: UUID, spaceImage:MultipartFile?,spaceRequestDto: SpaceRequestDto): SpaceResponseDto {
        val user = getAuthUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUserRoleIsAdmin(user, space)

        space.isDeleted = true

        spaceRepository.save(space)

        return createSpace(authentication,spaceImage, spaceRequestDto)
    }

    fun deleteSpace(authentication: Authentication, spaceId: UUID) {
        val user = getAuthUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUserRoleIsAdmin(user, space)

        space.isDeleted = true

        spaceRepository.save(space)
    }
}