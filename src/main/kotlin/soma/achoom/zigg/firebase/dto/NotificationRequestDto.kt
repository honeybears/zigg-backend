package soma.achoom.zigg.firebase.dto

data class NotificationRequestDto(
    val title: String,
    val body: String,
    val topic: String,
    val token: String
)