package soma.achoom.zigg.invite.service

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.invite.dto.InviteActionRequestDto
import soma.achoom.zigg.invite.entity.Invite
import soma.achoom.zigg.invite.entity.InviteStatus
import soma.achoom.zigg.invite.exception.InviteNotFoundException
import soma.achoom.zigg.invite.exception.InvitedUserMissMatchException
import soma.achoom.zigg.invite.exception.UserAlreadyInSpaceException
import soma.achoom.zigg.invite.repository.InviteRepository
import soma.achoom.zigg.space.entity.SpaceRole
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.space.repository.SpaceUserRepository
import soma.achoom.zigg.user.service.UserService
import java.util.*

@Service
class InviteService(
    private val userService: UserService,
    private val spaceRepository: SpaceRepository,
    private val inviteRepository: InviteRepository,
    private val spaceUserRepository: SpaceUserRepository,

    ) {

    @Transactional(readOnly = true)
    fun getInvites(authentication: Authentication): List<Invite> {
        val user = userService.authenticationToUser(authentication)
        val invites = inviteRepository.findAllByInvitee(user)
        return invites
    }

    @Transactional(readOnly = false)
    fun actionInvite(authentication: Authentication, inviteId: UUID, action: InviteActionRequestDto) {
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
                if (space.users.any { it.user?.userId == user.userId && !it.withdraw }) {
                    throw UserAlreadyInSpaceException()
                }
                val spaceUser = SpaceUser(
                    user = user,
                    space = space,
                    role = SpaceRole.USER
                )
                spaceUserRepository.save(spaceUser)
                space.users.add(spaceUser)
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