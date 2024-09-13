package soma.achoom.zigg.repository

import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.TestConfig
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.space.repository.SpaceUserRepository
import soma.achoom.zigg.user.repository.UserRepository
import java.util.*
import kotlin.test.Test
import kotlin.test.assertFails

@SpringBootTest(classes = [TestConfig::class])
@ActiveProfiles("test")
@Transactional
class SpaceUserRepositoryTest {
    @Autowired
    private lateinit var spaceRepository: SpaceRepository

    @Autowired
    private lateinit var spaceUserRepository: SpaceUserRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil
    @Test
    fun `delete with spaceUser`() {
        val space = Space(
            spaceId = UUID.randomUUID(),
            spaceName = "spaceName",
            spaceImageKey = TestConfig.SPACE_IMAGE_KEY
        )
        spaceRepository.save(space)

        val user = dummyDataUtil.createDummyUserWithMultiFCMToken(1)
        userRepository.save(user)
        val spaceUser = SpaceUser(
            space = space,
            user = user
        )
        space.spaceUsers.add(
            spaceUser
        )
        spaceRepository.save(space)
        user.spaces.add(spaceUser)
        userRepository.save(user)
        spaceUser.user=null
        spaceUserRepository.save(spaceUser)
        userRepository.delete(user)

        assert(userRepository.findById(user.userId).isEmpty)
        assert(spaceUserRepository.findById(spaceUser.spaceUserId).isPresent)
        println(spaceUserRepository.findById(spaceUser.spaceUserId).get().user)

    }

}