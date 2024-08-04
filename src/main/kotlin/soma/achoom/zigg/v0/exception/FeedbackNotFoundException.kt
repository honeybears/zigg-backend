package soma.achoom.zigg.v0.exception

class FeedbackNotFoundException : RuntimeException() {
    override val message: String
        get() = "존재하지 않는 피드백입니다."
}