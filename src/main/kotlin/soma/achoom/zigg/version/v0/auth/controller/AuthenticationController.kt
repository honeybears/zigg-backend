package soma.achoom.zigg.version.v0.auth.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import soma.achoom.zigg.version.v0.auth.dto.OAuth2UserRequestDto
import soma.achoom.zigg.version.v0.auth.service.AuthenticationService

@RestController
@RequestMapping("/api/v0/auth")
class AuthenticationController @Autowired constructor(
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/tokens")
    fun verifyAndGenerateToken(@RequestBody oAuth2UserRequestDto: OAuth2UserRequestDto): ResponseEntity<Void> {
        val tokenHeader = authenticationService.generateJWTToken(oAuth2UserRequestDto)
        return ResponseEntity.ok().headers(tokenHeader).build()
    }


}