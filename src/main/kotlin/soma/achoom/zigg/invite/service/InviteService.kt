package soma.achoom.zigg.invite.service

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.invite.dto.InviteActionRequestDto
import soma.achoom.zigg.invite.dto.InviteResponseDto
import soma.achoom.zigg.invite.entity.InviteStatus
import soma.achoom.zigg.invite.exception.InviteNotFoundException
import soma.achoom.zigg.invite.exception.InvitedUserMissMatchException
import soma.achoom.zigg.invite.exception.UserAlreadyInSpaceException
import soma.achoom.zigg.invite.repository.InviteRepository
import soma.achoom.zigg.s3.service.S3Service
import soma.achoom.zigg.space.dto.SpaceResponseDto
import soma.achoom.zigg.space.dto.SpaceUserResponseDto
import soma.achoom.zigg.space.entity.SpaceRole
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.space.repository.SpaceUserRepository
import soma.achoom.zigg.user.dto.UserResponseDto
import soma.achoom.zigg.user.service.UserService
import java.util.*

@Service
class InviteService(
    private val userService: UserService,
    private val spaceRepository: SpaceRepository,
    private val inviteRepository: InviteRepository,
    private val spaceUserRepository: SpaceUserRepository,
    private val s3Service: S3Service,

    ) {

    @Transactional(readOnly = true)
    fun getInvites(authentication: Authentication): List<InviteResponseDto> {
        val user = userService.authenticationToUser(authentication)
        val invites = inviteRepository.findAllByInvitee(user)
        return invites.map {
            InviteResponseDto(
                inviteId = it.inviteId,
                inviter = UserResponseDto(
                    userId = it.inviter.userId,
                    userName = it.inviter.name,
                    userNickname = it.inviter.nickname,
                    profileImageUrl = it.inviter.profileImageKey.imageKey,
                ),
                invitedUser = UserResponseDto(
                    userId = it.invitee.userId,
                    userName = it.invitee.name,
                    userNickname = it.invitee.nickname,
                    profileImageUrl = it.invitee.profileImageKey.imageKey,
                ),
                space = SpaceResponseDto(
                    spaceId = it.space.spaceId,
                    spaceName = it.space.name,
                    spaceImageUrl = s3Service.getPreSignedGetUrl(it.space.imageKey.imageKey),
                    spaceUsers = spaceUserRepository.findSpaceUserBySpace(it.space).map{ spaceUser ->
                        SpaceUserResponseDto(
                            spaceUserId = spaceUser.spaceUserId,
                            userId = spaceUser.user?.userId,
                            userName = spaceUser.user?.name,
                            userNickname = spaceUser.user?.nickname,
                            profileImageUrl = s3Service.getPreSignedGetUrl(spaceUser.user?.profileImageKey?.imageKey),
                            spaceRole = spaceUser.role,
                        )
                    }.toMutableSet(),
                    createdAt = it.space.createAt,
                    updatedAt = it.space.updateAt,
                ),
                createdAt = it.createAt,
            )
        }
    }

    @Transactional(readOnly = false)
    fun actionInvite(authentication: Authentication, inviteId: Long, action: InviteActionRequestDto) {
        val user = userService.authenticationToUser(authentication)
        val invite = inviteRepository.findById(inviteId).orElseThrow { InviteNotFoundException() }

        if (invite.invitee.userId != user.userId) {
            throw InvitedUserMissMatchException()
        }

        if (invite.isExpired) {
            throw InviteNotFoundException()
        }

        val space = invite.space
        when (action.accept) {
            true -> {
                if (spaceUserRepository.existsSpaceUserBySpaceAndUser(space, user)) {
                    throw UserAlreadyInSpaceException()
                }
                val spaceUser = SpaceUser(
                    user = user,
                    space = space,
                    role = SpaceRole.USER
                )
                spaceUserRepository.save(spaceUser)
                invite.status = InviteStatus.ACCEPTED
            }

            false -> {
                invite.status = InviteStatus.DENIED
                invite.isExpired = true
            }
        }
        invite.isExpired = true
        inviteRepository.save(invite)
        spaceRepository.save(space)
    }

}