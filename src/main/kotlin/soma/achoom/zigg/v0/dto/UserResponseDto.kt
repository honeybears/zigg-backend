package soma.achoom.zigg.v0.dto

import soma.achoom.zigg.v0.model.User

data class UserResponseDto(
    val userId:Long?,
    val userName:String?,
    val userNickname:String?,
    val email:String?
) :BaseResponseDto(){
    companion object {
        fun from(user: User?): UserResponseDto? {
            if (user == null) return null
            return UserResponseDto(
                userId = user.userId,
                userNickname = user.userNickname,
                userName = user.userName,
                email = user.email
            )
        }
    }
}