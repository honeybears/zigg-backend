package soma.achoom.zigg.user.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.user.entity.User
import java.time.LocalDateTime
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserResponseDto(
    val userId: UUID?,
    val userName: String?,
    val userNickname: String?,
    val profileImageUrl: String?,
    val profileBannerImageUrl:String?,
    val userTags : String?,
    val userDescription: String?,
    val createdAt:LocalDateTime?,

)  {

    override fun toString(): String {
        return "UserResponseDto(\n" +
                "userId=$userId,\n" +
                "userName=$userName,\n" +
                "userNickname=$userNickname,\n" +
                "profileImageUrl=$profileImageUrl\n" +
                ")\n"
    }
}