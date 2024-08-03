package soma.achoom.zigg.v0.exception

class UnknownHistory : RuntimeException() {
    override val message: String
        get() = "존재하지 않는 히스토리입니다."
}