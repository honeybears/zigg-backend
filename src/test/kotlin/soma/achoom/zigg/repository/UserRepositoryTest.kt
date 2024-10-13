package soma.achoom.zigg.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.TestConfig
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.user.repository.UserRepository
import kotlin.test.Test

@SpringBootTest(classes = [TestConfig::class])
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil

    @Test
    fun `delete user with spaceUser`() {
        val user1 = dummyDataUtil.createDummyUserWithMultiFCMToken(1)
        val user2 = dummyDataUtil.createDummyUserWithMultiFCMToken(1)

        val space1 = dummyDataUtil.createDummySpace()
        val space2 = dummyDataUtil.createDummySpace()

        val spaceUser1 = dummyDataUtil.createDummySpaceUser(space1, user1)

        val spaceUser2 = dummyDataUtil.createDummySpaceUser(space1, user2)

        val spaceUser3 = dummyDataUtil.createDummySpaceUser(space2, user2)

        val history1 = dummyDataUtil.createDummyHistory(space1)

        val history2 = dummyDataUtil.createDummyHistory(space2)

        val feedback1 = dummyDataUtil.createDummyFeedback(history1, spaceUser1, mutableListOf(spaceUser2))

        val feedback2 = dummyDataUtil.createDummyFeedback(history1, spaceUser2, mutableListOf(spaceUser1))

        val invite = dummyDataUtil.creatDummyInvite(user1, user2, space2)

        userRepository.delete(user1)
        userRepository.delete(user2)

        assert(userRepository.findById(user1.userId).isEmpty)
    }

    @Test
    fun `find user by username`() {
        val user = dummyDataUtil.createDummyUserWithMultiFCMToken(1)

        val user1 = userRepository.findUsersByUserNameLike(userName = user.name!!, pageable = Pageable.ofSize(10))
        for (u in user1) {
            assert(u.name == user.name)
        }

    }
}