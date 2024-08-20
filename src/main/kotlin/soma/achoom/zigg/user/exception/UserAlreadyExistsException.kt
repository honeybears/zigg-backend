package soma.achoom.zigg.user.exception

class UserAlreadyExistsException : RuntimeException() {
    override val message: String
        get() = "이미 존재하는 사용자입니다."
}