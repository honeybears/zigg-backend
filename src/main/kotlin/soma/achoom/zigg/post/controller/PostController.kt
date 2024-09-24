package soma.achoom.zigg.post.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import soma.achoom.zigg.history.dto.UploadContentTypeRequestDto
import soma.achoom.zigg.s3.service.S3DataType
import soma.achoom.zigg.s3.service.S3Service
import java.util.*

@RestController
@RequestMapping("/api/v0/posts")
class PostController(private val s3Service: S3Service) {
    @PostMapping("/pre-signed-url/{value}")
    fun getPreSignedUrl(
        @RequestBody uploadContentTypeRequestDto: UploadContentTypeRequestDto, @PathVariable value: String
    ): ResponseEntity<String> {
        if (value.trim() == "video") {
            val preSignedUrl =
                s3Service.getPreSignedPutUrl(S3DataType.POST_VIDEO, UUID.randomUUID(), uploadContentTypeRequestDto)
            return ResponseEntity.ok(preSignedUrl)
        }
        else if (value.trim() == "image") {
            val preSignedUrl = s3Service.getPreSignedPutUrl(S3DataType.POST_IMAGE, UUID.randomUUID(), uploadContentTypeRequestDto)
            return ResponseEntity.ok(preSignedUrl)
        }
        else return ResponseEntity.badRequest().build()
    }

}