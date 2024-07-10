package soma.achoom.zigg.v0.dto

import soma.achoom.zigg.v0.model.User

data class UserRequestDto (
    val userId:Long?,
    val userName:String?,
    val userNickname:String?,
    val email:String?
)