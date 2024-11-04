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
import soma.achoom.zigg.comment.dto.CommentRequestDto
import soma.achoom.zigg.comment.service.CommentService
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.repository.PostRepository
import soma.achoom.zigg.post.service.PostService
import soma.achoom.zigg.s3.service.S3Service

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CommentServiceTest {
    @Autowired
    private lateinit var postService: PostService

    @Autowired
    private lateinit var commentService: CommentService

    @Autowired
    private lateinit var boardRepository: BoardRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil

    @Autowired
    private lateinit var s3Service: S3Service

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
    fun `create comment`(){
        val user1 = dummyDataUtil.createDummyUser()
        val auth1 = dummyDataUtil.createDummyAuthentication(user1)
        val first = commentService.createComment(auth1, board.boardId!!, post.postId!!, CommentRequestDto("Test Comment"))
        assert(first.commentMessage == "Test Comment")
        println(post.commentCnt)

        assert(post.commentCnt == 1)
    }
    @Test
    fun `create child comment`(){
        val user1 = dummyDataUtil.createDummyUser()
        val auth1 = dummyDataUtil.createDummyAuthentication(user1)
        val first = commentService.createComment(auth1, board.boardId!!, post.postId!!, CommentRequestDto("Test Comment"))
        val user2 = dummyDataUtil.createDummyUser()
        val auth2 = dummyDataUtil.createDummyAuthentication(user2)
        val second = commentService.createChildComment(auth2, board.boardId!!, post.postId!!, first.commentId!!, CommentRequestDto("Test Child Comment"))
        assert(second.commentMessage == "Test Child Comment")
        assert(second.parentComment?.commentId == first.commentId)
        println(post.commentCnt)
        assert(post.commentCnt == 2)
    }
    @Test
    fun `comment like test`(){
        val user1 = dummyDataUtil.createDummyUser()
        val auth1 = dummyDataUtil.createDummyAuthentication(user1)
        val first = commentService.createComment(auth1, board.boardId!!, post.postId!!, CommentRequestDto("Test Comment"))

        val user2 = dummyDataUtil.createDummyUser()
        val auth2 = dummyDataUtil.createDummyAuthentication(user2)
        val second = commentService.createChildComment(auth2, board.boardId!!, post.postId!!, first.commentId!!, CommentRequestDto("Test Child Comment"))
        commentService.likeComment(auth1,board.boardId!!, post.postId!!,first.commentId!!)

        commentService.likeComment(auth1,board.boardId!!, post.postId!!,second.commentId!!)
        println(post.commentCnt)

        assert(post.commentCnt == 2)
        postService.getPost(auth1,board.boardId!!,post.postId!!).let {
            it.comments.forEach { comment ->
                if(comment.commentId == first.commentId){
                    assert(comment.commentLike == 1)
                }
                if(comment.commentId == second.commentId){
                    assert(comment.commentLike == 1)
                }
            }
        }
    }
}