package soma.achoom.zigg.invite.service

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.firebase.dto.FCMEvent
import soma.achoom.zigg.firebase.service.FCMService
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.invite.dto.InviteActionRequestDto
import soma.achoom.zigg.invite.dto.InviteListResponseDto
import soma.achoom.zigg.invite.entity.InviteStatus
import soma.achoom.zigg.invite.exception.InviteNotFoundException
import soma.achoom.zigg.invite.exception.InvitedUserMissMatchException
import soma.achoom.zigg.invite.exception.UserAlreadyInSpaceException
import soma.achoom.zigg.invite.repository.InviteRepository
import soma.achoom.zigg.space.entity.SpaceRole
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.user.service.UserService
import java.util.*

@Service
class InviteService(
    private val userService: UserService,
    private val spaceRepository: SpaceRepository,
    private val inviteRepository: InviteRepository,
    private val responseDtoManager: ResponseDtoManager,

    ) {
    @Transactional(readOnly = true)
    fun getInvites(authentication: Authentication): InviteListResponseDto {
        val user = userService.authenticationToUser(authentication)
        val invites = inviteRepository.findAllByInvitee(user)
        return InviteListResponseDto(
            invites.filter{
                it.isExpired.not()
            }.map {
                responseDtoManager.generateInviteResponseDto(it)
            }.toList()
        )

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
                if (space.spaceUsers.find{it.user == user} != null) {
                    throw UserAlreadyInSpaceException()
                }
                space.spaceUsers.add(
                    SpaceUser(
                        user = user,
                        space = space,
                        spaceRole = SpaceRole.USER
                    )
                )
                invite.inviteStatus = InviteStatus.ACCEPTED
            }
            false -> {
                invite.inviteStatus = InviteStatus.DENIED
            }
        }
        invite.isExpired = true
        inviteRepository.save(invite)
        spaceRepository.save(space)
    }

}