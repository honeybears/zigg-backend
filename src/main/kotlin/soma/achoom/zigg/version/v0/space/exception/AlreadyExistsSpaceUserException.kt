package soma.achoom.zigg.version.v0.space.exception

class AlreadyExistsSpaceUserException : RuntimeException() {
    override val message: String
        get() = "이미 존재하는 공간 사용자입니다."
}