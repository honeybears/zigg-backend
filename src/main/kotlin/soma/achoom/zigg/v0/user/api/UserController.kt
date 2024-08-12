package soma.achoom.zigg.v0.user.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.v0.auth.dto.OAuth2MetaDataRequestDto
import soma.achoom.zigg.v0.auth.dto.OAuth2UserRequestDto
import soma.achoom.zigg.v0.auth.service.AuthenticationService
import soma.achoom.zigg.v0.user.dto.UserExistsResponseDto
import soma.achoom.zigg.v0.user.dto.UserRequestDto
import soma.achoom.zigg.v0.user.dto.UserResponseDto
import soma.achoom.zigg.v0.user.service.UserService

@RestController
@RequestMapping("/api/v0/users")
class UserController @Autowired constructor(
    private val userService: UserService,

    private val authenticationService: AuthenticationService
) {
    @GetMapping("/search/{nickname}")
    fun searchUser(authentication: Authentication,@PathVariable nickname:String): ResponseEntity<MutableSet<UserResponseDto>> {
        val userResponseDto = userService.searchUser(authentication,nickname)
        return ResponseEntity.ok(userResponseDto)
    }
    @PostMapping
    fun registerUser(@RequestBody oAuth2UserRequestDto: OAuth2UserRequestDto): ResponseEntity<Void> {
        val tokenHeader = authenticationService.registers(oAuth2UserRequestDto)
        return ResponseEntity.ok().headers(tokenHeader).build()
    }
    @GetMapping
    fun getUserInfo(authentication: Authentication): ResponseEntity<UserResponseDto> {
        val userResponseDto = userService.getUserInfo(authentication)
        return ResponseEntity.ok(userResponseDto)
    }

    @PatchMapping
    fun updateUser(authentication: Authentication,@RequestBody userRequestDto: UserRequestDto): ResponseEntity<UserResponseDto> {
        println(userRequestDto.userNickname)
        val userResponseDto = userService.updateUser(authentication,userRequestDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto)
    }


    @PostMapping("/exists")
    fun checkUserExists(@RequestBody oAuth2MetaDataRequestDto: OAuth2MetaDataRequestDto): ResponseEntity<UserExistsResponseDto> {
        val userExistsResponseDto = authenticationService.userExistsCheckByOAuthPlatformAndProviderId(oAuth2MetaDataRequestDto)
        return ResponseEntity.ok(userExistsResponseDto)
    }
}