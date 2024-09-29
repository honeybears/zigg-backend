package soma.achoom.zigg.post.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.history.dto.UploadContentTypeRequestDto
import soma.achoom.zigg.post.dto.PostRequestDto
import soma.achoom.zigg.post.dto.PostResponseDto
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
    @GetMapping
    fun getPosts(authentication: Authentication, @RequestParam page: Int) : ResponseEntity<List<PostResponseDto>>{
        val posts = postService.getPosts(authentication, page)
        return ResponseEntity.ok(posts.map{responseDtoManager.generatePostResponseDto(it)}.toList())
    }
    @PostMapping
    fun createPost(authentication: Authentication, @RequestBody postRequestDto: PostRequestDto) : ResponseEntity<PostResponseDto>{
        val post = postService.createPost(authentication, postRequestDto)
        return ResponseEntity.ok(responseDtoManager.generatePostResponseDto(post))
    }
    @GetMapping("/{postId}")
    fun getPost(authentication : Authentication, @PathVariable postId: Long) : ResponseEntity<PostResponseDto>{
        val post = postService.getPost(authentication,postId)
        return ResponseEntity.ok(responseDtoManager.generatePostResponseDto(post))
    }
    @PatchMapping("/{postId}")
    fun updatePost(authentication: Authentication, @PathVariable postId: Long, @RequestBody postRequestDto: PostRequestDto) : ResponseEntity<PostResponseDto>{
        val post = postService.updatePost(authentication, postId, postRequestDto)
        return ResponseEntity.ok(responseDtoManager.generatePostResponseDto(post))
    }
    @DeleteMapping("/{postId}")
    fun deletePost(authentication: Authentication, @PathVariable postId: Long) : ResponseEntity<Unit> {
        postService.deletePost(authentication, postId)
        return ResponseEntity.ok().build()
    }
    @GetMapping("/search/{keyword}")
    fun searchPosts(authentication: Authentication, @PathVariable keyword: String, @RequestParam page: Int) : ResponseEntity<List<PostResponseDto>>{
        val posts = postService.searchPosts(authentication, page, keyword)
        return ResponseEntity.ok(posts.map{responseDtoManager.generatePostResponseDto(it)}.toList())
    }
}