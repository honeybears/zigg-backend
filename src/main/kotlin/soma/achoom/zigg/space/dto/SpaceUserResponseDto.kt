package soma.achoom.zigg.space.dto

import soma.achoom.zigg.space.entity.SpaceRole
import java.util.UUID

data class SpaceUserResponseDto(
    val userNickname: String?,
    val userName:String?,
    val spaceUserId: UUID?,
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