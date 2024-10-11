package soma.achoom.zigg.repository

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.TestConfig
import soma.achoom.zigg.board.entity.Board
import soma.achoom.zigg.board.repository.BoardRepository
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.repository.PostRepository
import kotlin.test.Test

@SpringBootTest(classes = [TestConfig::class])
@Transactional
@ActiveProfiles("test")
class PostRepositoryTest {
    @Autowired
    private lateinit var boardRepository: BoardRepository

    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil

    @Autowired
    private lateinit var postRepository: PostRepository

    @BeforeEach
    fun setUp() {
        val user = dummyDataUtil.createDummyUser()
        val board = Board(
            name = "board",
        )
        for (i in 1..10) {
            board.posts.add(
                postRepository.save(
                    Post(
                        creator = user,
                        likes = i.toLong(),
                        title = "title$i",
                        textContent = "content$i",
                        board = board

                    )
                )
            )
        }
        boardRepository.save(board)
    }

    @Test
    fun `hottest post while 3 days`() {
        val posts = postRepository.findBestPosts(Pageable.ofSize(2))
        assert(posts.size == 2)
        assert(posts[0].likes == 10L)
        assert(posts[1].likes == 9L)
    }

    @Test
    fun `delete post`() {
        val post = postRepository.findAll()[0]
//        post.detach()
        postRepository.delete(post)
        assert(postRepository.findAll().size == 9)
    }
}