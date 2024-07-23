package soma.achoom.zigg.v0.dto.response

import soma.achoom.zigg.v0.dto.BaseResponseDto
import soma.achoom.zigg.v0.model.History
import soma.achoom.zigg.v0.model.SpaceUser

data class SpaceResponseDto(
    val spaceId:Long?,
    val spaceName: String?,

    val comparisonVideo:Boolean?,

    val spaceUsers: MutableSet<SpaceUser>?,

    val history: MutableSet<History>?,
): BaseResponseDto()
