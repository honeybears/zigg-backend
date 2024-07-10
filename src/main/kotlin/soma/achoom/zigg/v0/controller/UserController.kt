package soma.achoom.zigg.v0.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.v0.auth.CustomOAuth2User
import soma.achoom.zigg.v0.dto.ResponseMessage
import soma.achoom.zigg.v0.dto.UserRequestDto
import soma.achoom.zigg.v0.dto.UserResponseDto
import soma.achoom.zigg.v0.model.User
import soma.achoom.zigg.v0.repository.UserRepository
import soma.achoom.zigg.v0.service.UserService

@RestController
@RequestMapping("/api/v0/user")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/register")
    fun register(@AuthenticationPrincipal auth: CustomOAuth2User,@RequestBody userRequestDto: UserRequestDto): ResponseEntity<ResponseMessage<UserResponseDto>> {
        val isValidNickname = userService.userNickNameIsValid(userRequestDto.userNickname!!)
        if(!isValidNickname) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseMessage("nickname is invalid."))
        }
        val userResponseDto = userService.updateUserNickName(auth,userRequestDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseMessage("success", userResponseDto))
    }

    @GetMapping("/info")
    fun test(@AuthenticationPrincipal auth : CustomOAuth2User):String{
//        println("LOG : INFO")
//        println(auth.attributes)
//        println(auth.name)
//        println(auth.authorities)
//        println(auth.registrationId)
//        val user = userService.getAuthUser(auth)
//        return user.userName as String
        return "hi"
    }
}