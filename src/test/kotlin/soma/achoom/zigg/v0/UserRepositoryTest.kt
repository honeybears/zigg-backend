package soma.achoom.zigg.v0

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import soma.achoom.zigg.v0.auth.CustomOAuthProviderEnum
import soma.achoom.zigg.v0.auth.UserRole
import soma.achoom.zigg.v0.model.User
import soma.achoom.zigg.v0.repository.UserRepository

@SpringBootTest
class UserRepositoryTest @Autowired constructor(
    private val userRepository: UserRepository
)  {


    @Test
    fun creatUser(){
        val user:User = User(
            userNickname = "test",
            email = "test@test.com",
            userName = "test",
            providerId = "test",
            role = UserRole.USER,
            provider = CustomOAuthProviderEnum.GOOGLE
        )
        userRepository.save(user)
        val user2 = userRepository.findByEmail("test@test.com").get()
        assertThat(user2.userName).isEqualTo(user.userName)
    }

}