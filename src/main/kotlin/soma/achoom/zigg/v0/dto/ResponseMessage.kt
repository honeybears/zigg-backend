package soma.achoom.zigg.v0.dto

class ResponseMessage<T:BaseResponseDto> {
    var message: String? = null
    var dto: T? = null


    constructor(message: String) {
        this.message = message

    }
    constructor(message: String ,dto: T) {
        this.message = message
        this.dto = dto
    }
}