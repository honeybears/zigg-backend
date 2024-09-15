package soma.achoom.zigg.space.exception

class GuestSpaceCreateLimitationException : RuntimeException() {
    override val message: String
        get() = "게스트 유저는 최대 1개의 스페이스만 생성 가능합니다."
}