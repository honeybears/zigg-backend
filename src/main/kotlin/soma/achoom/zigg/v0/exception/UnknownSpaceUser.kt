package soma.achoom.zigg.v0.exception

class UnknownSpaceUser : RuntimeException() {
    override val message: String
        get() = "존재하지 않는 공간 사용자입니다."
}