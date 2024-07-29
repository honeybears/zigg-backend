package soma.achoom.zigg.v0.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import soma.achoom.zigg.v0.dto.request.SpaceRequestDto
import soma.achoom.zigg.v0.dto.response.SpaceListResponse
import soma.achoom.zigg.v0.dto.response.SpaceResponseDto
import soma.achoom.zigg.v0.service.SpaceService

@RestController
@RequestMapping("/api/v0/spaces")
class SpaceController @Autowired constructor(
    private val spaceService: SpaceService
) {
    @PostMapping
    fun createSpace(authentication:Authentication, @RequestPart("spaceRequestDto") spaceRequestDto: SpaceRequestDto, @RequestPart("spaceImage") spaceImage:MultipartFile?) : ResponseEntity<SpaceResponseDto> {
        println(spaceRequestDto.spaceImage)
        val spaceResponseDto = spaceService.createSpace(authentication, spaceRequestDto,spaceImage)
        return ResponseEntity.ok(spaceResponseDto)
    }
    @GetMapping
    fun getSpaces(authentication:Authentication): ResponseEntity<SpaceListResponse> {
        val spaces = spaceService.getSpaces(authentication)
        return ResponseEntity.ok(spaces)
    }
    @GetMapping("/{spaceId}")
    fun getSpace(authentication:Authentication, @PathVariable spaceId:Long): ResponseEntity<SpaceResponseDto> {
        val space = spaceService.getSpace(authentication,spaceId)
        return ResponseEntity.ok(space)
    }
    @PatchMapping("/{spaceId}")
    fun updateSpace(authentication:Authentication, @PathVariable spaceId:Long, @RequestPart("spaceRequestDto") spaceRequestDto: SpaceRequestDto): ResponseEntity<SpaceResponseDto> {
        val space = spaceService.updateSpace(authentication,spaceId,spaceRequestDto)
        return ResponseEntity.ok(space)
    }

}