package soma.achoom.zigg.v0

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import soma.achoom.zigg.v0.auth.OAuthProviderEnum
import soma.achoom.zigg.v0.model.User
import soma.achoom.zigg.v0.model.enums.UserRole
import soma.achoom.zigg.v0.repository.UserRepository
import kotlin.test.Test

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest @Autowired constructor(
    private val userRepository: UserRepository
)  {

    @Test
    fun creatUser(){
        val user = User(
            userId = null,
            userNickname = "test",
            userName = "test",
            providerId = "test",
            role = UserRole.USER,
            provider = OAuthProviderEnum.TEST,
        )
        userRepository.save(user)
        val user2 = userRepository.findUserByUserNickname("test")?: throw IllegalArgumentException("user not found")
        assertThat(user2.userName).isEqualTo(user.userName)
    }

}