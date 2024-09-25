package soma.achoom.zigg.post.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.history.dto.UploadContentTypeRequestDto
import soma.achoom.zigg.post.service.PostService
import soma.achoom.zigg.s3.service.S3DataType
import soma.achoom.zigg.s3.service.S3Service
import java.util.*

@RestController
@RequestMapping("/api/v0/posts")
class PostController(
    private val s3Service: S3Service,
    private val responseDtoManager: ResponseDtoManager,
    private val postService: PostService
) {
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
    @GetMapping("/{page}")
    fun getPosts(authentication: Authentication, @PathVariable page: Int) {
        val posts = postService.getPosts(authentication, page)
    }

}