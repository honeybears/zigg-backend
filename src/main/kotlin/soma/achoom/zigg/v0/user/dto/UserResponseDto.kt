package soma.achoom.zigg.v0.user.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.v0.user.entity.User
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserResponseDto(
    val userId: UUID?,
    val userName: String?,
    val userNickname: String?,
)  {
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