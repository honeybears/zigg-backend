package soma.achoom.zigg.v0.user.dto

import java.util.UUID

data class UserRequestDto (
    val userId:UUID?,
    val userName:String?,
    val userNickname:String?,
    val email:String?
)