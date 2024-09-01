package soma.achoom.zigg.firebase.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.firebase.dto.FCMTokenRequestDto
import soma.achoom.zigg.firebase.service.FCMService

@RestController("/api/v0/fcm")
class FCMController(
    private val fcmService: FCMService
) {
    @PostMapping("/token")
    fun registerToken(authentication: Authentication,token: FCMTokenRequestDto) : ResponseEntity<Void> {
        fcmService.registerToken(authentication,token)
        return ResponseEntity.ok().build()
    }
}