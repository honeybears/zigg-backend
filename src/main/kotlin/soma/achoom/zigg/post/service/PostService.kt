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
import soma.achoom.zigg.history.repository.HistoryRepository
import soma.achoom.zigg.post.dto.PostRequestDto
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.entity.PostLike
import soma.achoom.zigg.post.entity.PostScrap
import soma.achoom.zigg.post.exception.PostCreatorMismatchException
import soma.achoom.zigg.post.repository.PostLikeRepository
import soma.achoom.zigg.post.repository.PostRepository
import soma.achoom.zigg.post.repository.PostScrapRepository
import soma.achoom.zigg.user.service.UserService

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userService: UserService,
    private val boardRepository: BoardRepository,
    private val historyRepository: HistoryRepository,
    private val postLikeRepository: PostLikeRepository,
    private val postScrapRepository: PostScrapRepository
) {
    fun createPost(authentication: Authentication, boardId:Long, postRequestDto: PostRequestDto): Post {
        val user = userService.authenticationToUser(authentication)
        val board = boardRepository.findById(boardId).orElseThrow { IllegalArgumentException("Board not found") }

        val history = postRequestDto.historyId?.let {
            historyRepository.findHistoryByHistoryId(it)
        }
        val post = Post(
            title = postRequestDto.postTitle,
            textContent = postRequestDto.postMessage,
            imageContents = postRequestDto.postImageContent.map {
                 Image.fromUrl(
                    uploader = user,
                    imageUrl = it
                )
            }.toMutableSet(),
            videoContent = postRequestDto.postVideoContent?.let {
                history?.videoKey ?:  Video.fromUrl(
                    uploader = user,
                    videoUrl = it.videoKey,
                    duration = it.videoDuration

                )

            },
            board = board,
            creator = user,
        )
        board.posts.add(post)
        return postRepository.save(post)
    }

    fun getPosts(authentication: Authentication, boardId: Long, page:Int): Page<Post> {
        val board = boardRepository.findById(boardId).orElseThrow { IllegalArgumentException("Board not found") }
        val sort = Sort.by(Sort.Order.desc("createdAt"))
        return postRepository.findPostsByBoard(board ,PageRequest.of(page, 10, sort))
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
             Image.fromUrl(
                uploader = post.creator,
                imageUrl = it
            )
        }.toMutableSet()
        post.videoContent = postRequestDto.postVideoContent?.let {
            Video.fromUrl(
                uploader = post.creator,
                videoUrl = it.videoKey,
                duration = it.videoDuration
            )
        }
        post.videoThumbnail = postRequestDto.postVideoThumbnail?.let {
            Image.fromUrl(
                uploader = post.creator,
                imageUrl = it
            )
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

    fun likePost(authentication: Authentication, postId: Long) {
        val user = userService.authenticationToUser(authentication)
        val post = postRepository.findById(postId).orElseThrow { IllegalArgumentException("Post not found") }
        val postLike = PostLike(
            post = post,
            user = user
        )
        postLikeRepository.save(postLike)
    }

    fun getMyPosts(authentication: Authentication): List<Post> {
        val user = userService.authenticationToUser(authentication)
        return postRepository.findPostsByCreator(user)
    }

    fun getScraps(authentication: Authentication): List<Post> {
        val user = userService.authenticationToUser(authentication)
        return user.scrapedPosts.map{it.post}.toList()
    }
    fun getMyScrapPosts(authentication: Authentication): List<Post> {
        val user = userService.authenticationToUser(authentication)
        return user.scrapedPosts.map{it.post}.toList()
    }
    fun scrapPost(authentication: Authentication, postId: Long) {
        val user = userService.authenticationToUser(authentication)
        val post = postRepository.findById(postId).orElseThrow { IllegalArgumentException("Post not found") }
        post.scraps.filter { it.user.userId == user.userId }.map {
            postScrapRepository.delete(it)
            return
        }
        val postScrap = PostScrap(
            post = post,
            user = user
        )
        postScrapRepository.save(postScrap)
    }

}