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
import soma.achoom.zigg.space.dto.InviteUsersRequestDto
import soma.achoom.zigg.space.dto.SpaceUserRequestDto
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.entity.SpaceRole
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.space.repository.SpaceUserRepository
import soma.achoom.zigg.space.service.SpaceService
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.repository.UserRepository
import soma.achoom.zigg.user.service.UserService
import kotlin.test.Test

@SpringBootTest(classes = [TestConfig::class])
@ActiveProfiles("test")
class InviteServiceTest {
    @Autowired
    private lateinit var spaceService: SpaceService

    @Autowired
    private lateinit var spaceRepository: SpaceRepository

    @Autowired
    private lateinit var spaceUserRepository: SpaceUserRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var inviteService: InviteService

    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil

    private lateinit var user: User
    private lateinit var inviter: User
    private lateinit var space: Space
    private lateinit var spaceUser: SpaceUser

    @BeforeEach
    fun setup() {
        user = dummyDataUtil.createDummyUser()
        inviter = dummyDataUtil.createDummyUser()
        space = dummyDataUtil.createDummySpace()
        spaceUser = SpaceUser(
            space = space,
            user = user,
            withdraw = true
        )
        val inviteeInSpace = SpaceUser(
            space = space,
            user = inviter,
            withdraw = false
        )
        spaceUserRepository.save(spaceUser)
        spaceUserRepository.save(inviteeInSpace)
        space.spaceUsers.add(inviteeInSpace)
        space.spaceUsers.add(spaceUser)
        spaceRepository.save(space)

        user.spaces.add(spaceUser)
        inviter.spaces.add(inviteeInSpace)

        userRepository.save(user)
        userRepository.save(inviter)

    }

    @Test
    fun `invite space after withdraw`() {
        val auth = dummyDataUtil.createDummyAuthentication(inviter)
        val result = spaceService.inviteUserToSpace(
            authentication = auth,
            spaceId = space.spaceId,
            inviteUsersRequestDto = InviteUsersRequestDto(
                spaceUsers = listOf(
                    SpaceUserRequestDto(
                        userNickname = user.userNickname,
                        spaceRole = SpaceRole.USER,
                        spaceUserId = null
                    )
                )
            )
        )
        assert(result.invites.size == 1)
        assert(result.invites.any { it.invitee.userName == user.userName })
    }

}