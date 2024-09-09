package soma.achoom.zigg.invite.exception

class UserAlreadyInSpaceException : RuntimeException() {
    override val message: String
        get() = "해당 스페이스에 이미 참가하고 있습니다."
}