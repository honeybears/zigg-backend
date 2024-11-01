package soma.achoom.zigg


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean

import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.s3.service.S3Service
import soma.achoom.zigg.space.repository.SpaceUserRepository
import soma.achoom.zigg.user.repository.UserRepository


@TestConfiguration
class TestConfig {
    @MockBean
    private lateinit var s3Service: S3Service

    @Autowired
    private lateinit var spaceUserRepository: SpaceUserRepository


    @Autowired
    private lateinit var userRepository: UserRepository

    @Bean
    fun responseDtoManager(): ResponseDtoManager {
        return ResponseDtoManager(s3Service)
    }

    companion object {
        const val SPACE_IMAGE_KEY = "space-image-key"
        const val PROFILE_IMAGE_KEY = "profile-image-key"
        const val SPACE_IMAGE_URL = "http://example.com/space-image"
        const val PROFILE_IMAGE_URL = "http://example.com/profile-image"
        const val HISTORY_VIDEO_KEY = "history-video-key"
        const val HISTORY_VIDEO_URL = "https://your-bucket-name.s3.amazonaws.com/123e4567-e89b-12d3-a456-426614174000?AWSAccessKeyId=ASIAxxxxxxxxxxxx&Expires=1648585600&Signature=abcd1234efgh5678ijkl9012mnop3456\n"
        const val HISTORY_VIDEO_THUMBNAIL_KEY = "history-video-thumbnail-key"
        const val HISTORY_VIDEO_THUMBNAIL_URL = "https://your-bucket-name.s3.amazonaws.com/123e4567-e89b-12d3-a456-426614174000?AWSAccessKeyId=ASIAxxxxxxxxxxxx&Expires=1648585600&Signature=abcd1234efgh5678ijkl9012mnop3456\n"
    }
}
