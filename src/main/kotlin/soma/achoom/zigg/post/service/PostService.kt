package soma.achoom.zigg.post.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.board.repository.BoardRepository
import soma.achoom.zigg.content.dto.VideoResponseDto
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.entity.Video
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.history.repository.HistoryRepository
import soma.achoom.zigg.post.dto.PostRequestDto
import soma.achoom.zigg.post.dto.PostResponseDto
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.entity.PostLike
import soma.achoom.zigg.post.entity.PostScrap
import soma.achoom.zigg.post.exception.PostCreatorMismatchException
import soma.achoom.zigg.post.repository.PostLikeRepository
import soma.achoom.zigg.post.repository.PostRepository
import soma.achoom.zigg.post.repository.PostScrapRepository
import soma.achoom.zigg.s3.service.S3Service
import soma.achoom.zigg.user.service.UserService

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userService: UserService,
    private val boardRepository: BoardRepository,
    private val historyRepository: HistoryRepository,
    private val postLikeRepository: PostLikeRepository,
    private val postScrapRepository: PostScrapRepository,
    private val s3Service: S3Service
) {
    fun createPost(authentication: Authentication, boardId:Long, postRequestDto: PostRequestDto): PostResponseDto {
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
        postRepository.save(post)
        return PostResponseDto(
            postId = post.postId!!,
            postTitle = post.title,
            postMessage = post.textContent,
            postImageContents = post.imageContents.map { s3Service.getPreSignedGetUrl(it.imageKey) }.toList(),
            postThumbnailImage = post.videoThumbnail?.let { s3Service.getPreSignedGetUrl(it.imageKey) },
            postVideoContent = post.videoContent?.let {
                VideoResponseDto(
                    videoUrl = s3Service.getPreSignedGetUrl(post.videoContent!!.videoKey),
                    videoDuration = it.duration
                )
            },
            likeCnt = post.likeCnt,
            scrapCnt = post.scrapCnt,
            isScraped = false,
            isLiked = false
        )
    }

    fun getPosts(authentication: Authentication, boardId: Long, page:Int): List<PostResponseDto> {
        val user = userService.authenticationToUser(authentication)
        val board = boardRepository.findById(boardId).orElseThrow { IllegalArgumentException("Board not found") }
        val sort = Sort.by(Sort.Order.desc("createdAt"))
        val posts = postRepository.findPostsByBoard(board ,PageRequest.of(page, 10, sort))
        return posts.map {
            PostResponseDto(
                postId = it.postId!!,
                postTitle = it.title,
                postMessage = it.textContent,
                postThumbnailImage = it.videoThumbnail?.let { s3Service.getPreSignedGetUrl(it.imageKey) },
                likeCnt = it.likeCnt,
                scrapCnt = it.scrapCnt,
                isScraped = postScrapRepository.existsPostScrapByPostAndUser(it, user),
                isLiked = postLikeRepository.existsPostLikeByPostAndUser(it, user)
            )
        }.toList()

    }

    fun getPost(authentication:Authentication, boardId:Long, postId: Long): PostResponseDto {
        val user = userService.authenticationToUser(authentication)
        val post =  postRepository.findById(postId).orElseThrow { IllegalArgumentException("Post not found") }

        return PostResponseDto(
            postId = post.postId!!,
            postTitle = post.title,
            postMessage = post.textContent,
            postImageContents = post.imageContents.map { s3Service.getPreSignedGetUrl(it.imageKey) }.toList(),
            postThumbnailImage = post.videoThumbnail?.let { s3Service.getPreSignedGetUrl(it.imageKey) },
            postVideoContent = post.videoContent?.let {
                VideoResponseDto(
                    videoUrl = s3Service.getPreSignedGetUrl(post.videoContent!!.videoKey),
                    videoDuration = it.duration
                )
            },
            likeCnt = post.likeCnt,
            scrapCnt = post.scrapCnt,
            isScraped = postScrapRepository.existsPostScrapByPostAndUser(post, user),
            isLiked = postLikeRepository.existsPostLikeByPostAndUser(post, user),
        )

    }

    fun searchPosts (authentication: Authentication, boardId :Long ,keyword: String, page:Int, ): List<PostResponseDto> {
        val user = userService.authenticationToUser(authentication)
        val sort = Sort.by(Sort.Order.desc("createdAt"))
        val board = boardRepository.findById(boardId).orElseThrow { IllegalArgumentException("Board not found") }
        val posts = postRepository.findPostsByBoardAndTitleContaining(board, keyword, PageRequest.of(page, 10, sort))
        return posts.map { PostResponseDto(
            postId = it.postId!!,
            postTitle = it.title,
            postMessage = it.textContent,
            postThumbnailImage = it.videoThumbnail?.let { s3Service.getPreSignedGetUrl(it.imageKey) },

            likeCnt = it.likeCnt,
            scrapCnt = it.scrapCnt,
            isScraped = postScrapRepository.existsPostScrapByPostAndUser(it, user),
            isLiked = postLikeRepository.existsPostLikeByPostAndUser(it, user)
        ) }.toList()
    }

    fun updatePost(authentication: Authentication, postId:Long, postRequestDto:PostRequestDto) : PostResponseDto{
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
        postRepository.save(post)
        return PostResponseDto(
            postId = post.postId!!,
            postTitle = post.title,
            postMessage = post.textContent,
            postImageContents = post.imageContents.map { s3Service.getPreSignedGetUrl(it.imageKey) }.toList(),
            postThumbnailImage = post.videoThumbnail?.let { s3Service.getPreSignedGetUrl(it.imageKey) },
            postVideoContent = post.videoContent?.let {
                VideoResponseDto(
                    videoUrl = s3Service.getPreSignedGetUrl(post.videoContent!!.videoKey),
                    videoDuration = it.duration
                )
            },
            likeCnt = post.likeCnt,
            scrapCnt = post.scrapCnt,
            isScraped = postScrapRepository.existsPostScrapByPostAndUser(post, user),
            isLiked = postLikeRepository.existsPostLikeByPostAndUser(post, user)
        )
    }

    fun deletePost(authentication: Authentication, postId: Long){
        val user = userService.authenticationToUser(authentication)
        val post = postRepository.findById(postId).orElseThrow { IllegalArgumentException("Post not found") }
        if(post.creator.userId != user.userId){
            throw PostCreatorMismatchException()
        }
        postRepository.delete(post)
    }

    fun likeOrUnlikePost(authentication: Authentication, postId: Long): PostResponseDto{
        val user = userService.authenticationToUser(authentication)
        val post = postRepository.findById(postId).orElseThrow { IllegalArgumentException("Post not found") }
        val postLike = postLikeRepository.findByPostAndUser(post, user)
        if(postLike == null){
            postLikeRepository.save(PostLike(user = user, post = post))
        } else {
            postLikeRepository.delete(postLike)
        }
        return PostResponseDto(
            postId = post.postId!!,
            postTitle = post.title,
            likeCnt = post.likeCnt,
            scrapCnt = post.scrapCnt,
            isScraped = postScrapRepository.existsPostScrapByPostAndUser(post, user),
            isLiked = postLikeRepository.existsPostLikeByPostAndUser(post, user)
        )
    }

    fun scrapOrUnscrapPost(authentication: Authentication, postId: Long): PostResponseDto{
        val user = userService.authenticationToUser(authentication)
        val post = postRepository.findById(postId).orElseThrow { IllegalArgumentException("Post not found") }
        val postScrap = postScrapRepository.findByPostAndUser(post, user)
        if(postScrap == null){
            postScrapRepository.save(PostScrap(user = user, post = post))
        } else {
            postScrapRepository.delete(postScrap)
        }
        return PostResponseDto(
            postId = post.postId!!,
            postTitle = post.title,
            likeCnt = post.likeCnt,
            scrapCnt = post.scrapCnt,
            isScraped = postScrapRepository.existsPostScrapByPostAndUser(post, user),
            isLiked = postLikeRepository.existsPostLikeByPostAndUser(post, user)
        )
    }

    fun getMyPosts(authentication: Authentication): List<PostResponseDto> {
        val user = userService.authenticationToUser(authentication)
        val post = postRepository.findPostsByCreator(user)
        return post.map {
            PostResponseDto(
                postId = it.postId!!,
                postTitle = it.title,
                postMessage = it.textContent,
                postThumbnailImage = it.videoThumbnail?.let { s3Service.getPreSignedGetUrl(it.imageKey) },
                likeCnt = it.likeCnt,
                scrapCnt = it.scrapCnt,
                isScraped = postScrapRepository.existsPostScrapByPostAndUser(it, user),
                isLiked = postLikeRepository.existsPostLikeByPostAndUser(it, user)
            )
        }.toList()

    }
    fun getMyScraps(authentication: Authentication): List<PostResponseDto> {
        val user = userService.authenticationToUser(authentication)
        val post = postScrapRepository.findByUser(user).map { it.post }
        return post.map {
            PostResponseDto(
                postId = it.postId!!,
                postTitle = it.title,
                postMessage = it.textContent,
                postThumbnailImage = it.videoThumbnail?.let { s3Service.getPreSignedGetUrl(it.imageKey) },
                likeCnt = it.likeCnt,
                scrapCnt = it.scrapCnt,
                isScraped = postScrapRepository.existsPostScrapByPostAndUser(it, user),
                isLiked = postLikeRepository.existsPostLikeByPostAndUser(it, user)
            )
        }.toList()
    }
    fun getMyLikes(authentication: Authentication): List<PostResponseDto> {
        val user = userService.authenticationToUser(authentication)
        val post = postLikeRepository.findByUser(user).map { it.post }
        return post.map {
            PostResponseDto(
                postId = it.postId!!,
                postTitle = it.title,
                postMessage = it.textContent,
                postThumbnailImage = it.videoThumbnail?.let { s3Service.getPreSignedGetUrl(it.imageKey) },
                likeCnt = it.likeCnt,
                scrapCnt = it.scrapCnt,
                isScraped = postScrapRepository.existsPostScrapByPostAndUser(it, user),
                isLiked = postLikeRepository.existsPostLikeByPostAndUser(it, user)
            )
        }.toList()
    }
}