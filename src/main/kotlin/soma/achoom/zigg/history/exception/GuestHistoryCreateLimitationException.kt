package soma.achoom.zigg.history.exception

class GuestHistoryCreateLimitationException : RuntimeException() {
    override val message: String
        get() = "게스트 유저는 최대 3개의 히스토리만 생성 가능합니다."
}