package soma.achoom.zigg.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.TestConfig.Companion.SPACE_IMAGE_URL
import soma.achoom.zigg.board.entity.Board
import soma.achoom.zigg.board.repository.BoardRepository
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.post.dto.PostRequestDto
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.repository.PostRepository
import soma.achoom.zigg.post.service.PostService
import soma.achoom.zigg.s3.service.S3Service

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostServiceTest {
    @Autowired
    private lateinit var postService: PostService

    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil

    @Autowired
    private lateinit var s3Service: S3Service
    @Autowired
    private lateinit var boardRepository: BoardRepository
    @Autowired
    private lateinit var postRepository: PostRepository

    private lateinit var board: Board

    private lateinit var post: Post

    @BeforeEach
    fun setup() {
        Mockito.`when`(s3Service.getPreSignedGetUrl(anyString())).thenReturn(SPACE_IMAGE_URL)


        val user = dummyDataUtil.createDummyUser()
        board = Board(name = "test board")
        boardRepository.save(board)

        post = Post(board = board, title = "test post", textContent = "test content", creator = user)
        postRepository.save(post)
    }

    @Test
    fun `create post`() {
        val user = dummyDataUtil.createDummyUser()
        val auth = dummyDataUtil.createDummyAuthentication(user)
        val postResponse = postService.createPost(
            auth, board.boardId!!, PostRequestDto(
                postTitle = "test post",
                postMessage = "test content",
            )
        )
        assert(postResponse.postTitle == "test post")
    }
    @Test
    fun `update post`() {
        val user = dummyDataUtil.createDummyUser()
        val auth = dummyDataUtil.createDummyAuthentication(user)
        val newPost = Post(board = board, title = "test post", textContent = "test content", creator = user)
        postRepository.save(newPost)
        val postResponse = postService.updatePost(
            auth, newPost.postId!!, PostRequestDto(
                postTitle = "update post",
                postMessage = "update content",
            )
        )
        assert(postResponse.postTitle == "update post")
        assert(postResponse.postMessage == "update content")
    }
    @Test
    fun `delete post`(){
        val user = dummyDataUtil.createDummyUser()
        val auth = dummyDataUtil.createDummyAuthentication(user)
        val newPost = Post(board = board, title = "test post", textContent = "test content", creator = user)
        postRepository.save(newPost)
        postService.deletePost(auth, newPost.postId!!)
        assert(postRepository.findById(newPost.postId!!).isEmpty)
    }
    @Test
    fun `like unlike post`(){
        val user = dummyDataUtil.createDummyUser()
        val auth = dummyDataUtil.createDummyAuthentication(user)
        postService.likeOrUnlikePost(auth, post.postId!!)
        assert(postRepository.findById(post.postId!!).get().likeCnt == 1)

        postService.likeOrUnlikePost(auth, post.postId!!)
        assert(postRepository.findById(post.postId!!).get().likeCnt == 0)
    }
    @Test
    fun `scrap unscrap post`() {
        val user = dummyDataUtil.createDummyUser()
        val auth = dummyDataUtil.createDummyAuthentication(user)
        postService.scrapOrUnscrapPost(auth, post.postId!!)
        assert(postRepository.findById(post.postId!!).get().scrapCnt == 1)

        postService.scrapOrUnscrapPost(auth, post.postId!!)
        assert(postRepository.findById(post.postId!!).get().scrapCnt == 0)
    }
    @Test
    fun `get post`(){
        val user = dummyDataUtil.createDummyUser()
        val auth = dummyDataUtil.createDummyAuthentication(user)
        val postResponse = postService.getPost(auth, board.boardId!!, post.postId!!)
        assert(postResponse.postId == post.postId)
    }
    @Test
    fun `get scraps`(){
        val user = dummyDataUtil.createDummyUser()
        val auth = dummyDataUtil.createDummyAuthentication(user)
        postService.scrapOrUnscrapPost(auth, post.postId!!)
        val scraps = postService.getMyScraps(auth)
        assert(scraps[0].postId == post.postId)
    }
    @Test
    fun `get my posts`(){
        val user = dummyDataUtil.createDummyUser()
        val auth = dummyDataUtil.createDummyAuthentication(user)
        val postResponse = postService.createPost(
            auth, board.boardId!!, PostRequestDto(
                postTitle = "test post",
                postMessage = "test content",
            )
        )
        val posts = postService.getMyPosts(auth)
        assert(posts[0].postId == postResponse.postId)
    }
    @Test
    fun `get my commented posts`(){
        val user = dummyDataUtil.createDummyUser()
        val auth = dummyDataUtil.createDummyAuthentication(user)
        val posts = postService.getCommented(auth)
        assert(posts.isEmpty())
    }
}