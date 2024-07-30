package soma.achoom.zigg.v0.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.v0.dto.request.OAuth2MetaDataRequestDto
import soma.achoom.zigg.v0.dto.response.UserExistsResponseDto
import soma.achoom.zigg.v0.dto.token.OAuth2UserRequestDto
import soma.achoom.zigg.v0.service.AuthenticationService

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