package soma.achoom.zigg.service

import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
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
import soma.achoom.zigg.feedback.entity.FeedbackRecipient
import soma.achoom.zigg.feedback.repository.FeedbackRepository
import soma.achoom.zigg.firebase.dto.FCMTokenRequestDto
import soma.achoom.zigg.firebase.repository.FCMRepository
import soma.achoom.zigg.firebase.service.FCMService
import soma.achoom.zigg.history.repository.HistoryRepository
import soma.achoom.zigg.s3.service.S3Service
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.space.repository.SpaceUserRepository
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.repository.UserRepository
import soma.achoom.zigg.user.service.UserService
import java.util.UUID
import kotlin.test.Test

@SpringBootTest(classes = [TestConfig::class])
@ActiveProfiles("test")
@Transactional
class UserServiceTest {
    @Autowired
    private lateinit var spaceUserRepository: SpaceUserRepository

    @Autowired
    private lateinit var historyRepository: HistoryRepository

    @Autowired
    private lateinit var feedbackRepository: FeedbackRepository

    @Autowired
    private lateinit var spaceRepository: SpaceRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var fcmRepository: FCMRepository

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var fcmService: FCMService

    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil
    private lateinit var userWithFCM: User

    @Autowired
    private lateinit var s3Service: S3Service

    private lateinit var authentication: Authentication

    @BeforeEach
    fun setup() {

        Mockito.`when`(s3Service.getPreSignedGetUrl(SPACE_IMAGE_KEY)).thenReturn(SPACE_IMAGE_URL)
        Mockito.`when`(s3Service.getPreSignedGetUrl(PROFILE_IMAGE_KEY)).thenReturn(PROFILE_IMAGE_URL)
        Mockito.`when`(s3Service.getPreSignedGetUrl(HISTORY_VIDEO_KEY)).thenReturn(HISTORY_VIDEO_URL)
        Mockito.`when`(s3Service.getPreSignedGetUrl(HISTORY_VIDEO_THUMBNAIL_KEY))
            .thenReturn(HISTORY_VIDEO_THUMBNAIL_URL)

        userWithFCM = dummyDataUtil.createDummyUserWithMultiFCMToken(3)
        authentication = dummyDataUtil.createDummyAuthentication(userWithFCM)
        userRepository.save(userWithFCM)
    }

    @Test
    fun `Multi fcm token user`() {
        userService.registerToken(authentication, FCMTokenRequestDto(UUID.randomUUID().toString()))
        println(userWithFCM.deviceTokens.size)
        userWithFCM = userRepository.findUserByUserId(userWithFCM.userId)!!
        assert(userWithFCM.deviceTokens.size == 4)
    }

    @Test
    fun `Logout user`() {
        val fcmToken = userWithFCM.deviceTokens.first()
        println("Delete token: ${fcmToken.token}")

        // Logout 호출
        userService.logoutUser(authentication, FCMTokenRequestDto(fcmToken.token))
        println(userWithFCM.deviceTokens.size)
        userWithFCM = userRepository.findUserByUserId(userWithFCM.userId)!!
        assert(userWithFCM.deviceTokens.size == 2)
    }

//    @Test
//    fun `Delete user without spaceUser`(){
//        val space = dummyDataUtil.createDummySpace()
//        spaceRepository.save(space)
//        val spaceUser = dummyDataUtil.createDummySpaceUser(space, userWithFCM)
//        userWithFCM.spaces.add(spaceUser)
//        userWithFCM = userRepository.save(userWithFCM)
//
//        userService.deleteUser(authentication)
//        assert(userRepository.findUserByUserId(userWithFCM.userId) == null)
//        assert(fcmRepository.findAll().size == 0)
//    }


    @Test
    fun `Delete user`() {
        userService.deleteUser(authentication)
        assert(userRepository.findUserByUserId(userWithFCM.userId) == null)
        assert(fcmRepository.findAll().size == 0)
    }

//    @Test
//    fun `Delete user with complex`() {
//        val space = dummyDataUtil.createDummySpace()
//        spaceRepository.save(space)
//        val spaceUser = dummyDataUtil.createDummySpaceUser(space, userWithFCM)
//        spaceUserRepository.save(spaceUser)
//        userWithFCM.spaces.add(spaceUser)
//        userWithFCM = userRepository.save(userWithFCM)
//        val history = dummyDataUtil.createDummyHistory(space)
//        historyRepository.save(history)
//        val feedback = dummyDataUtil.createDummyFeedback(spaceUser, history)
//        feedback.recipients.add(
//            FeedbackRecipient(
//                feedback = feedback,
//                recipient = spaceUser
//
//            )
//        )
//        feedbackRepository.save(feedback)
//        userService.deleteUser(authentication)
//        assert(userRepository.findUserByUserId(userWithFCM.userId) == null)
//        assert(fcmRepository.findAll().size == 0)
//    }


}