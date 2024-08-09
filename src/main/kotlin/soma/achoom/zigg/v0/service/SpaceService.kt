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
import soma.achoom.zigg.v0.repository.HistoryRepository
import soma.achoom.zigg.v0.repository.SpaceRepository
import soma.achoom.zigg.v0.repository.SpaceUserRepository


@Service
class SpaceService @Autowired constructor(
    private val spaceRepository: SpaceRepository,
    private val spaceUserRepository: SpaceUserRepository,
    private val historyRepository: HistoryRepository,
    private val gcsService: GCSService
) : SpaceAsset() {
    @Value("\${space.default.image.url}")
    private lateinit var defaultSpaceImageUrl: String

    fun createSpace(authentication: Authentication, spaceImage:MultipartFile?, spaceRequestDto: SpaceRequestDto): SpaceResponseDto {
        val user = getAuthUser(authentication)

        val inviteUser = spaceRequestDto.spaceUsers.map {
            userRepository.findUserByUserNickname(it.userNickname!!)
                ?: throw UserNotFoundException()
        }.toMutableSet()

//        val spaceImage = spaceImage?.let {
//            uploadSpaceImage(it)
//        } ?: defaultSpaceImageUrl
//
//        val space = spaceRequestDto.toSpace(user,inviteUser)

        spaceRepository.save(space)

        return SpaceResponseDto.from(space)
    }

    fun getSpaces(authentication: Authentication): List<SpaceResponseDto> {
        val user = getAuthUser(authentication)
        val spaceList = spaceRepository.findSpaceByUserAndAccepted(user)
        return spaceList.map { SpaceResponseDto.from(it) }
    }

    fun getSpace(authentication: Authentication, spaceId:Long): SpaceResponseDto{
        val user = getAuthUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)

        return SpaceResponseDto.from(space)
    }

    fun updateSpace(authentication: Authentication, spaceId: Long, spaceImage:MultipartFile?,spaceRequestDto: SpaceRequestDto): SpaceResponseDto {
        val user = getAuthUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUserRoleIsAdmin(user, space)

        space.isDeleted = true

        spaceRepository.save(space)

        return createSpace(authentication, spaceRequestDto)
    }

    fun deleteSpace(authentication: Authentication, spaceId: Long) {
        val user = getAuthUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUserRoleIsAdmin(user, space)

        space.isDeleted = true

        spaceRepository.save(space)
    }
}