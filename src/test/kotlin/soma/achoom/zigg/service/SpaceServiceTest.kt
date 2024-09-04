package soma.achoom.zigg.service

import io.mockk.InternalPlatformDsl.toArray
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import org.mockito.Mockito
import soma.achoom.zigg.TestConfig
import soma.achoom.zigg.TestConfig.Companion.HISTORY_VIDEO_KEY
import soma.achoom.zigg.TestConfig.Companion.HISTORY_VIDEO_THUMBNAIL_KEY
import soma.achoom.zigg.TestConfig.Companion.HISTORY_VIDEO_THUMBNAIL_URL
import soma.achoom.zigg.TestConfig.Companion.HISTORY_VIDEO_URL
import soma.achoom.zigg.TestConfig.Companion.PROFILE_IMAGE_KEY
import soma.achoom.zigg.TestConfig.Companion.PROFILE_IMAGE_URL
import soma.achoom.zigg.TestConfig.Companion.SPACE_IMAGE_KEY
import soma.achoom.zigg.TestConfig.Companion.SPACE_IMAGE_URL
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.s3.service.S3Service
import soma.achoom.zigg.space.dto.SpaceRequestDto
import soma.achoom.zigg.space.exception.SpaceNotFoundException
import soma.achoom.zigg.space.service.SpaceService
import soma.achoom.zigg.space.dto.SpaceUserRequestDto
import soma.achoom.zigg.space.entity.SpaceRole
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.repository.UserRepository
import java.util.*

@SpringBootTest(classes = [TestConfig::class])
@ActiveProfiles("test")
@Transactional
class SpaceServiceTest {

    @Autowired
    private lateinit var s3Service: S3Service

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var spaceService: SpaceService

    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil


    lateinit var admin: User
    val userList: MutableSet<User> = mutableSetOf()

    @BeforeEach
    fun setup() {

        // Mocking S3Service behavior
        Mockito.`when`(s3Service.getPreSignedGetUrl(SPACE_IMAGE_KEY)).thenReturn(SPACE_IMAGE_URL)
        Mockito.`when`(s3Service.getPreSignedGetUrl(PROFILE_IMAGE_KEY)).thenReturn(PROFILE_IMAGE_URL)
        Mockito.`when`(s3Service.getPreSignedGetUrl(HISTORY_VIDEO_KEY)).thenReturn(HISTORY_VIDEO_URL)
        Mockito.`when`(s3Service.getPreSignedGetUrl(HISTORY_VIDEO_THUMBNAIL_KEY)).thenReturn(HISTORY_VIDEO_THUMBNAIL_URL)
        admin = dummyDataUtil.createDummyUser()
        for (i in 0 until 10){
            userList.add( dummyDataUtil.createDummyUserWithMultiFCMToken(3))
        }
        userRepository.saveAll(userList)
        userRepository.save(admin)
    }

    @Test
    fun `create Space test`() {
        // given
        val adminAuth = dummyDataUtil.createDummyAuthentication(admin)
        val spaceRequestDto = SpaceRequestDto(
            spaceName = "testSpace",
            spaceImageUrl = SPACE_IMAGE_URL,
            spaceUsers = userList.map {
                SpaceUserRequestDto(
                    userNickname = it.userNickname,
                    spaceRole = SpaceRole.USER,
                    spaceUserId = null
                )
            },
            spaceId = null
        )
        val response = spaceService.createSpace(adminAuth, spaceRequestDto)
        // then
        assert(response.spaceName == spaceRequestDto.spaceName)
        println(response.toString())
    }

    @Test
    fun `update Space Test`() {
        // given
        val adminAuth = dummyDataUtil.createDummyAuthentication(admin)
        val spaceRequestDto = SpaceRequestDto(
            spaceName = "testSpace",
            spaceImageUrl = SPACE_IMAGE_URL,
            spaceUsers = userList.map {
                SpaceUserRequestDto(
                    userNickname = it.userNickname,
                    spaceRole = SpaceRole.USER,
                    spaceUserId = null
                )
            },
            spaceId = null
        )
        val response = spaceService.createSpace(adminAuth, spaceRequestDto)
        val updateSpaceRequestDto = SpaceRequestDto(
            spaceName = "updateSpace",
            spaceImageUrl = SPACE_IMAGE_URL,
            spaceUsers = userList.map {
                SpaceUserRequestDto(
                    userNickname = it.userNickname,
                    spaceRole = SpaceRole.USER,
                    spaceUserId = null
                )
            },
            spaceId = response.spaceId
        )
        // when
        val updateResponse = spaceService.updateSpace(adminAuth, response.spaceId!!, updateSpaceRequestDto)
        // then
        assert(updateResponse.spaceName == updateSpaceRequestDto.spaceName)
        println(updateResponse.toString())
    }
    @Test
    fun `delete Space Test`() {
        // given
        val adminAuth = dummyDataUtil.createDummyAuthentication(admin)
        val spaceRequestDto = SpaceRequestDto(
            spaceName = "testSpace",
            spaceImageUrl = TestConfig.SPACE_IMAGE_URL,
            spaceUsers = userList.map {
                SpaceUserRequestDto(
                    userNickname = it.userNickname,
                    spaceRole = SpaceRole.USER,
                    spaceUserId = null
                )
            },
            spaceId = null
        )
        val response = spaceService.createSpace(adminAuth, spaceRequestDto)
        // when
        spaceService.deleteSpace(adminAuth, response.spaceId!!)
        // then
        assertThrows<SpaceNotFoundException>(
            message = "존재하지 않는 공간입니다."
        ) {
            spaceService.getSpace(adminAuth,response.spaceId!!)
        }
    }
}
