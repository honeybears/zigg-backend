package soma.achoom.zigg.repository

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.TestConfig
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.feedback.repository.FeedbackRepository
import soma.achoom.zigg.user.entity.User

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FeedbackRepositoryTest {
    @Autowired
    private lateinit var feedbackRepository: FeedbackRepository
    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil

    private lateinit var user : User

    @BeforeEach
    fun setUp() {
        user = dummyDataUtil.createDummyUserWithMultiFCMToken(1)
    }

}