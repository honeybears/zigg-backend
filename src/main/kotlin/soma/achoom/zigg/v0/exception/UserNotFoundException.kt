package soma.achoom.zigg.v0.exception

class UserNotFoundException : RuntimeException() {
    override val message: String
        get() = "존재하지 않는 사용자입니다."
}