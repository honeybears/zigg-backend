package soma.achoom.zigg.v0.dto.request

import java.util.UUID

data class UserRequestDto (
    val userId:UUID?,
    val userName:String?,
    val userNickname:String?,
    val email:String?
)