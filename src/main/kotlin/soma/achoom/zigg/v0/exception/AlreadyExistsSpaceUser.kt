package soma.achoom.zigg.v0.exception

class AlreadyExistsSpaceUser : RuntimeException() {
    override val message: String
        get() = "이미 존재하는 공간 사용자입니다."
}