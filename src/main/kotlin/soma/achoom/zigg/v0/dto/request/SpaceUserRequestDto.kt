package soma.achoom.zigg.v0.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import soma.achoom.zigg.v0.model.enums.SpaceRole
data class SpaceUserRequestDto(

    val userNickname:String?,
    val spaceRole: SpaceRole?
){
}