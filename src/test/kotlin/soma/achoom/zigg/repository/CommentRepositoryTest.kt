package soma.achoom.zigg.repository

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.board.entity.Board
import soma.achoom.zigg.board.repository.BoardRepository
import soma.achoom.zigg.comment.entity.Comment
import soma.achoom.zigg.comment.repository.CommentRepository
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.repository.PostRepository
import soma.achoom.zigg.user.entity.User

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CommentRepositoryTest {
    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var boardRepository: BoardRepository

    @Autowired
    private lateinit var commentRepository: CommentRepository
    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil


    private lateinit var user : User

    @BeforeEach
    fun setUp() {
        user = dummyDataUtil.createDummyUserWithMultiFCMToken(1)
    }

    @Test
    fun `save comment test`() {
        val board = Board(
            name = "test board"
        )
        boardRepository.save(board)
        val post = Post(
            board = board,
            title = "test post",
            textContent = "test content",
            creator = user
        )
        postRepository.save(post)
        // given
        val parentComment = Comment(
            creator = user,
            textComment = "parent comment",
            replies = mutableListOf(Comment(
                creator = user,
                textComment = "child comment",
                post = post
            )),
            post = post
        )
        commentRepository.save(parentComment)


        // when
        val result = commentRepository.findAll()

        // then
        assert(result.size == 2)
    }
    @Test
    fun `delete comment test`() {
        val board = Board(
            name = "test board"
        )
        boardRepository.save(board)
        val post = Post(
            board = board,
            title = "test post",
            textContent = "test content",
            creator = user
        )
        postRepository.save(post)
        // given
        val parentComment = Comment(
            creator = user,
            textComment = "parent comment",
            replies = mutableListOf(Comment(
                creator = user,
                textComment = "child comment",
                post = post
            )),
            post = post
        )
        commentRepository.save(parentComment)

        // when
        commentRepository.delete(parentComment)

        // then
        val result = commentRepository.findAll()
        assert(result.size == 0)
    }

}