package soma.achoom.zigg.repository

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.entity.Video
import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.repository.SpaceRepository
import java.util.*
import kotlin.test.Test

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SpaceRepositoryTest {
    @Autowired
    private lateinit var spaceRepository: SpaceRepository

    @BeforeEach
    fun setup(){

    }

    @Test
    fun `is not n+1`() {
        val space = spaceRepository.findAll().firstOrNull() ?: return
        for (i in space.histories){
            println(i.name)
        }
    }

}