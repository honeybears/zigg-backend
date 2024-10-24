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
@RequestMapping("/api/v0/boards/posts")
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
            val preSignedUrl = s3Service.getPreSignedPutUrl(S3DataType.POST_VIDEO, UUID.randomUUID(), uploadContentTypeRequestDto)
            return ResponseEntity.ok(preSignedUrl)
        }
        else if (value.trim() == "image") {
            val preSignedUrl = s3Service.getPreSignedPutUrl(S3DataType.POST_IMAGE, UUID.randomUUID(), uploadContentTypeRequestDto)
            return ResponseEntity.ok(preSignedUrl)
        }
        else return ResponseEntity.badRequest().build()
    }
    @GetMapping("/{boardId}")
    fun getPosts(authentication: Authentication, @PathVariable boardId:Long,  @RequestParam("page") page: Int) : ResponseEntity<List<PostResponseDto>>{
        val posts = postService.getPosts(authentication, boardId, page)
        return ResponseEntity.ok(posts.map{responseDtoManager.generatePostShortResponseDto(it)}.toList())
    }
    @PostMapping("/{boardId}")
    fun createPost(authentication: Authentication, @PathVariable boardId:Long, @RequestBody postRequestDto: PostRequestDto) : ResponseEntity<PostResponseDto>{
        val post = postService.createPost(authentication, boardId, postRequestDto)
        return ResponseEntity.ok(responseDtoManager.generatePostResponseDto(post))
    }
    @GetMapping("/{boardId}/{postId}")
    fun getPost(authentication : Authentication, @PathVariable boardId : Long, @PathVariable postId: Long) : ResponseEntity<PostResponseDto>{
        val post = postService.getPost(authentication,boardId,postId)
        return ResponseEntity.ok(responseDtoManager.generatePostResponseDto(post))
    }
    @GetMapping("/{boardId}/{postId}/likes")
    fun likePost(authentication: Authentication, @PathVariable boardId : Long,@PathVariable postId: Long) : ResponseEntity<Unit>{
        postService.likePost(authentication, postId)
        return ResponseEntity.noContent().build()
    }
    @PatchMapping("/{boardId}/{postId}")
    fun updatePost(authentication: Authentication, @PathVariable boardId : Long,@PathVariable postId: Long, @RequestBody postRequestDto: PostRequestDto) : ResponseEntity<PostResponseDto>{
        val post = postService.updatePost(authentication, postId, postRequestDto)
        return ResponseEntity.ok(responseDtoManager.generatePostResponseDto(post))
    }
    @DeleteMapping("/{boardId}/{postId}")
    fun deletePost(authentication: Authentication, @PathVariable boardId : Long,@PathVariable postId: Long) : ResponseEntity<Unit> {
        postService.deletePost(authentication, postId)
        return ResponseEntity.ok().build()
    }
    @GetMapping("/{boardId}/search")
    fun searchPosts(authentication: Authentication,@PathVariable boardId:Long, @RequestParam("page") page: Int, @RequestParam("keyword") keyword: String) : ResponseEntity<List<PostResponseDto>>{
        val posts = postService.searchPosts(authentication, boardId, keyword, page )
        return ResponseEntity.ok(posts.map{responseDtoManager.generatePostShortResponseDto(it)}.toList())
    }
    @GetMapping("/my")
    fun getMyPosts(authentication: Authentication) : ResponseEntity<List<PostResponseDto>>{
        val posts = postService.getMyPosts(authentication)
        return ResponseEntity.ok(posts.map{responseDtoManager.generatePostShortResponseDto(it)}.toList())
    }
//    @GetMapping("/scraps")
//    fun getScraps(authentication: Authentication) : ResponseEntity<List<PostResponseDto>>{
//        val posts = postService.getScraps(authentication)
//        return ResponseEntity.ok(posts.map{responseDtoManager.generatePostShortResponseDto(it)}.toList())
//    }
}