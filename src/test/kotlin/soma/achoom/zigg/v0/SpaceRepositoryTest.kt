package soma.achoom.zigg.v0

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import soma.achoom.zigg.v0.model.Space
import soma.achoom.zigg.v0.repository.SpaceRepository

@SpringBootTest
class SpaceRepositoryTest @Autowired constructor(private val spaceRepository: SpaceRepository) {
    @Test
    fun createSpace(){

    }
}