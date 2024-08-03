package soma.achoom.zigg.v0.exception

class UnknownSpace : RuntimeException() {
    override val message: String
        get() = "존재하지 않는 공간입니다."
}