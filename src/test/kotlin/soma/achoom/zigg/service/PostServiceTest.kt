package soma.achoom.zigg.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.TestConfig.Companion.HISTORY_VIDEO_THUMBNAIL_URL
import soma.achoom.zigg.TestConfig.Companion.HISTORY_VIDEO_URL
import soma.achoom.zigg.TestConfig.Companion.PROFILE_IMAGE_URL
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
}