package soma.achoom.zigg.service

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.TestConfig
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.invite.service.InviteService
import soma.achoom.zigg.space.dto.InviteRequestDto
import soma.achoom.zigg.space.dto.SpaceUserRequestDto
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.entity.SpaceRole
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.space.repository.SpaceUserRepository
import soma.achoom.zigg.space.service.SpaceService
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.repository.UserRepository
import kotlin.test.Test

@SpringBootTest
@EntityScan("soma.achoom.zigg.*")
@ActiveProfiles("test")
@Transactional
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
        space.users.add(inviteeInSpace)
        space.users.add(spaceUser)
        spaceRepository.save(space)

        user.spaces.add(spaceUser)
        inviter.spaces.add(inviteeInSpace)

        userRepository.save(user)
        userRepository.save(inviter)

    }


}