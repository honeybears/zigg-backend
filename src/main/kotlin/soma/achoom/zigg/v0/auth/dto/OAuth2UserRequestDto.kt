package soma.achoom.zigg.v0.auth.dto


data class OAuth2UserRequestDto(
    val accessToken: String?,
    val idToken: String,
    val userInfo: Map<String, Any>,
    val platform: String,
    val providerId: String,
    val userNickname: String?,
    val userName: String?,
)