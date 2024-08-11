package soma.achoom.zigg.v0.dto.response

import soma.achoom.zigg.v0.dto.BaseResponseDto
import soma.achoom.zigg.v0.model.User
import java.util.UUID

data class UserResponseDto(
    val userId: UUID?,
    val userName: String?,
    val userNickname: String?,
) : BaseResponseDto() {
    companion object {
        fun from(user: User): UserResponseDto {
            return UserResponseDto(
                userId = user.userId,
                userNickname = user.userNickname,
                userName = user.userName,
            )
        }
    }
}