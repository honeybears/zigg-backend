package soma.achoom.zigg.v0.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.v0.dto.request.UserRequestDto
import soma.achoom.zigg.v0.dto.response.UserResponseDto
import soma.achoom.zigg.v0.repository.UserRepository
import soma.achoom.zigg.v0.service.UserService

@RestController
@RequestMapping("/api/v0/user")
class UserController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/register")
    fun register(authentication: Authentication,@RequestBody userRequestDto: UserRequestDto): ResponseEntity<UserResponseDto> {
        println(userRequestDto.userNickname)
        val userResponseDto = userService.register(authentication,userRequestDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto)
    }

    @PostMapping("/info")
    fun test(authentication:Authentication, @RequestBody userRequestDto: UserRequestDto):ResponseEntity<UserResponseDto>{
        println(authentication.name)
        println(authentication.details)
        println(authentication.credentials)
        println(authentication.authorities)
        val userResponseDto = UserResponseDto(authentication.name.toLong(),"test","test")
        return ResponseEntity.ok(userResponseDto)

    }
}