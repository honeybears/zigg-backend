package soma.achoom.zigg.v0.dto.token


data class OAuthTokenVerificationDto(
    val accessToken: String?,
    val idToken: String,
    val userInfo : Map<String,Any>,
    val platform: String,
    val providerId: String,
)