package soma.achoom.zigg

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import soma.achoom.zigg.firebase.config.FCMConfig
import soma.achoom.zigg.firebase.service.FCMService
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.s3.service.S3Service
import kotlin.test.BeforeTest

@TestConfiguration
class TestConfig {

    @MockBean
    lateinit var s3Service: S3Service

    @MockBean
    lateinit var fcmService: FCMService

    @MockBean
    lateinit var fcmConfig: FCMConfig

    lateinit var responseDtoManager: ResponseDtoManager

    @Bean
    fun responseDtoManager(s3Service: S3Service): ResponseDtoManager {
        return ResponseDtoManager(s3Service)
    }

    companion object {
        const val SPACE_IMAGE_KEY = "space-image-key"
        const val PROFILE_IMAGE_KEY = "profile-image-key"
        const val SPACE_IMAGE_URL = "http://example.com/space-image"
        const val PROFILE_IMAGE_URL = "http://example.com/profile-image"
        const val HISTORY_VIDEO_KEY = "history-video-key"
        const val HISTORY_VIDEO_URL = "http://example.com/history-video"
        const val HISTORY_VIDEO_THUMBNAIL_KEY = "history-video-thumbnail-key"
        const val HISTORY_VIDEO_THUMBNAIL_URL = "http://example.com/history-video-thumbnail"
    }
}
