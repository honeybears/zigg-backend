package soma.achoom.zigg.space.exception

class LowSpacePermissionException : RuntimeException() {
    override val message: String
        get() = "스페이스 권한이 부족합니다."
}