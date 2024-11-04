package soma.achoom.zigg.comment.exception

class CommentUserMissMatchException : RuntimeException() {
    override val message: String
        get() = "앗! 댓글 작성자가 아닙니다."
}