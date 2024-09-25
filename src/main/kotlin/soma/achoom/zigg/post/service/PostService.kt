package soma.achoom.zigg.post.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.entity.Video
import soma.achoom.zigg.content.repository.ImageRepository
import soma.achoom.zigg.content.repository.VideoRepository
import soma.achoom.zigg.history.repository.HistoryRepository
import soma.achoom.zigg.post.dto.PostRequestDto
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.exception.PostCreatorMismatchException
import soma.achoom.zigg.post.repository.PostRepository
import soma.achoom.zigg.user.service.UserService
import java.util.UUID

@Service
class PostService(
    private val postRepository: PostRepository,
    private val historyRepository: HistoryRepository,
    private val userService: UserService,
    private val imageRepository: ImageRepository,
    private val videoRepository: VideoRepository
) {
    fun createPost(authentication: Authentication, postRequestDto: PostRequestDto): Post {
        val user = userService.authenticationToUser(authentication)

        val post = Post(
            postTitle = postRequestDto.postTitle,
            postMessage = postRequestDto.postMessage,
            postImageContent = postRequestDto.postImageContent.map {
                val image = Image(
                    imageUploader = user,
                    imageKey = it.split("?")[0].split("/")
                        .subList(3, it.split("?")[0].split("/").size).joinToString("/")
                )
                imageRepository.save(image)
            }.toMutableSet(),
            postVideoContent = postRequestDto.postVideoContent.map {
                val video = Video(
                    videoUploader = user,
                    videoKey = it.videoKey.split("?")[0].split("/")
                        .subList(3, it.videoKey.split("?")[0].split("/").size).joinToString("/"),
                    videoDuration = it.videoDuration

                )
                videoRepository.save(video)
            }.toMutableSet(),
            postCreator = user
        )
        return postRepository.save(post)
    }

    fun getPosts(authentication: Authentication, page:Int): Page<Post> {
        val sort = Sort.by(Sort.Order.desc("createdAt"))
        return postRepository.findAll(PageRequest.of(page, 10, sort))
    }

    fun getPost(authentication:Authentication, postId: Long): Post {
        return postRepository.findById(postId).orElseThrow { IllegalArgumentException("Post not found") }
    }

    fun searchPosts (authentication: Authentication, page:Int, keyword: String): Page<Post> {
        val sort = Sort.by(Sort.Order.desc("createdAt"))
        return postRepository.findPostsByPostTitleContaining(keyword, PageRequest.of(page, 10, sort))
    }

    fun updatePost(authentication: Authentication, postRequestDto:PostRequestDto, postId:Long){
        val user = userService.authenticationToUser(authentication)

        val post = postRepository.findById(postId).orElseThrow { IllegalArgumentException("Post not found") }

        if(post.postCreator.userId != user.userId){
            throw PostCreatorMismatchException()
        }

        post.postTitle = postRequestDto.postTitle
        post.postMessage = postRequestDto.postMessage
        post.postImageContent = postRequestDto.postImageContent.map {
            val image = Image(
                imageUploader = post.postCreator,
                imageKey = it.split("?")[0].split("/")
                    .subList(3, it.split("?")[0].split("/").size).joinToString("/")
            )
            imageRepository.save(image)
        }.toMutableSet()
        post.postVideoContent = postRequestDto.postVideoContent.map {
            val video = Video(
                videoUploader = post.postCreator,
                videoKey = it.videoKey.split("?")[0].split("/")
                    .subList(3, it.videoKey.split("?")[0].split("/").size).joinToString("/"),
                videoDuration = it.videoDuration

            )
            videoRepository.save(video)
        }.toMutableSet()

        postRepository.save(post)
    }

    fun deletePost(authentication: Authentication, postId: Long){
        val user = userService.authenticationToUser(authentication)
        val post = postRepository.findById(postId).orElseThrow { IllegalArgumentException("Post not found") }

        if(post.postCreator.userId != user.userId){
            throw PostCreatorMismatchException()
        }

        postRepository.delete(post)
    }
}