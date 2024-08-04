package soma.achoom.zigg.v0.exception

class UserAlreadyExistsException : RuntimeException() {
    override val message: String
        get() = "이미 존재하는 사용자입니다."
}