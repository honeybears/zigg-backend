package soma.achoom.zigg.user.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import soma.achoom.zigg.global.BaseEntity
import soma.achoom.zigg.auth.dto.OAuthProviderEnum
import soma.achoom.zigg.firebase.entity.FCMToken
import soma.achoom.zigg.invite.entity.Invite
import soma.achoom.zigg.space.entity.SpaceUser
import java.util.*


@Entity
@Table(name = "`user`")
class User(
    @Id
    var userId: UUID = UUID.randomUUID(),

    var userName: String? = null,

    var userNickname: String? = null,

    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER,

    var profileImageKey: String,

    var profileBannerImageKey:String?,

    @Enumerated(EnumType.STRING)
    var platform: OAuthProviderEnum,

    @JsonBackReference
    var providerId: String,

    @JsonBackReference
    var jwtToken: String,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var deviceTokens : MutableSet<FCMToken>,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var spaces : MutableSet<SpaceUser>,

    @OneToMany(mappedBy = "inviter", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var invites : MutableSet<Invite>,
    @OneToMany(mappedBy = "invitee", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var invited : MutableSet<Invite>

) : BaseEntity(){


}