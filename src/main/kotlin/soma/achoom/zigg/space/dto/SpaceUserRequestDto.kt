package soma.achoom.zigg.space.dto

import soma.achoom.zigg.space.entity.SpaceRole
import java.util.UUID

data class SpaceUserRequestDto(

    val userNickname:String?,
    val spaceUserId: UUID?,
    val spaceRole: SpaceRole?
){
    override fun equals(other: Any?): Boolean {
        return spaceUserId == (other as SpaceUserRequestDto).spaceUserId
    }
    override fun hashCode(): Int {
        return spaceUserId.hashCode()
    }
}