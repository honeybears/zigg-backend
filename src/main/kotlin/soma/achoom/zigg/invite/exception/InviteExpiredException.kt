package soma.achoom.zigg.invite.exception

class InviteExpiredException : RuntimeException() {
    override val message: String
        get() = "초대장이 만료되었습니다."
}