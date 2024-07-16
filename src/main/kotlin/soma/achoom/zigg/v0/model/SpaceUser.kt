package soma.achoom.zigg.v0.model

import jakarta.persistence.*

@Entity
@Table(name = "space_user") // 조인 테이블 이름 명시
data class SpaceUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "space_id")
    val space: Space,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "role") // 추가할 role 컬럼
    @Enumerated(EnumType.STRING)
    val role: SpaceRole
):BaseEntity()
