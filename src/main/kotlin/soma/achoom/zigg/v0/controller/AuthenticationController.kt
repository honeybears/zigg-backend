package soma.achoom.zigg.v0.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.v0.dto.token.OAuthTokenVerificationDto
import soma.achoom.zigg.v0.dto.token.TokenResponseDto
import soma.achoom.zigg.v0.service.AuthenticationService

@RestController
@RequestMapping("/api/v0/auth")
class AuthenticationController @Autowired constructor(
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/oauth/token")
    fun verifyAndGenerateToken(@RequestBody requestDto: OAuthTokenVerificationDto): ResponseEntity<TokenResponseDto> {
        val tokenResponseDto:TokenResponseDto = authenticationService.verifyAndGenerateToken(requestDto)
        return ResponseEntity.ok(tokenResponseDto)
    }
}