package soma.achoom.zigg.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.TestConfig
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.user.repository.UserRepository
import kotlin.test.Test

@SpringBootTest(
    classes = [TestConfig::class]
)
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil

    @Test
    fun `delete user with spaceUser`() {
        val user = dummyDataUtil.createDummyUserWithMultiFCMToken(1)
        val space = dummyDataUtil.createDummySpace()
        val spaceUser = dummyDataUtil.createDummySpaceUser(space, user)

        val history = dummyDataUtil.createDummyHistory(space)



        userRepository.delete(user)
    }
}