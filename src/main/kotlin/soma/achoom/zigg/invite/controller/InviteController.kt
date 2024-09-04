package soma.achoom.zigg.invite.controller

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.invite.dto.InviteActionRequestDto
import soma.achoom.zigg.invite.service.InviteService
import java.util.*

@RestController("/api/v0/invites")
class InviteController(
    private val inviteService: InviteService
) {
    @GetMapping
    fun getInvites(authentication: Authentication) {
        inviteService.getInvites(authentication)
    }
    @PostMapping("/{inviteId}")
    fun actionInvite(authentication: Authentication, @PathVariable inviteId: UUID,@RequestBody action: InviteActionRequestDto) {
        inviteService.actionInvite(authentication, inviteId, action)
    }
}