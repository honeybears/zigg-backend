package soma.achoom.zigg.space.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import soma.achoom.zigg.history.dto.UploadContentTypeRequestDto
import soma.achoom.zigg.s3.service.S3DataType
import soma.achoom.zigg.s3.service.S3Service
import soma.achoom.zigg.space.dto.SpaceReferenceUrlRequestDto
import soma.achoom.zigg.space.dto.SpaceRequestDto
import soma.achoom.zigg.space.dto.SpaceResponseDto
import soma.achoom.zigg.space.service.SpaceService
import java.util.*

@RestController
@RequestMapping("/api/v0/spaces")
class SpaceController @Autowired constructor(
    private val spaceService: SpaceService,
    private val s3Service: S3Service,
) {
    @PostMapping("/pre-signed-url")
    fun getPreSignUrl(@RequestBody uploadContentTypeRequestDto: UploadContentTypeRequestDto) : ResponseEntity<String> {
        val preSignUrl = s3Service.getPreSignedPutUrl(S3DataType.SPACE_IMAGE,UUID.randomUUID(),uploadContentTypeRequestDto)
        return ResponseEntity.ok(preSignUrl)
    }

    @GetMapping
    fun getSpaces(authentication:Authentication) : ResponseEntity<List<SpaceResponseDto>> {
        val spaceListResponse = spaceService.getSpaces(authentication)
        return ResponseEntity.ok(spaceListResponse)
    }

    @PostMapping
    fun createSpace(
        authentication:Authentication,
        @RequestBody spaceRequestDto: SpaceRequestDto
    ) : ResponseEntity<SpaceResponseDto> {
        val spaceResponseDto = spaceService.createSpace(authentication, spaceRequestDto)
        return ResponseEntity.ok(spaceResponseDto)
    }

    @GetMapping("/{spaceId}")
    fun getSpace(authentication:Authentication, @PathVariable spaceId:UUID) : ResponseEntity<SpaceResponseDto> {
        val spaceResponseDto = spaceService.getSpace(authentication, spaceId)
        return ResponseEntity.ok(spaceResponseDto)
    }

    @PatchMapping("/{spaceId}")
    fun updateSpace(authentication:Authentication,@PathVariable spaceId:UUID, @RequestBody spaceRequestDto: SpaceRequestDto) : ResponseEntity<SpaceResponseDto> {
        val spaceResponseDto = spaceService.updateSpace(authentication,spaceId, spaceRequestDto)
        return ResponseEntity.ok(spaceResponseDto)
    }

    @DeleteMapping("/{spaceId}")
    fun deleteSpace(authentication:Authentication, @PathVariable spaceId:UUID) : ResponseEntity<Unit> {
        spaceService.deleteSpace(authentication, spaceId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/reference/{spaceId}")
    fun addReferenceUrl(authentication:Authentication, @PathVariable spaceId:UUID, @RequestBody spaceReferenceUrlRequestDto: SpaceReferenceUrlRequestDto) : ResponseEntity<SpaceResponseDto> {
        val spaceResponseDto = spaceService.addReferenceUrl(authentication, spaceId, spaceReferenceUrlRequestDto)
        return ResponseEntity.ok(spaceResponseDto)
    }
}