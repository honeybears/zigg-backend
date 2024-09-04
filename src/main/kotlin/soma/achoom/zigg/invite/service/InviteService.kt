package soma.achoom.zigg.invite.service

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.invite.dto.InviteActionRequestDto
import soma.achoom.zigg.invite.dto.InviteListResponseDto
import soma.achoom.zigg.invite.dto.InviteResponseDto
import soma.achoom.zigg.invite.entity.InviteStatus
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
        val invites = inviteRepository.findAllByUser(user)
        return InviteListResponseDto(
            invites.map {
                responseDtoManager.generateInviteResponseDto(it)
            }.toList()
        )

    }

    @Transactional(readOnly = false)
    fun actionInvite(authentication: Authentication, inviteId: UUID, action: InviteActionRequestDto) {
        val user = userService.authenticationToUser(authentication)
        val invite = inviteRepository.findById(inviteId).orElseThrow { Exception("Invite not found") }
        if (invite.user.userId != user.userId) {
            throw Exception("You are not the invitee")
        }
        val space = invite.space
        when (action.accept) {
            true -> {
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
        inviteRepository.save(invite)
        spaceRepository.save(space)
    }

}