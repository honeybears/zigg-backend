package soma.achoom.zigg.space.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.exception.ImageNotfoundException
import soma.achoom.zigg.content.repository.ImageRepository
import soma.achoom.zigg.firebase.dto.FCMEvent
import soma.achoom.zigg.firebase.service.FCMService
import soma.achoom.zigg.history.dto.HistoryResponseDto
import soma.achoom.zigg.invite.dto.InviteResponseDto
import soma.achoom.zigg.invite.entity.InviteStatus
import soma.achoom.zigg.invite.entity.Invite
import soma.achoom.zigg.invite.repository.InviteRepository
import soma.achoom.zigg.s3.service.S3Service
import soma.achoom.zigg.space.dto.*
import soma.achoom.zigg.space.entity.*
import soma.achoom.zigg.space.exception.GuestSpaceCreateLimitationException
import soma.achoom.zigg.space.exception.SpaceNotFoundException
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.space.exception.SpaceUserNotFoundInSpaceException
import soma.achoom.zigg.space.repository.SpaceUserRepository
import soma.achoom.zigg.user.dto.UserResponseDto
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.entity.UserRole
import soma.achoom.zigg.user.repository.UserRepository
import soma.achoom.zigg.user.service.UserService
import java.util.UUID


@Service
class SpaceService(
    private val spaceRepository: SpaceRepository,
    private val userService: UserService,
    private val fcmService: FCMService,
    private val inviteRepository: InviteRepository,
    private val spaceUserRepository: SpaceUserRepository,
    private val imageRepository: ImageRepository,
    private val s3Service: S3Service
) {
    @Value("\${space.default.image.url}")
    private lateinit var defaultSpaceImageUrl: String

    @Transactional(readOnly = false)
    fun inviteSpace(
        authentication: Authentication, spaceId: Long, inviteRequestDto: InviteRequestDto
    ): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)

        val invitedUsers: MutableSet<User> = inviteRequestDto.spaceUsers.map {
            userService.findUserByNickName(it.userNickname!!)
        }.toMutableSet()

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)

        val filteredInvite = invitedUsers.filter { invitee ->
            inviteRepository.findInvitesBySpace(space).none {
                it.invitee.userId == invitee.userId && it.status != InviteStatus.DENIED
            } && spaceUserRepository.findSpaceUserBySpace(space).none {
                it.user?.userId == invitee.userId && it.withdraw.not()
            }
        }.map { invitee ->
            Invite(
                invitee = invitee, space = space, inviter = user, status = InviteStatus.WAITING
            )
        }.toMutableSet()

        inviteRepository.saveAll(filteredInvite)

        fcmService.sendMessageTo(
            FCMEvent(
                users = filteredInvite.map { it.invitee }.toMutableSet(),
                title = "새로운 스페이스에 초대되었습니다.",
                body = "${user.name}님이 회원님을 ${space.name} 스페이스에 초대하였습니다.",
                data = mapOf("spaceId" to space.spaceId.toString()),
                android = null,
                apns = null
            )
        )
        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.name,
            spaceImageUrl = s3Service.getPreSignedGetUrl(space.imageKey.imageKey),
            referenceVideoUrl = space.referenceVideoKey,
            spaceUsers = spaceUserRepository.findSpaceUserBySpace(space).filter { it.withdraw.not() }.map {
                SpaceUserResponseDto(
                    userId = it.user?.userId,
                    userNickname = it.user?.nickname,
                    spaceUserId = it.spaceUserId,
                    spaceRole = it.role,
                    profileImageUrl = s3Service.getPreSignedGetUrl(it.user?.profileImageKey?.imageKey),
                    userName = it.user?.name
                )
            }.toMutableSet(),
            createdAt = space.createAt,
            updatedAt = space.updateAt,
            invites = inviteRepository.findInvitesBySpace(space).map {
                InviteResponseDto(
                    inviteId = it.inviteId,
                    invitedUser = UserResponseDto(
                        userName = it.invitee.name,
                        userNickname = it.invitee.nickname,
                        profileImageUrl = s3Service.getPreSignedGetUrl(it.invitee.profileImageKey.imageKey),
                        userId = it.invitee.userId
                    ),
                    inviter = UserResponseDto(
                        userName = it.invitee.name,
                        userNickname = it.invitee.nickname,
                        profileImageUrl = s3Service.getPreSignedGetUrl(it.invitee.profileImageKey.imageKey),
                        userId = it.invitee.userId
                    ),
                    createdAt = it.createAt
                )
            }.toMutableSet()
        )
    }

    @Transactional(readOnly = false)
    fun createSpace(
        authentication: Authentication, spaceRequestDto: SpaceRequestDto
    ): SpaceResponseDto {

        val user = userService.authenticationToUser(authentication)


        val invitedUsers = spaceRequestDto.spaceUsers.map {
            userService.findUserByNickName(it.userNickname!!)
        }.toMutableSet()

        val spaceBannerImage = spaceRequestDto.spaceImageUrl?.let {
            Image.fromUrl(
                imageUrl = it,
                uploader = user
            )
        } ?: imageRepository.findByImageKey(defaultSpaceImageUrl) ?: throw ImageNotfoundException()


        val space = Space(
            name = spaceRequestDto.spaceName,
            imageKey = spaceBannerImage,
        )

        spaceRepository.save(space)

        val admin = SpaceUser(
            user = user,
            space = space,
            role = SpaceRole.ADMIN,
        )

        spaceUserRepository.save(admin)

        inviteRepository.saveAll(
            invitedUsers.map {
                Invite(
                    invitee = it, space = space, inviter = user, status = InviteStatus.WAITING
                )
            }.toList()
        )



        invitedUsers.isNotEmpty().takeIf { it }?.let {
            fcmService.sendMessageTo(
                FCMEvent(
                    users = invitedUsers,
                    title = "새로운 스페이스에 초대되었습니다.",
                    body = "${user.name}님이 회원님을 ${space.name} 스페이스에 초대하였습니다.",
                    data = mapOf("spaceId" to space.spaceId.toString()),
                    android = null,
                    apns = null
                )
            )
        }

        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.name,
            spaceImageUrl = s3Service.getPreSignedGetUrl(space.imageKey.imageKey),
            referenceVideoUrl = space.referenceVideoKey,
            spaceUsers = spaceUserRepository.findSpaceUserBySpace(space).filter { it.withdraw.not() }.map {
                SpaceUserResponseDto(
                    userId = it.user?.userId,
                    userNickname = it.user?.nickname,
                    spaceUserId = it.spaceUserId,
                    spaceRole = it.role,
                    profileImageUrl = s3Service.getPreSignedGetUrl(it.user?.profileImageKey?.imageKey),
                    userName = it.user?.name
                )
            }.toMutableSet(),
            createdAt = space.createAt,
            updatedAt = space.updateAt,
        )
    }

    @Transactional(readOnly = false)
    fun withdrawSpace(authentication: Authentication, spaceId: Long) {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
        spaceUserRepository.findSpaceUserBySpace(space).find { it.user?.userId == user.userId }?.let {
            it.withdraw = true
        }
        spaceRepository.save(space)
    }

    @Transactional(readOnly = true)
    fun getSpaces(authentication: Authentication): List<SpaceResponseDto> {
        val user = userService.authenticationToUser(authentication)
        val spaceList = spaceRepository.findSpacesByUser(user)
        return spaceList.map {
            SpaceResponseDto(
                spaceId = it.spaceId,
                spaceName = it.name,
                spaceImageUrl = s3Service.getPreSignedGetUrl(it.imageKey.imageKey),
                referenceVideoUrl = it.referenceVideoKey,
                spaceUsers = spaceUserRepository.findSpaceUserBySpace(it).filter { it.withdraw.not() }.map {
                    SpaceUserResponseDto(
                        userId = it.user?.userId,
                        userNickname = it.user?.nickname,
                        spaceUserId = it.spaceUserId,
                        spaceRole = it.role,
                        profileImageUrl = s3Service.getPreSignedGetUrl(it.user?.profileImageKey?.imageKey),
                        userName = it.user?.name
                    )
                }.toMutableSet(),
                createdAt = it.createAt,
                updatedAt = it.updateAt,
            )
        }
    }

    @Transactional(readOnly = true)
    fun getSpace(authentication: Authentication, spaceId: Long): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.name,
            spaceImageUrl = s3Service.getPreSignedGetUrl(space.imageKey.imageKey),
            referenceVideoUrl = space.referenceVideoKey,
            spaceUsers = spaceUserRepository.findSpaceUserBySpace(space).filter { it.withdraw.not() }.map {
                SpaceUserResponseDto(
                    userId = it.user?.userId,
                    userNickname = it.user?.nickname,
                    spaceUserId = it.spaceUserId,
                    spaceRole = it.role,
                    profileImageUrl = s3Service.getPreSignedGetUrl(it.user?.profileImageKey?.imageKey),
                    userName = it.user?.name
                )
            }.toMutableSet(),
            history = space.histories.map {
                HistoryResponseDto(
                    historyId = it.historyId,
                    historyName = it.name,
                    historyVideoPreSignedUrl = s3Service.getPreSignedGetUrl(it.videoKey.videoKey),
                    historyVideoThumbnailPreSignedUrl = s3Service.getPreSignedGetUrl(it.videoThumbnailUrl.imageKey),
                    createdAt = it.createAt,
                    feedbackCount = it.feedbacks.size,
                    videoDuration = it.videoKey.duration,

                )
            }.toMutableSet(),
            createdAt = space.createAt,
            updatedAt = space.updateAt,
        )
    }

    @Transactional(readOnly = false)
    fun updateSpace(
        authentication: Authentication, spaceId: Long, spaceRequestDto: SpaceRequestDto
    ): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)

        space.name = spaceRequestDto.spaceName

        spaceRequestDto.spaceImageUrl?.let {
            space.imageKey = Image.fromUrl(
                imageUrl = it,
                uploader = user
            )
        }

        spaceRepository.save(space)

        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.name,
            spaceImageUrl = s3Service.getPreSignedGetUrl(space.imageKey.imageKey),
            referenceVideoUrl = space.referenceVideoKey,
            spaceUsers = spaceUserRepository.findSpaceUserBySpace(space).filter { it.withdraw.not() }.map {
                SpaceUserResponseDto(
                    userId = it.user?.userId,
                    userNickname = it.user?.nickname,
                    spaceUserId = it.spaceUserId,
                    spaceRole = it.role,
                    profileImageUrl = s3Service.getPreSignedGetUrl(it.user?.profileImageKey?.imageKey),
                    userName = it.user?.name
                )
            }.toMutableSet(),
            createdAt = space.createAt,
            updatedAt = space.updateAt,
        )
    }

    @Transactional(readOnly = false)
    fun deleteSpace(authentication: Authentication, spaceId: Long) {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
        spaceUserRepository.deleteAllBySpace(space)
        // Delete the space
        spaceRepository.delete(space)
    }

    @Transactional(readOnly = false)
    fun addReferenceUrl(
        authentication: Authentication, spaceId: Long, spaceReferenceUrlRequestDto: SpaceReferenceUrlRequestDto
    ): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
        space.referenceVideoKey = spaceReferenceUrlRequestDto.referenceUrl

        spaceRepository.save(space)

        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.name,
            spaceImageUrl = s3Service.getPreSignedGetUrl(space.imageKey.imageKey),
            referenceVideoUrl = space.referenceVideoKey,
            spaceUsers = spaceUserRepository.findSpaceUserBySpace(space).filter { it.withdraw.not() }.map {
                SpaceUserResponseDto(
                    userId = it.user?.userId,
                    userNickname = it.user?.nickname,
                    spaceUserId = it.spaceUserId,
                    spaceRole = it.role,
                    profileImageUrl = s3Service.getPreSignedGetUrl(it.user?.profileImageKey?.imageKey),
                    userName = it.user?.name
                )
            }.toMutableSet(),
            createdAt = space.createAt,
            updatedAt = space.updateAt,
        )
    }

    @Transactional(readOnly = false)
    fun deleteReferenceUrl(authentication: Authentication, spaceId: Long): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
        space.referenceVideoKey = null

        spaceRepository.save(space)
        return SpaceResponseDto(
            spaceId = space.spaceId,
            spaceName = space.name,
            spaceImageUrl = s3Service.getPreSignedGetUrl(space.imageKey.imageKey),
            referenceVideoUrl = space.referenceVideoKey,
            spaceUsers = spaceUserRepository.findSpaceUserBySpace(space).filter { it.withdraw.not() }.map {
                SpaceUserResponseDto(
                    userId = it.user?.userId,
                    userNickname = it.user?.nickname,
                    spaceUserId = it.spaceUserId,
                    spaceRole = it.role,
                    profileImageUrl = s3Service.getPreSignedGetUrl(it.user?.profileImageKey?.imageKey),
                    userName = it.user?.name
                )
            }.toMutableSet(),
            createdAt = space.createAt,
            updatedAt = space.updateAt,
        )
    }


    @Transactional(readOnly = false)
    fun validateSpaceUser(user: User, space: Space): SpaceUser {
        spaceUserRepository.findSpaceUserBySpaceAndUser(space, user)?.let {
            return it
        }
        throw SpaceUserNotFoundInSpaceException()
    }

}