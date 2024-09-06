package soma.achoom.zigg.invite.exception

class InviteNotFoundException : RuntimeException() {
    override val message: String
        get() = "초대장을 찾을 수 없습니다."
}