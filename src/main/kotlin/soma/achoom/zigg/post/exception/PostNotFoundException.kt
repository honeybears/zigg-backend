package soma.achoom.zigg.post.exception

class PostNotFoundException : RuntimeException() {
    override val message: String
        get() = "앗! 게시글을 찾을 수 없습니다."
}