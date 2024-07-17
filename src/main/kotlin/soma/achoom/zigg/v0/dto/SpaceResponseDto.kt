package soma.achoom.zigg.v0.dto

import soma.achoom.zigg.v0.model.History
import soma.achoom.zigg.v0.model.SpaceUser

data class SpaceResponseDto(
    val spaceId:Long,
    val spaceName: String,

    val comparisonVideo:Boolean,

    val spaceUsers: MutableSet<SpaceUser> = mutableSetOf(),

    val history: MutableSet<History> = mutableSetOf(),
):BaseResponseDto()
