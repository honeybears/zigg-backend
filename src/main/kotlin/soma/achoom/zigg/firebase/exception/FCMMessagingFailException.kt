package soma.achoom.zigg.firebase.exception

class FCMMessagingFailException : RuntimeException() {
    override val message: String
        get() = "FCM 메시지 전송에 실패하였습니다."
}