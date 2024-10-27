package soma.achoom.zigg.board.exception

class BoardNotFoundException : RuntimeException() {
    override val message: String
        get() = "게시판을 찾을 수 없습니다."
}
