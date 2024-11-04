package soma.achoom.zigg.space.dto

import com.fasterxml.jackson.annotation.JsonInclude
import soma.achoom.zigg.space.entity.SpaceRole
import java.util.UUID
@JsonInclude(JsonInclude.Include.NON_NULL)
data class SpaceUserResponseDto(
    val userId: Long? = null,
    val userNickname: String?,
    val userName:String?,
    val spaceUserId: Long?,
    val spaceRole: SpaceRole?,
    val profileImageUrl: String?
){
    override fun toString(): String {
        return "SpaceUserResponseDto(\n" +
                "userNickname=$userNickname,\n" +
                "userName=$userName,\n" +
                "spaceUserId=$spaceUserId,\n" +
                "spaceRole=$spaceRole,\n" +
                "profileImageUrl=$profileImageUrl\n" +
                ")\n"
    }
}
