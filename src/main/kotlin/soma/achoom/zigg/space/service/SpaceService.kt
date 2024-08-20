package soma.achoom.zigg.space.service

import kotlinx.coroutines.coroutineScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

import soma.achoom.zigg.global.infra.gcs.GCSService
import soma.achoom.zigg.global.infra.gcs.GCSDataType
import soma.achoom.zigg.ai.dto.YoutubeUrlRequestDto
import soma.achoom.zigg.ai.service.AIService
import soma.achoom.zigg.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.history.dto.HistoryResponseDto
import soma.achoom.zigg.space.dto.SpaceReferenceUrlRequestDto
import soma.achoom.zigg.space.dto.SpaceRequestDto
import soma.achoom.zigg.space.dto.SpaceResponseDto
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.entity.SpaceRole
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.space.exception.LowSpacePermissionException
import soma.achoom.zigg.space.exception.SpaceNotFoundException
import soma.achoom.zigg.space.exception.SpaceUserNotFoundInSpaceException
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.service.UserService
import java.util.UUID


@Service
class SpaceService @Autowired constructor(
    private val spaceRepository: SpaceRepository,
    private val gcsService: GCSService,
    private val aiService: AIService,
    private val userService: UserService,
    private val spaceUserService: SpaceUserService
)  {
    @Value("\${space.default.image.url}")
    private lateinit var defaultSpaceImageUrl: String

    fun createSpace(
        authentication: Authentication,
        spaceImage: MultipartFile?,
        spaceRequestDto: SpaceRequestDto
    ): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)
        val inviteUser = spaceRequestDto.spaceUsers.map {
            userService.findUserByNickName(it.userNickname!!)
        }

        val space = Space.createSpace(
            spaceImageUrl = defaultSpaceImageUrl,
            spaceName = spaceRequestDto.spaceName,
            users = inviteUser.toMutableSet(),
            admin = user
        )
        spaceImage?.let {
            space.spaceImageKey = gcsService.uploadFile(GCSDataType.SPACE_IMAGE, spaceImage, space.spaceId)
        }

        spaceRepository.save(space)

        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.spaceName,
            spaceImageUrl = gcsService.getPreSignedGetUrl(space.spaceImageKey),
            spaceUsers = space.spaceUsers.map {
                it.toResponseDto()
            }.toMutableSet(),
            createdAt = space.createAt,
            updatedAt = space.updateAt,
            referenceVideoUrl = space.referenceVideoUrl
        )
    }

    fun getSpaces(authentication: Authentication): List<SpaceResponseDto> {
        val user = userService.authenticationToUser(authentication)
        val spaceList = spaceRepository.findSpaceByUserAndAccepted(user)
        return spaceList.map {
            SpaceResponseDto(
                spaceId = it.spaceId,
                spaceName = it.spaceName,
                spaceImageUrl = gcsService.getPreSignedGetUrl(it.spaceImageKey),
                spaceUsers = it.spaceUsers.map {
                    it.toResponseDto()
                }.toMutableSet(),
                createdAt = it.createAt,
                updatedAt = it.updateAt,
                referenceVideoUrl = it.referenceVideoUrl
            )
        }
    }

    fun getSpace(authentication: Authentication, spaceId: UUID): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)

        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.spaceName,
            spaceImageUrl = gcsService.getPreSignedGetUrl(space.spaceImageKey),
            spaceUsers = space.spaceUsers.map {
                it.toResponseDto()
            }.toMutableSet(),
            history = space.histories.map {
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
            }.toMutableSet(),

            createdAt = space.createAt,
            updatedAt = space.updateAt,
            referenceVideoUrl = space.referenceVideoUrl

        )
    }

    fun updateSpace(
        authentication: Authentication,
        spaceId: UUID,
        spaceImage: MultipartFile?,
        spaceRequestDto: SpaceRequestDto
    ): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUserRoleIsAdmin(user, space)

        space.isDeleted = true

        spaceRepository.save(space)

        return createSpace(authentication, spaceImage, spaceRequestDto)
    }

    fun deleteSpace(authentication: Authentication, spaceId: UUID) {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUserRoleIsAdmin(user, space)

        space.isDeleted = true

        spaceRepository.save(space)
    }
    suspend fun addReferenceUrl(authentication: Authentication, spaceId: UUID, spaceReferenceUrlRequestDto: SpaceReferenceUrlRequestDto): SpaceResponseDto
    = coroutineScope {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
        space.referenceVideoUrl = spaceReferenceUrlRequestDto.referenceUrl
        space.referenceVideoKey = GCSDataType.SPACE_REFERENCE_VIDEO.name+space.spaceId+".mp4"
        aiService.putYoutubeVideoToGCS(
            YoutubeUrlRequestDto(
                youtubeUrl =  spaceReferenceUrlRequestDto.referenceUrl,
                bucketName = gcsService.bucketName,
                bucketKey = space.referenceVideoKey!!
            )
        )
        spaceRepository.save(space)

        return@coroutineScope SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.spaceName,
            spaceImageUrl = gcsService.getPreSignedGetUrl(space.spaceImageKey),
            spaceUsers = space.spaceUsers.map {
                it.toResponseDto()
            }.toMutableSet(),
            createdAt = space.createAt,
            updatedAt = space.updateAt,
            referenceVideoUrl = space.referenceVideoUrl
        )
    }
    fun validateSpaceUserRoleIsAdmin(user: User , space:Space):SpaceUser{
        space.spaceUsers.find {
            it.user == user
        }?.let {
            return it
        }
        throw SpaceUserNotFoundInSpaceException()
    }
    fun validateSpaceUser(user:User, space: Space):SpaceUser{
        space.spaceUsers.find {
            it.user == user && it.spaceRole == SpaceRole.ADMIN
        }?.let {
            return it
        }
        throw LowSpacePermissionException()
    }
}