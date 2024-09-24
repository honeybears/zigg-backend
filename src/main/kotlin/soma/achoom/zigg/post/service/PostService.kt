package soma.achoom.zigg.post.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import soma.achoom.zigg.content.repository.ImageRepository
import soma.achoom.zigg.content.repository.VideoRepository
import soma.achoom.zigg.history.repository.HistoryRepository
import soma.achoom.zigg.post.dto.PostRequestDto
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.repository.PostRepository
import soma.achoom.zigg.user.service.UserService

@Service
class PostService(
    private val postRepository: PostRepository,
    private val historyRepository: HistoryRepository,
    private val userService: UserService,
    private val imageRepository: ImageRepository,
    private val videoRepository: VideoRepository
) {

    fun getPosts(authentication: Authentication): Page<Post> {
        return postRepository.findAll(page = Pageable.ofSize(10))
    }

    fun getPost(authentication:Authentication, postId: Long): Post {
        return postRepository.findById(postId).orElseThrow { IllegalArgumentException("Post not found") }
    }

    fun searchPosts (authentication: Authentication, keyword: String): Page<Post> {
        return postRepository.findPostsByPostTitleContaining(keyword, Pageable.ofSize(10))
    }
}