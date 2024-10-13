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
import soma.achoom.zigg.invite.entity.InviteStatus
import soma.achoom.zigg.invite.entity.Invite
import soma.achoom.zigg.space.dto.InviteRequestDto
import soma.achoom.zigg.space.dto.SpaceReferenceUrlRequestDto
import soma.achoom.zigg.space.dto.SpaceRequestDto
import soma.achoom.zigg.space.entity.*
import soma.achoom.zigg.space.exception.GuestSpaceCreateLimitationException
import soma.achoom.zigg.space.exception.SpaceNotFoundException
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.space.exception.SpaceUserNotFoundInSpaceException
import soma.achoom.zigg.space.repository.SpaceUserRepository
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
    private val userRepository: UserRepository,
    private val spaceUserRepository: SpaceUserRepository,
    private val imageRepository: ImageRepository
) {
    @Value("\${space.default.image.url}")
    private lateinit var defaultSpaceImageUrl: String

    @Transactional(readOnly = false)
    fun inviteSpace(
        authentication: Authentication, spaceId: UUID, inviteRequestDto: InviteRequestDto
    ): Space {
        val user = userService.authenticationToUser(authentication)

        val invitedUsers: MutableSet<User> = inviteRequestDto.spaceUsers.map {
            userService.findUserByNickName(it.userNickname!!)
        }.toMutableSet()

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)

        val filteredInvite = invitedUsers.filter { invitee ->
            space.invites.none {
                it.invitee.userId == invitee.userId && it.status != InviteStatus.DENIED
            } || space.users.none {
                it.user?.userId == invitee.userId && it.withdraw.not()
            }
        }.map { invitee ->
            Invite(
                invitee = invitee, space = space, inviter = user, status = InviteStatus.WAITING
            )
        }.toMutableSet()

        space.invites.addAll(filteredInvite)

        spaceRepository.save(space)
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
        return space
    }

    @Transactional(readOnly = false)
    fun createSpace(
        authentication: Authentication, spaceRequestDto: SpaceRequestDto
    ): Space {
        val user = userService.authenticationToUser(authentication)

        checkGuestSpaceLimit(user)

        val invitedUsers = spaceRequestDto.spaceUsers.map {
            userService.findUserByNickName(it.userNickname!!)
        }.toMutableSet()

        val spaceBannerImage = spaceRequestDto.spaceImageUrl?.let {
            Image(uploader = user, imageKey = spaceRequestDto.spaceImageUrl.let {
                it.split("?")[0].split("/").subList(3, spaceRequestDto.spaceImageUrl.split("?")[0].split("/").size)
                    .joinToString("/")
            })
        } ?: imageRepository.findByImageKey(defaultSpaceImageUrl) ?: throw ImageNotfoundException()

        imageRepository.save(spaceBannerImage)

        val space = Space(
            name = spaceRequestDto.spaceName,
            imageKey = spaceBannerImage,
            users = mutableSetOf(),
            invites = mutableSetOf(),
        )

        val admin = SpaceUser(
            user = user,
            space = space,
            role = SpaceRole.ADMIN,
        )

        space.invites.addAll(invitedUsers.map {
            Invite(
                invitee = it, space = space, inviter = user, status = InviteStatus.WAITING
            )
        })

        spaceRepository.save(space)
        spaceUserRepository.save(admin)
        space.users.add(admin)
        spaceRepository.save(space)
        user.spaces.add(admin)
        userRepository.save(user)

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

        return space
    }

    @Transactional(readOnly = false)
    fun withdrawSpace(authentication: Authentication, spaceId: UUID) {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)

        space.users.find { it.user?.userId == user.userId }?.let {
            it.withdraw = true
        }
        spaceRepository.save(space)
    }

    @Transactional(readOnly = true)
    fun getSpaces(authentication: Authentication): List<Space> {
        val user = userService.authenticationToUser(authentication)
        val spaceList = spaceRepository.findSpacesByUser(user)
        return spaceList
    }

    @Transactional(readOnly = true)
    fun getSpace(authentication: Authentication, spaceId: UUID): Space {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
        return space
    }

    @Transactional(readOnly = false)
    fun updateSpace(
        authentication: Authentication, spaceId: UUID, spaceRequestDto: SpaceRequestDto
    ): Space {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)

        space.name = spaceRequestDto.spaceName

        spaceRequestDto.spaceImageUrl?.let {
            space.imageKey = Image(
                imageKey = it.let {
                    it.split("?")[0].split("/").subList(3, spaceRequestDto.spaceImageUrl.split("?")[0].split("/").size)
                        .joinToString("/")
                }, uploader = user
            )
            imageRepository.save(space.imageKey)
        }

        spaceRepository.save(space)

        return space
    }

    @Transactional(readOnly = false)
    fun deleteSpace(authentication: Authentication, spaceId: UUID) {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
//        space.spaceUsers.forEach{
//            it.user = null
//            it.space = null
//            spaceUserRepository.delete(it)
//        }
        space.users.clear()
        space.histories.clear()
        space.invites.clear()

        // Delete the space
        spaceRepository.delete(space)
    }

    @Transactional(readOnly = false)
    fun addReferenceUrl(
        authentication: Authentication, spaceId: UUID, spaceReferenceUrlRequestDto: SpaceReferenceUrlRequestDto
    ): Space {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
        space.referenceVideoKey = spaceReferenceUrlRequestDto.referenceUrl

        spaceRepository.save(space)

        return space
    }

    @Transactional(readOnly = false)
    fun deleteReferenceUrl(authentication: Authentication, spaceId: UUID): Space {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId) ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
        space.referenceVideoKey = null

        spaceRepository.save(space)
        return space
    }


    @Transactional(readOnly = false)
    fun validateSpaceUser(user: User, space: Space): SpaceUser {
        space.users.find {
            it.user == user && it.withdraw.not()
        }?.let {
            return it
        }
        throw SpaceUserNotFoundInSpaceException()
    }

    private fun checkGuestSpaceLimit(user: User) {
        println(user.role)
        println(user.spaces.size)
        if (user.role == UserRole.GUEST && user.spaces.size >= 1) {
            throw GuestSpaceCreateLimitationException()
        }
    }
}