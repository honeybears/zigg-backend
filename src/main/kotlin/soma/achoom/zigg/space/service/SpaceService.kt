package soma.achoom.zigg.space.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.firebase.dto.FCMEvent
import soma.achoom.zigg.firebase.service.FCMService
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.invite.entity.InviteStatus
import soma.achoom.zigg.invite.entity.Invite
import soma.achoom.zigg.space.dto.InviteUsersRequestDto
import soma.achoom.zigg.space.dto.SpaceReferenceUrlRequestDto
import soma.achoom.zigg.space.dto.SpaceRequestDto
import soma.achoom.zigg.space.dto.SpaceResponseDto
import soma.achoom.zigg.space.entity.*
import soma.achoom.zigg.space.exception.SpaceNotFoundException
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.space.exception.LowSpacePermissionException
import soma.achoom.zigg.space.exception.SpaceUserNotFoundInSpaceException
import soma.achoom.zigg.space.repository.SpaceUserRepository
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.repository.UserRepository
import soma.achoom.zigg.user.service.UserService
import java.util.UUID


@Service
class SpaceService(
    private val spaceRepository: SpaceRepository,
    private val userService: UserService,
    private val responseDtoManager: ResponseDtoManager,
    private val fcmService: FCMService,
    private val userRepository: UserRepository,
    private val spaceUserRepository: SpaceUserRepository
) {
    @Value("\${space.default.image.url}")
    private lateinit var defaultSpaceImageUrl: String

    @Transactional(readOnly = false)
    fun inviteUserToSpace(
        authentication: Authentication,
        spaceId: UUID,
        inviteUsersRequestDto: InviteUsersRequestDto
    ): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)

        val invitedUsers = inviteUsersRequestDto.spaceUsers.map {
            userService.findUserByNickName(it.userNickname!!)
        }.toMutableSet()

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)

        space.invites.find { invitedUsers.contains(it.invitee).and(it.inviteStatus != InviteStatus.DENIED) }?.let {
            invitedUsers.remove(it.invitee)
        }
        space.spaceUsers.find { invitedUsers.contains(it.user).and(it.withdraw.not()) }?.let {
            invitedUsers.remove(it.user)
        }

        space.invites.addAll(
            invitedUsers.map {
                Invite(
                    invitee = it,
                    space = space,
                    inviter = user,
                    inviteStatus = InviteStatus.WAITING
                )
            }
        )

        spaceRepository.save(space)
        fcmService.sendMessageTo(
            FCMEvent(
                users = invitedUsers,
                title = "새로운 스페이스에 초대되었습니다.",
                body = "${user.userNickname}님이 회원님을 ${space.spaceName} 스페이스에 초대하였습니다.",
                data = mapOf("spaceId" to space.spaceId.toString()),
                android = null,
                apns = null
            )
        )
        return responseDtoManager.generateSpaceResponseShortDto(space)
    }

    @Transactional(readOnly = false)
    fun createSpace(
        authentication: Authentication,
        spaceRequestDto: SpaceRequestDto
    ): SpaceResponseDto {
        val user = userService.authenticationToUser(authentication)
        val invitedUsers = spaceRequestDto.spaceUsers.map {
            userService.findUserByNickName(it.userNickname!!)
        }.toMutableSet()
        val space = Space(
            spaceName = spaceRequestDto.spaceName,
            spaceImageKey = spaceRequestDto.spaceImageUrl?.let {
                it.split("?")[0].split("/").subList(3, spaceRequestDto.spaceImageUrl.split("?")[0].split("/").size)
                    .joinToString("/")
            } ?: defaultSpaceImageUrl,
            spaceUsers = mutableSetOf(),
            invites = mutableSetOf(),
        )

        space.spaceUsers.add(
            SpaceUser(
                user = user,
                space = space,
                spaceRole = SpaceRole.ADMIN,
            )
        )
        space.invites.addAll(
            invitedUsers.map {
                Invite(
                    invitee = it,
                    space = space,
                    inviter = user,
                    inviteStatus = InviteStatus.WAITING
                )
            }
        )
        spaceRepository.save(space)
        invitedUsers.isNotEmpty().takeIf { it }?.let {
            fcmService.sendMessageTo(
                FCMEvent(
                    users = invitedUsers,
                    title = "새로운 스페이스에 초대되었습니다.",
                    body = "${user.userNickname}님이 회원님을 ${space.spaceName} 스페이스에 초대하였습니다.",
                    data = mapOf("spaceId" to space.spaceId.toString()),
                    android = null,
                    apns = null
                )
            )
        }

        return responseDtoManager.generateSpaceResponseShortDto(space)
    }
    @Transactional(readOnly = false)
    fun withdrawSpace(authentication: Authentication, spaceId: UUID) {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)

        space.spaceUsers.find{it.user == user}?.let {
            it.withdraw = true
        }
        spaceRepository.save(space)
    }

    @Transactional(readOnly = true)
    fun getSpaces(authentication: Authentication): List<SpaceResponseDto> {
        val user = userService.authenticationToUser(authentication)
        val spaceList = spaceRepository.findSpacesByUser(user)
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
        validateSpaceUser(user, space)
        spaceRepository.save(space)
        space.spaceName = spaceRequestDto.spaceName
        spaceRequestDto.spaceImageUrl?.let {
            space.spaceImageKey = it.let {
                it.split("?")[0].split("/").subList(3, spaceRequestDto.spaceImageUrl.split("?")[0].split("/").size)
                    .joinToString("/")
            }
        }
        return responseDtoManager.generateSpaceResponseShortDto(space)
    }

    @Transactional(readOnly = false)
    fun deleteSpace(authentication: Authentication, spaceId: UUID) {
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)

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
        space.referenceVideoKey = spaceReferenceUrlRequestDto.referenceUrl

        spaceRepository.save(space)

        return responseDtoManager.generateSpaceResponseShortDto(space)
    }
    @Transactional(readOnly = false)
    fun deleteReferenceUrl(authentication: Authentication, spaceId: UUID) : SpaceResponseDto{
        val user = userService.authenticationToUser(authentication)

        val space = spaceRepository.findSpaceBySpaceId(spaceId)
            ?: throw SpaceNotFoundException()

        validateSpaceUser(user, space)
        space.referenceVideoKey = null

        spaceRepository.save(space)
        return responseDtoManager.generateSpaceResponseShortDto(space)
    }


    @Transactional(readOnly = false)
    fun validateSpaceUser(user: User, space: Space): SpaceUser {
        space.spaceUsers.find {
            it.user == user && it.withdraw.not()
        }?.let {
            return it
        }
        throw SpaceUserNotFoundInSpaceException()
    }
}