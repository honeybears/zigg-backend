package soma.achoom.zigg.invite.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.invite.dto.InviteActionRequestDto
import soma.achoom.zigg.invite.dto.InviteResponseDto
import soma.achoom.zigg.invite.entity.InviteStatus
import soma.achoom.zigg.invite.service.InviteService
import java.util.*

@RestController
@RequestMapping("/api/v0/invites")
class InviteController(
    private val inviteService: InviteService,
) {
    @GetMapping
    fun getInvites(authentication: Authentication) : ResponseEntity<List<InviteResponseDto>>{
        val invites = inviteService.getInvites(authentication)
        return ResponseEntity.ok(invites)
    }
    @PostMapping("/{inviteId}")
    fun actionInvite(authentication: Authentication, @PathVariable inviteId: Long,@RequestBody action: InviteActionRequestDto) : ResponseEntity<Void> {
        inviteService.actionInvite(authentication, inviteId, action)
        return ResponseEntity.ok().build()
    }
}