package soma.achoom.zigg.repository

import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.TestConfig
import soma.achoom.zigg.auth.dto.OAuthProviderEnum
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.history.dto.UploadContentTypeRequestDto
import soma.achoom.zigg.s3.config.S3Config
import soma.achoom.zigg.s3.service.S3DataType
import soma.achoom.zigg.s3.service.S3Service
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.repository.UserRepository
import java.util.UUID
import kotlin.test.Test

@SpringBootTest()
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `find user by username`() {

        val user = User(
            name = "test",
            nickname = "test",
            profileImageKey = Image(
                uploader = null,
                imageKey = "test"
            ),
            jwtToken = "test",
            providerId = "test",
            platform = OAuthProviderEnum.TEST
        )
        userRepository.save(user)

        val user1 = userRepository.findUsersByUserNameLike(userName = user.name!!, pageable = Pageable.ofSize(10))
        for (u in user1) {
            assert(u.name == user.name)
        }

    }
}