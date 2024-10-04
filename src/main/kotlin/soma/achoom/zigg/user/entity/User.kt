package soma.achoom.zigg.user.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.auth.dto.OAuthProviderEnum
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.firebase.entity.FCMToken
import soma.achoom.zigg.invite.entity.Invite
import soma.achoom.zigg.space.entity.SpaceUser
import java.util.*


@Entity
@Table(name = "`user`")
class User(
    @Id
    var userId: UUID = UUID.randomUUID(),

    // 활동명
    var name: String? = null,
    // 고유한 닉네임
    var nickname: String? = null,

    var description: String? = "",

    var tags : String? = "",

    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER,

    @ManyToOne
    var profileImageKey: Image,

    @ManyToOne
    var profileBannerImageKey:Image? = null,

    @Enumerated(EnumType.STRING)
    var platform: OAuthProviderEnum,

    @JsonBackReference
    var providerId: String,

    @JsonBackReference
    var jwtToken: String,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var deviceTokens : MutableSet<FCMToken> = mutableSetOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var spaces : MutableSet<SpaceUser>,

    @OneToMany(mappedBy = "inviter", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var invites : MutableSet<Invite>,
    @OneToMany(mappedBy = "invitee", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var invited : MutableSet<Invite>,

    ) : BaseEntity(){


}