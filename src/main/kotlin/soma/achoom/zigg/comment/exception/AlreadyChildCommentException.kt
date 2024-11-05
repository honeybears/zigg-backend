package soma.achoom.zigg.comment.exception

class AlreadyChildCommentException : RuntimeException() {
    override val message: String
        get() = "이미 대댓글입니다."
}