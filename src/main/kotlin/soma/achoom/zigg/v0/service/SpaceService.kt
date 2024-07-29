package soma.achoom.zigg.v0.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import soma.achoom.zigg.v0.dto.request.SpaceRequestDto
import soma.achoom.zigg.v0.dto.response.SpaceListResponse
import soma.achoom.zigg.v0.dto.response.SpaceResponseDto
import soma.achoom.zigg.v0.model.Space
import soma.achoom.zigg.v0.model.SpaceTag
import soma.achoom.zigg.v0.model.SpaceUser
import soma.achoom.zigg.v0.model.enums.S3Option
import soma.achoom.zigg.v0.model.enums.SpaceUserStatus
import soma.achoom.zigg.v0.repository.SpaceRepository
import soma.achoom.zigg.v0.repository.SpaceUserRepository


@Service
class SpaceService @Autowired constructor(
    private val spaceRepository: SpaceRepository,
    private val s3Service: S3Service,
    private val spaceUserRepository: SpaceUserRepository
): BaseService() {
    @Value("\${space.default.image.url}")
    private lateinit var defaultSpaceImageUrl: String

    fun createSpace(authentication:Authentication, spaceRequestDto: SpaceRequestDto,spaceImage:MultipartFile?): SpaceResponseDto {
        val user = getAuthUser(authentication)

        val spaceImageUrl = spaceImage?.let {
            s3Service.uploadFile(it, S3Option.SPACE_IMAGE)
        }?: defaultSpaceImageUrl

        val space = Space(
            spaceName = spaceRequestDto.spaceName,
            spaceImageUrl = spaceImageUrl,
            spaceId = null,
            comparisonVideoUrl = null,
            spaceThumbnailUrl = null,
        )

        val invitedUsers = spaceRequestDto.spaceUsers?.map {
            val invitedUser = it.userNickname?.let {userRepository.findUserByUserNickname(it)}
                ?: throw IllegalArgumentException("user not found")
            val spaceUser = SpaceUser(
                user = invitedUser,
                inviteStatus = SpaceUserStatus.WAITING,
                space = space,
                spaceRole = it.spaceRole,
                spaceUserId = null
            )
            spaceUser
        }?.toMutableSet()

        invitedUsers?.let { space.spaceUsers.addAll(it) }

        spaceRequestDto.tags?.map {
            val tag = SpaceTag(
                space = space,
                tag = it,
                spaceTagId = null
            )
            space.tags.add(tag)
        }
        spaceRepository.save(space)

        return SpaceResponseDto.from(space, generatedPreSignedUrl = s3Service.generatePreSignedUrl(spaceImageUrl, 10))
    }

    fun getSpaces(authentication: Authentication): SpaceListResponse? {
        val user = getAuthUser(authentication)
        val spaceUsers = spaceUserRepository.findSpaceUsersByUser(user)
        val spaces = mutableSetOf<SpaceResponseDto>()
        spaceUsers.forEach {
            spaces.add(SpaceResponseDto.from(it.space,
                generatedPreSignedUrl = s3Service.generatePreSignedUrl(it.space.spaceImageUrl!!, 10)))
        }
        return SpaceListResponse(spaces)
    }

    fun getSpace(authentication: Authentication, spaceId: Long): SpaceResponseDto? {
        val user = getAuthUser(authentication)
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: return null
        return SpaceResponseDto.from(space, generatedPreSignedUrl = s3Service.generatePreSignedUrl(space.spaceImageUrl!!, 10))
    }

    fun updateSpace(authentication: Authentication, spaceId: Long, spaceRequestDto: SpaceRequestDto): SpaceResponseDto? {
        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: return null
        spaceRequestDto.spaceImage?.let {
            space.spaceImageUrl = s3Service.uploadFile(it,S3Option.SPACE_IMAGE)
        }
        spaceRequestDto.spaceName?.let { space.spaceName = it }
        spaceRepository.save(space)
        return SpaceResponseDto.from(space, generatedPreSignedUrl = s3Service.generatePreSignedUrl(space.spaceImageUrl!!, 10))
    }
}