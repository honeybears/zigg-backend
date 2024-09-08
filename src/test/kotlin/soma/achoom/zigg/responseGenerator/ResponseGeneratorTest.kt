package soma.achoom.zigg.responseGenerator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import soma.achoom.zigg.auth.dto.OAuthProviderEnum
import soma.achoom.zigg.global.ResponseDtoManager
import soma.achoom.zigg.s3.service.S3Service
import soma.achoom.zigg.space.dto.SpaceResponseDto
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.space.entity.SpaceRole
import soma.achoom.zigg.user.entity.User
import java.util.*

class ResponseDtoManagerTest {

    @Mock
    private lateinit var s3Service: S3Service

    @InjectMocks
    private lateinit var responseDtoManager: ResponseDtoManager

    private lateinit var space: Space


    companion object {
        const val SPACE_IMAGE_KEY = "space-image-key"
        const val PROFILE_IMAGE_KEY = "profile-image-key"
        const val SPACE_IMAGE_URL = "http://example.com/space-image"
        const val PROFILE_IMAGE_URL = "http://example.com/profile-image"
    }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)

        val user = User(
            userId = UUID.randomUUID(),
            userName = "Test User",
            userNickname = "Test Nickname",
            profileImageKey = PROFILE_IMAGE_KEY,
            jwtToken = "1234",
            providerId = "1234",
            platform = OAuthProviderEnum.TEST,
            deviceTokens = mutableSetOf(),
            spaces = mutableSetOf(),
            invites = mutableSetOf(),
            invited = mutableSetOf(),
            profileBannerImageKey = null
        )

        val spaceUser = SpaceUser(
            spaceUserId = UUID.randomUUID(),
            spaceRole = SpaceRole.ADMIN,
            user = user,
            space = Space(
                spaceId = UUID.randomUUID(),
                spaceName = "Test Space",
                spaceImageKey = SPACE_IMAGE_KEY,
                referenceVideoUrl = "http://example.com/video",
                spaceUsers = mutableSetOf(),
                histories = mutableSetOf()
            )
        )

        space = Space(
            spaceId = UUID.randomUUID(),
            spaceName = "Test Space",
            spaceImageKey = SPACE_IMAGE_KEY,
            referenceVideoUrl = "http://example.com/video",
            spaceUsers = mutableSetOf(spaceUser),
            histories = mutableSetOf()
        )

        Mockito.`when`(s3Service.getPreSignedGetUrl(SPACE_IMAGE_KEY)).thenReturn(SPACE_IMAGE_URL)
        Mockito.`when`(s3Service.getPreSignedGetUrl(PROFILE_IMAGE_KEY)).thenReturn(PROFILE_IMAGE_URL)
    }

    @Test
    fun `test generateSpaceResponseDto`() {
        // Act
        val result: SpaceResponseDto = responseDtoManager.generateSpaceResponseDto(space)

        println(formatSpaceResponseDtoAsJson(result))
        // Assert
        assertEquals("Test Space", result.spaceName)
        assertEquals(SPACE_IMAGE_URL, result.spaceImageUrl)
        assertEquals(PROFILE_IMAGE_URL, result.spaceUsers?.first()?.profileImageUrl)
    }
}

fun formatSpaceResponseDtoAsJson(spaceResponseDto: SpaceResponseDto): String {
    val spaceUsersJson = spaceResponseDto.spaceUsers?.joinToString(", ") { user ->
        """
        {
            "userName": "${user.userName}",
            "userNickname": "${user.userNickname}",
            "spaceRole": "${user.spaceRole}",
            "profileImageUrl": "${user.profileImageUrl}"
        }
        """.trimIndent()
    }

    return """
    {
        "spaceName": "${spaceResponseDto.spaceName}",
        "spaceImageUrl": "${spaceResponseDto.spaceImageUrl}",
        "referenceVideoUrl": "${spaceResponseDto.referenceVideoUrl}",
        "spaceUsers": [
               $spaceUsersJson
        ]
    }
    """.trimIndent()
}