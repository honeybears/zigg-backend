package soma.achoom.zigg.service

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import soma.achoom.zigg.TestConfig
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.invite.entity.Invite
import soma.achoom.zigg.invite.repository.InviteRepository
import soma.achoom.zigg.invite.service.InviteService
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.entity.SpaceRole
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.repository.UserRepository
import soma.achoom.zigg.user.service.UserService
import kotlin.test.Test

@SpringBootTest(classes = [TestConfig::class])
@ActiveProfiles("test")
class InviteServiceTest {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var spaceRepository: SpaceRepository

    @Autowired
    private lateinit var inviteRepository: InviteRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var inviteService: InviteService

    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil

    private lateinit var user: User
    private lateinit var invitee: User
    private lateinit var space: Space
    private lateinit var invite: Invite

    @BeforeEach
    fun setup() {
        user = dummyDataUtil.createDummyUser()
        invitee = dummyDataUtil.createDummyUser()
        space = dummyDataUtil.createDummySpace()
        space.spaceUsers.add(SpaceUser(
            user = user,
            space = space,
            spaceRole = SpaceRole.ADMIN
        ))
        invite = dummyDataUtil.creatDummyInvite(user, invitee, space)
        userRepository.save(user)
        userRepository.save(invitee)
        spaceRepository.save(space)
        inviteRepository.save(invite)
    }

    @Test
    fun `delete user with invite`(){
        val auth = dummyDataUtil.createDummyAuthentication(user)
        userService.deleteUser(auth)
        assert(userRepository.findById(user.userId).isEmpty)
    }

}