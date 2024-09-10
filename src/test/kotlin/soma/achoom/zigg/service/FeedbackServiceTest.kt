package soma.achoom.zigg.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.TestConfig
import soma.achoom.zigg.feedback.service.FeedbackService

@SpringBootTest(
    classes = [TestConfig::class]
)
@ActiveProfiles("test")
@Transactional
class FeedbackServiceTest {
    @Autowired
    private lateinit var feedbackService: FeedbackService

}