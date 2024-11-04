package soma.achoom.zigg.repository

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.board.entity.Board
import soma.achoom.zigg.board.repository.BoardRepository
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.repository.PostRepository
import java.lang.Thread.sleep
import kotlin.test.Test

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class BoardRepositoryTest {
    @Autowired
    private lateinit var postRepository: PostRepository
    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil

    @Autowired
    private lateinit var boardRepository: BoardRepository

    @BeforeEach
    fun setup() {
        val user = dummyDataUtil.createDummyUser()
        val board = Board(
            name = "board",
        )
        for (i in 1..10) {
            postRepository.save(
                Post(
                    creator = user,
                    title = "title$i",
                    textContent = "content$i",
                    board = board

                )
            )

        }
        boardRepository.save(board)
    }


}