package soma.achoom.zigg.v0.exception

class LowSpacePermission : RuntimeException() {
    override val message: String
        get() = "스페이스 권한이 부족합니다."
}