package soma.achoom.zigg.v0.model

import jakarta.persistence.*
import soma.achoom.zigg.v0.auth.CustomOAuthProviderEnum
import soma.achoom.zigg.v0.auth.UserRole


@Entity
@Table(name = "user")
data class User( 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long? = null,

    var userName: String?,

    var userNickname: String? = null,

    var email: String?,

    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER,

    @Enumerated(EnumType.STRING)
    var provider: CustomOAuthProviderEnum,

    @OneToMany(mappedBy = "user") // SpaceUser 엔티티와의 관계 설정
    val spaceUsers: MutableSet<SpaceUser> = mutableSetOf(),


    var providerId:String,

) : BaseEntity()