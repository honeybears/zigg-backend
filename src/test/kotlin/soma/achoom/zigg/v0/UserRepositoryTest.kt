package soma.achoom.zigg.v0

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import soma.achoom.zigg.v0.model.User
import soma.achoom.zigg.v0.repository.UserRepository

@SpringBootTest
class UserRepositoryTest @Autowired constructor(
    private val userRepository: UserRepository
)  {


//    @Test
//    fun creatUser(){
//        val user:User = User(userNickname = "test", email = "test@test.com", userName = "test")
//        userRepository.save(user)
//        var user2 = userRepository.findByEmail("test@test.com")
//        println(user.toString())
//
//    }

}