package soma.achoom.zigg.version.v0.space.exception

class SpaceUserNotFoundInSpaceException : RuntimeException() {
    override val message: String
        get() = "스페이스에 포함되지 않은 사용자입니다."
}