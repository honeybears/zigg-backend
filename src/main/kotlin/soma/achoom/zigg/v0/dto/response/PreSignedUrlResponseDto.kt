package soma.achoom.zigg.v0.dto.response

data class PreSignedUrlResponseDto(
    val url: String,
    val key: String,
    val method: String,
) {
}