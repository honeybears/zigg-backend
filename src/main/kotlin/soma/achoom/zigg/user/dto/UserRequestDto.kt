package soma.achoom.zigg.user.dto

import java.util.UUID

data class UserRequestDto (
    val userId:UUID?,
    val userName:String?,
    val userNickname:String?,
    val profileImageUrl:String?,
    val profileBannerImageUrl:String?
)