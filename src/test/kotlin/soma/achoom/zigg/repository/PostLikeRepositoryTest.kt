package soma.achoom.zigg.repository

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.board.entity.Board
import soma.achoom.zigg.board.repository.BoardRepository
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.entity.PostLike
import soma.achoom.zigg.post.repository.PostLikeRepository
import soma.achoom.zigg.post.repository.PostRepository
import kotlin.test.Test

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PostLikeRepositoryTest {

    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil

    @Autowired
    private lateinit var boardRepository: BoardRepository

    @Autowired
    private lateinit var postLikeRepository: PostLikeRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @BeforeEach
    fun setUp() {
        val user = dummyDataUtil.createDummyUser()
        val board = Board(
            name = "test",
        )

        boardRepository.save(board)

        val post = Post(
            creator = user,
            title = "title",
            textContent = "content",
            board = board
        )

        postRepository.save(post)

    }

    @Test
    fun `is ok when delete post contained post likes`() {
        val user = dummyDataUtil.createDummyUser()
        val post = postRepository.findAll().first()
        val postLike = postLikeRepository.save(
            PostLike(
                post = post,
                user = user
            )
        )

        postRepository.delete(post)
        assert(postLikeRepository.findByPost(post).size == 1)
        assert(postLikeRepository.findByUser(user).size == 1)
    }


}