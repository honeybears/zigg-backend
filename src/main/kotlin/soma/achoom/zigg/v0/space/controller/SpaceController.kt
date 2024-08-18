package soma.achoom.zigg.v0.space.controller

import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import soma.achoom.zigg.v0.space.dto.SpaceRequestDto
import soma.achoom.zigg.v0.space.dto.SpaceResponseDto
import soma.achoom.zigg.v0.space.service.SpaceService
import java.util.*

@RestController
@RequestMapping("/api/v0/spaces")
class SpaceController @Autowired constructor(
    private val spaceService: SpaceService,
) {

    @GetMapping
    fun getSpaces(authentication:Authentication) : ResponseEntity<List<SpaceResponseDto>> {
        val spaceListResponse = spaceService.getSpaces(authentication)
        return ResponseEntity.ok(spaceListResponse)
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createSpace(
        authentication:Authentication,
        @RequestPart(value = "space_image", required = false) spaceImage:MultipartFile? ,
        @Valid @RequestPart(value = "spaceRequestDto") spaceRequestDto: SpaceRequestDto
    ) : ResponseEntity<SpaceResponseDto> {
        val spaceResponseDto = spaceService.createSpace(authentication, spaceImage, spaceRequestDto)
        return ResponseEntity.ok(spaceResponseDto)
    }
    @GetMapping("/{spaceId}")
    fun getSpace(authentication:Authentication, @PathVariable spaceId:UUID) : ResponseEntity<SpaceResponseDto> {
        val spaceResponseDto = spaceService.getSpace(authentication, spaceId)
        return ResponseEntity.ok(spaceResponseDto)
    }
    @PatchMapping("/{spaceId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateSpace(
        authentication:Authentication, @PathVariable spaceId:UUID,
        @RequestPart("space_image", required = false) spaceImage:MultipartFile? ,
        @Valid @RequestPart("spaceRequestDto", required = true) spaceRequestDto: SpaceRequestDto
    ) : ResponseEntity<SpaceResponseDto> {
        val spaceResponseDto = spaceService.updateSpace(authentication, spaceId, spaceImage,spaceRequestDto)
        return ResponseEntity.ok(spaceResponseDto)
    }
    @DeleteMapping("/{spaceId}")
    fun deleteSpace(authentication:Authentication, @PathVariable spaceId:UUID) : ResponseEntity<Unit> {
        spaceService.deleteSpace(authentication, spaceId)
        return ResponseEntity.ok().build()
    }
}