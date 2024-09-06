package soma.achoom.zigg.invite.exception

class InvitedUserMissMatchException : RuntimeException() {
    override val message: String
        get() = "초대받은 사용자가 아닙니다."
}