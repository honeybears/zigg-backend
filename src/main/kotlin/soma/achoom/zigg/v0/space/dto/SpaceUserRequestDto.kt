package soma.achoom.zigg.v0.space.dto

import soma.achoom.zigg.v0.space.entity.SpaceRole

data class SpaceUserRequestDto(

    val userNickname:String?,
    val spaceRole: SpaceRole?
){
}