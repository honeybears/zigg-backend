package soma.achoom.zigg.auth.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.auth.dto.NicknameValidResponseDto

import soma.achoom.zigg.auth.dto.OAuth2UserRequestDto
import soma.achoom.zigg.auth.service.AuthenticationService

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
    @GetMapping("/nickname/{nickname}")
    fun checkNickname(@PathVariable nickname:String): ResponseEntity<NicknameValidResponseDto> {
        val nicknameValidResponseDto = authenticationService.checkNickname(nickname)
        return ResponseEntity.ok().body(nicknameValidResponseDto)
    }

}