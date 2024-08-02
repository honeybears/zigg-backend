package soma.achoom.zigg.v0.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import soma.achoom.zigg.v0.dto.request.SpaceRequestDto
import soma.achoom.zigg.v0.dto.response.SpaceListResponse
import soma.achoom.zigg.v0.dto.response.SpaceResponseDto
import soma.achoom.zigg.v0.model.History
import soma.achoom.zigg.v0.model.Space
import soma.achoom.zigg.v0.model.SpaceUser
import soma.achoom.zigg.v0.model.enums.S3Option
import soma.achoom.zigg.v0.model.enums.SpaceRole
import soma.achoom.zigg.v0.model.enums.SpaceUserStatus
import soma.achoom.zigg.v0.repository.HistoryRepository
import soma.achoom.zigg.v0.repository.SpaceRepository
import soma.achoom.zigg.v0.repository.SpaceUserRepository


@Service
class SpaceService @Autowired constructor(
    private val spaceRepository: SpaceRepository,
    private val s3Service: S3Service,
    private val spaceUserRepository: SpaceUserRepository,
    private val historyRepository: HistoryRepository
) : SpaceAsset() {
    @Value("\${space.default.image.url}")
    private lateinit var defaultSpaceImageUrl: String

    fun createSpace(
        authentication: Authentication,
        spaceRequestDto: SpaceRequestDto,
    ): SpaceResponseDto {
        val user = getAuthUser(authentication)
        val inviteUser = spaceRequestDto.spaceUsers.map {
            userRepository.findUserByUserNickname(it.userNickname!!)
                ?: throw IllegalArgumentException("user not found")
        }.toMutableSet()

        val space = Space(
            spaceName = spaceRequestDto.spaceName,
            spaceId = null,
        )
        space.spaceUsers.add(
            SpaceUser(
                space = space,
                user = user,
                spaceRole = SpaceRole.ADMIN,
                inviteStatus = SpaceUserStatus.ACCEPTED,
                spaceUserId = null
            )
        )
        inviteUser.forEach { invitedUser ->
            val spaceUser = SpaceUser(
                space = space,
                user = invitedUser,
                spaceRole = SpaceRole.USER, // Use correct role
                inviteStatus = SpaceUserStatus.ACCEPTED,
                spaceUserId = null
            )
            space.spaceUsers.add(spaceUser)
        }

        spaceRepository.save(space)
        return SpaceResponseDto.from(space)
    }
}