package soma.achoom.zigg.comment.exception

class CommentNotFoundException : RuntimeException() {
    override val message: String
        get() = "앗! 댓글을 찾을 수 없습니다."
}