package soma.achoom.zigg.v0.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import soma.achoom.zigg.v0.dto.request.SpaceRequestDto
import soma.achoom.zigg.v0.dto.response.SpaceResponseDto
import soma.achoom.zigg.v0.model.enums.GCSDataType
import soma.achoom.zigg.v0.service.GCSService
import soma.achoom.zigg.v0.service.SpaceService
import java.util.UUID

@RestController
@RequestMapping("/api/v0/spaces")
class SpaceController @Autowired constructor(
    private val spaceService: SpaceService,
    private val gcsService: GCSService
) {
    @GetMapping("/pre-signed-url")
    fun getPreSignedUrl() : ResponseEntity<String> {
        val preSignedUrl = gcsService.getPreSignedPutUrl(GCSDataType.SPACE_IMAGE, UUID.randomUUID())
        return ResponseEntity.ok(preSignedUrl)
    }

    @GetMapping
    fun getSpaces(authentication:Authentication) : ResponseEntity<List<SpaceResponseDto>> {
        val spaceListResponse = spaceService.getSpaces(authentication)
        return ResponseEntity.ok(spaceListResponse)
    }

    @PostMapping
    fun createSpace(
        authentication:Authentication,
        @RequestPart("space_image", required = false) spaceImage:MultipartFile? ,
        @RequestPart("spaceRequestDto") spaceRequestDto: SpaceRequestDto
    ) : ResponseEntity<SpaceResponseDto> {
        val spaceResponseDto = spaceService.createSpace(authentication, spaceImage, spaceRequestDto)
        return ResponseEntity.ok(spaceResponseDto)
    }
    @GetMapping("/{spaceId}")
    fun getSpace(authentication:Authentication, @PathVariable spaceId:UUID) : ResponseEntity<SpaceResponseDto> {
        val spaceResponseDto = spaceService.getSpace(authentication, spaceId)
        return ResponseEntity.ok(spaceResponseDto)
    }
    @PatchMapping("/{spaceId}")
    fun updateSpace(
        authentication:Authentication, @PathVariable spaceId:UUID,
        @RequestPart("space_image", required = false) spaceImage:MultipartFile? ,
        @RequestPart("spaceRequestDto", required = true) spaceRequestDto: SpaceRequestDto
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