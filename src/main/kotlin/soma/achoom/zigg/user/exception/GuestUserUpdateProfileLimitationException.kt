package soma.achoom.zigg.user.exception

class GuestUserUpdateProfileLimitationException : RuntimeException() {
    override val message: String
        get() = "게스트 유저는 프로필을 수정할 수 없습니다."
}