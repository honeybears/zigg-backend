package soma.achoom.zigg.v0.space.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import soma.achoom.zigg.v0.space.exception.SpaceNotFoundException
import soma.achoom.zigg.v0.user.exception.UserNotFoundException
import soma.achoom.zigg.global.infra.GCSService
import soma.achoom.zigg.global.util.SpaceAsset
import soma.achoom.zigg.global.infra.GCSDataType
import soma.achoom.zigg.v0.feedback.dto.FeedbackResponseDto
import soma.achoom.zigg.v0.history.dto.HistoryResponseDto
import soma.achoom.zigg.v0.space.dto.SpaceRequestDto
import soma.achoom.zigg.v0.space.dto.SpaceResponseDto
import soma.achoom.zigg.v0.space.entity.Space
import soma.achoom.zigg.v0.space.repository.SpaceRepository
import java.util.UUID


@Service
class SpaceService @Autowired constructor(
    private val spaceRepository: SpaceRepository,
    private val gcsService: GCSService
) : SpaceAsset() {
    @Value("\${space.default.image.url}")
    private lateinit var defaultSpaceImageUrl: String

    fun createSpace(
        authentication: Authentication,
        spaceImage: MultipartFile?,
        spaceRequestDto: SpaceRequestDto
    ): SpaceResponseDto {
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
            space.spaceImageKey = gcsService.uploadFile(GCSDataType.SPACE_IMAGE, spaceImage, space.spaceId)
        }

        spaceRepository.save(space)

        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.spaceName,
            spaceImageUrl = gcsService.generatePreSignedUrl(space.spaceImageKey),
            spaceUsers = space.spaceUsers.map {
                it.user.userName!!
            }.toMutableSet(),
        )
    }

    fun getSpaces(authentication: Authentication): List<SpaceResponseDto> {
        val user = getAuthUser(authentication)
        val spaceList = spaceRepository.findSpaceByUserAndAccepted(user)
        return spaceList.map {
            SpaceResponseDto(
                spaceId = it.spaceId,
                spaceName = it.spaceName,
                spaceImageUrl = gcsService.generatePreSignedUrl(it.spaceImageKey),
                spaceUsers = it.spaceUsers.map {
                    it.user.userName!!
                }.toMutableSet(),
            )
        }
    }

    fun getSpace(authentication: Authentication, spaceId: UUID): SpaceResponseDto {
        val user = getAuthUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)

        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.spaceName,
            spaceImageUrl = gcsService.generatePreSignedUrl(space.spaceImageKey),
            spaceUsers = space.spaceUsers.map {
                it.user.userName!!
            }.toMutableSet(),
            history = space.histories.map {
                HistoryResponseDto(
                    historyId = it.historyId,
                    historyName = it.historyName,
                    historyVideoPreSignedUrl = gcsService.generatePreSignedUrl(it.historyVideoKey),
                    feedbacks = it.feedbacks.map { feedback ->
                        FeedbackResponseDto.from(feedback)
                    }.toMutableSet()
                )
            }.toMutableSet()
        )
    }

    fun updateSpace(
        authentication: Authentication,
        spaceId: UUID,
        spaceImage: MultipartFile?,
        spaceRequestDto: SpaceRequestDto
    ): SpaceResponseDto {
        val user = getAuthUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUserRoleIsAdmin(user, space)

        space.isDeleted = true

        spaceRepository.save(space)

        return createSpace(authentication, spaceImage, spaceRequestDto)
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