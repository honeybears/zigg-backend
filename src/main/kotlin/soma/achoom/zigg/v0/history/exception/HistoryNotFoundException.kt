package soma.achoom.zigg.v0.history.exception

class HistoryNotFoundException : RuntimeException() {
    override val message: String
        get() = "존재하지 않는 히스토리입니다."
}