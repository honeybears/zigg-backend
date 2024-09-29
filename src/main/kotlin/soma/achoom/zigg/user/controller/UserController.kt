package soma.achoom.zigg.user.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.auth.dto.OAuth2MetaDataRequestDto
import soma.achoom.zigg.auth.dto.OAuth2UserRequestDto
import soma.achoom.zigg.auth.service.AuthenticationService
import soma.achoom.zigg.firebase.dto.FCMTokenRequestDto
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.history.dto.UploadContentTypeRequestDto
import soma.achoom.zigg.s3.service.S3DataType
import soma.achoom.zigg.s3.service.S3Service
import soma.achoom.zigg.user.dto.UserExistsResponseDto
import soma.achoom.zigg.user.dto.UserRequestDto
import soma.achoom.zigg.user.dto.UserResponseDto
import soma.achoom.zigg.user.service.UserService
import java.util.*


@RestController
@RequestMapping("/api/v0/users")
class UserController @Autowired constructor(
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val s3Service: S3Service,
    private val responseDtoManager: ResponseDtoManager
) {
    @PostMapping("/pre-signed-url/{value}")
    fun getPreSignUrl(
        @RequestBody uploadContentTypeRequestDto: UploadContentTypeRequestDto,
        @PathVariable value: String
    ): ResponseEntity<String> {
        if (value.trim() == "profile") {
            val preSignedUrl = s3Service.getPreSignedPutUrl(
                S3DataType.USER_PROFILE_IMAGE,
                UUID.randomUUID(),
                uploadContentTypeRequestDto
            )
            return ResponseEntity.ok(preSignedUrl)

        } else if (value.trim() == "banner") {
            val preSignedUrl = s3Service.getPreSignedPutUrl(
                S3DataType.USER_BANNER_IMAGE,
                UUID.randomUUID(),
                uploadContentTypeRequestDto
            )
            return ResponseEntity.ok(preSignedUrl)
        } else
            return ResponseEntity.badRequest().build()
    }

    @PostMapping("/fcm")
    fun registerToken(authentication: Authentication, @RequestBody token: FCMTokenRequestDto): ResponseEntity<Void> {
        userService.registerToken(authentication, token)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/search/{nickname}")
    fun searchUser(
        authentication: Authentication,
        @PathVariable nickname: String
    ): ResponseEntity<List<UserResponseDto>> {
        val users = userService.searchUser(authentication, nickname)
        return ResponseEntity.ok(
            users.map {
                responseDtoManager.generateUserResponseDto(it)
            }
        )
    }

    @PostMapping("/info")
    fun getUserInfoByUserId(authentication: Authentication,@RequestBody userRequestDto: UserRequestDto): ResponseEntity<UserResponseDto> {
        val user = userService.getUserInfoByUserId(authentication,userRequestDto)
        return ResponseEntity.ok(responseDtoManager.generateUserResponseDto(user))
    }

    @PostMapping("/exists")
    fun checkUserExists(@RequestBody oAuth2MetaDataRequestDto: OAuth2MetaDataRequestDto): ResponseEntity<UserExistsResponseDto> {
        val userExists = authenticationService.userExistsCheckByOAuthPlatformAndProviderId(oAuth2MetaDataRequestDto)
        return ResponseEntity.ok(UserExistsResponseDto(userExists))
    }

    @PostMapping
    fun registerUser(@RequestBody oAuth2UserRequestDto: OAuth2UserRequestDto): ResponseEntity<Void> {
        val tokenHeader = authenticationService.registers(oAuth2UserRequestDto)
        return ResponseEntity.ok().headers(tokenHeader).build()
    }

    @GetMapping
    fun getUserInfo(authentication: Authentication): ResponseEntity<UserResponseDto> {
        val user = userService.getUserInfo(authentication)
        return ResponseEntity.ok(responseDtoManager.generateUserResponseDto(user))
    }

    @PatchMapping
    fun updateUser(
        authentication: Authentication,
        @RequestBody userRequestDto: UserRequestDto
    ): ResponseEntity<UserResponseDto> {
        val user = userService.updateUser(authentication, userRequestDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDtoManager.generateUserResponseDto(user))
    }

    @DeleteMapping
    fun deleteUser(authentication: Authentication): ResponseEntity<Void> {
        userService.deleteUser(authentication)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/logout")
    fun logout(
        authentication: Authentication,
        @RequestBody fcmTokenRequestDto: FCMTokenRequestDto
    ): ResponseEntity<Void> {
        userService.logoutUser(authentication, fcmTokenRequestDto)
        return ResponseEntity.ok().build()
    }
}