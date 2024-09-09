package soma.achoom.zigg.invite.exception

class UserAlreadyInSpaceException : RuntimeException() {
    override val message: String
        get() = "이미 해당 스페이스에 소속되어 있습니다."
}