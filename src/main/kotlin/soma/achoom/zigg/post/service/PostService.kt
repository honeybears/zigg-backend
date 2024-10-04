package soma.achoom.zigg.post.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.board.repository.BoardRepository
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.entity.Video
import soma.achoom.zigg.content.repository.ImageRepository
import soma.achoom.zigg.content.repository.VideoRepository
import soma.achoom.zigg.post.dto.PostRequestDto
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.exception.PostCreatorMismatchException
import soma.achoom.zigg.post.repository.PostRepository
import soma.achoom.zigg.user.service.UserService

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userService: UserService,
    private val imageRepository: ImageRepository,
    private val videoRepository: VideoRepository,
    private val boardRepository: BoardRepository
) {
    fun createPost(authentication: Authentication, boardId:Long, postRequestDto: PostRequestDto): Post {
        val user = userService.authenticationToUser(authentication)
        val board = boardRepository.findById(boardId).orElseThrow { IllegalArgumentException("Board not found") }
        val post = Post(
            title = postRequestDto.postTitle,
            textContent = postRequestDto.postMessage,
            imageContents = postRequestDto.postImageContent.map {
                val image = Image(
                    uploader = user,
                    imageKey = it.split("?")[0].split("/")
                        .subList(3, it.split("?")[0].split("/").size).joinToString("/")
                )
                imageRepository.save(image)
            }.toMutableSet(),
            videoContent = postRequestDto.postVideoContent?.let {
                val video = Video(
                    uploader = user,
                    videoKey = it.videoKey.split("?")[0].split("/")
                        .subList(3, it.videoKey.split("?")[0].split("/").size).joinToString("/"),
                    duration = it.videoDuration

                )
                videoRepository.save(video)
            },
            board = board,
            creator = user,


        )
        return postRepository.save(post)
    }

    fun getPosts(authentication: Authentication, page:Int): Page<Post> {
        val sort = Sort.by(Sort.Order.desc("createdAt"))
        return postRepository.findAll(PageRequest.of(page, 10, sort))
    }

    fun getPost(authentication:Authentication, boardId:Long, postId: Long): Post {
        return postRepository.findById(postId).orElseThrow { IllegalArgumentException("Post not found") }
    }

    fun searchPosts (authentication: Authentication, boardId :Long ,keyword: String, page:Int, ): Page<Post> {
        val sort = Sort.by(Sort.Order.desc("createdAt"))
        val board = boardRepository.findById(boardId).orElseThrow { IllegalArgumentException("Board not found") }
        return postRepository.findPostsByBoardAndTitleContaining(board, keyword, PageRequest.of(page, 10, sort))
    }

    fun updatePost(authentication: Authentication, postId:Long, postRequestDto:PostRequestDto) : Post{
        val user = userService.authenticationToUser(authentication)

        val post = postRepository.findById(postId).orElseThrow { IllegalArgumentException("Post not found") }

        if(post.creator.userId != user.userId){
            throw PostCreatorMismatchException()
        }

        post.title = postRequestDto.postTitle
        post.textContent = postRequestDto.postMessage
        post.imageContents = postRequestDto.postImageContent.map {
            val image = Image(
                uploader = post.creator,
                imageKey = it.split("?")[0].split("/")
                    .subList(3, it.split("?")[0].split("/").size).joinToString("/")
            )
            imageRepository.save(image)
        }.toMutableSet()
        post.videoContent = postRequestDto.postVideoContent?.let {
            val video = Video(
                uploader = post.creator,
                videoKey = it.videoKey.split("?")[0].split("/")
                    .subList(3, it.videoKey.split("?")[0].split("/").size).joinToString("/"),
                duration = it.videoDuration

            )
            videoRepository.save(video)
        }

        return postRepository.save(post)
    }

    fun deletePost(authentication: Authentication, postId: Long){
        val user = userService.authenticationToUser(authentication)
        val post = postRepository.findById(postId).orElseThrow { IllegalArgumentException("Post not found") }
        if(post.creator.userId != user.userId){
            throw PostCreatorMismatchException()
        }

        postRepository.delete(post)
    }
}