package soma.achoom.zigg.v0.dto

import soma.achoom.zigg.v0.model.User

data class UserResponseDto(
    val userId:Long?,
    val userName:String?,
    val userNickname:String?,
    val email:String?
) :BaseDto(){
    companion object {
        fun from(user: User): UserResponseDto {
            return UserResponseDto(
                userNickname = user.userNickname,
                userName = user.userName,
                userId = user.userId,
                email = user.email
            )
        }
    }
}