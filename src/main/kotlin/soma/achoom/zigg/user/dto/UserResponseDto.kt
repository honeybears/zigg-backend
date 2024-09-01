package soma.achoom.zigg.user.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.user.entity.User
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserResponseDto(
    val userId: UUID?,
    val userName: String?,
    val userNickname: String?,
    val profileImageUrl: String?
)  {
    companion object {
        fun from(user: User): UserResponseDto {
            return UserResponseDto(
                userId = user.userId,
                userNickname = user.userNickname,
                userName = user.userName,
                profileImageUrl = user.profileImageKey
            )
        }
    }

    override fun toString(): String {
        return "UserResponseDto(\n" +
                "userId=$userId,\n" +
                "userName=$userName,\n" +
                "userNickname=$userNickname,\n" +
                "profileImageUrl=$profileImageUrl\n" +
                ")\n"
    }
}