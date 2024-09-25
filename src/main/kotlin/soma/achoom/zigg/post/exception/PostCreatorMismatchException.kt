package soma.achoom.zigg.post.exception

class PostCreatorMismatchException : RuntimeException(){
    override val message: String
        get() = "게시글 작성자가 아닙니다."
}