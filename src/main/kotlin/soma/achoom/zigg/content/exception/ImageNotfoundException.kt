package soma.achoom.zigg.content.exception

class ImageNotfoundException : RuntimeException() {
    override val message: String
        get() = "해당 이미지를 찾을 수 없습니다."
}