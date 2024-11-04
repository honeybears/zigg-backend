package soma.achoom.zigg.user.exception

class NicknameUserNotFoundException : RuntimeException() {
    override val message: String
        get() = "사용자를 찾을 수 없습니다."
}