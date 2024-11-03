package soma.achoom.zigg.user.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.user.entity.User
import java.time.LocalDateTime
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserResponseDto(
    val userId: Long? = null,
    val userName: String?,
    val userNickname: String?,
    val profileImageUrl: String?,
    val profileBannerImageUrl:String? = null,
    val userTags : String? = null,
    val userDescription: String? = null,
    val createdAt:LocalDateTime? = null,

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