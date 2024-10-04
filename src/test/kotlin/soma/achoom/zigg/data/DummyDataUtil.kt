package soma.achoom.zigg.data

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import soma.achoom.zigg.TestConfig
import soma.achoom.zigg.TestConfig.Companion.HISTORY_VIDEO_URL
import soma.achoom.zigg.TestConfig.Companion.PROFILE_IMAGE_KEY
import soma.achoom.zigg.TestConfig.Companion.SPACE_IMAGE_KEY
import soma.achoom.zigg.auth.dto.OAuthProviderEnum
import soma.achoom.zigg.auth.filter.CustomUserDetails
import soma.achoom.zigg.content.entity.Image
import soma.achoom.zigg.content.entity.Video
import soma.achoom.zigg.content.repository.ImageRepository
import soma.achoom.zigg.content.repository.VideoRepository
import soma.achoom.zigg.feedback.entity.Feedback
import soma.achoom.zigg.firebase.entity.FCMToken
import soma.achoom.zigg.history.entity.History
import soma.achoom.zigg.history.repository.HistoryRepository
import soma.achoom.zigg.invite.entity.Invite
import soma.achoom.zigg.invite.repository.InviteRepository
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.entity.SpaceRole
import soma.achoom.zigg.space.entity.SpaceUser
import soma.achoom.zigg.space.repository.SpaceRepository
import soma.achoom.zigg.space.repository.SpaceUserRepository
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.entity.UserRole
import soma.achoom.zigg.user.repository.UserRepository
import java.util.*

@Component
@Import(TestConfig::class)
class DummyDataUtil(
    private val inviteRepository: InviteRepository,
    private val userRepository: UserRepository,
    private val spaceRepository: SpaceRepository,
    private val historyRepository: HistoryRepository,
    private val spaceUserRepository: SpaceUserRepository,
    private val imageRepository: ImageRepository,
    private val videoRepository: VideoRepository
) {

    @Value("\${jwt.secret}")
    private lateinit var secret: String

    private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)) }

    fun createDummyUser(): User {
        val image = Image(
            imageKey = PROFILE_IMAGE_KEY,
            uploader = null
        )
        imageRepository.save(image)
        val user = User(
            userId = UUID.randomUUID(),
            name = createRandomString(5),
            nickname = createRandomString(7),
            profileImageKey = image,
            jwtToken = "",
            providerId = UUID.randomUUID().toString(),
            platform = OAuthProviderEnum.TEST,
            deviceTokens = mutableSetOf(),
            spaces = mutableSetOf(),
            invites = mutableSetOf(),
            invited = mutableSetOf(),
            profileBannerImageKey = null
        )
        return userRepository.save(user)
    }

    fun createDummyGuestUser() : User {
        val image = Image(
            imageKey = PROFILE_IMAGE_KEY,
            uploader = null
        )
        imageRepository.save(image)
        val user = User(
            userId = UUID.randomUUID(),
            name = createRandomString(5),
            nickname = createRandomString(7),
            profileImageKey = image,
            jwtToken = "",
            providerId = UUID.randomUUID().toString(),
            platform = OAuthProviderEnum.GUEST,
            role = UserRole.GUEST,
            deviceTokens = mutableSetOf(),
            spaces = mutableSetOf(),
            invites = mutableSetOf(),
            invited = mutableSetOf(),
            profileBannerImageKey = null
        )
        return userRepository.save(user)
    }

    fun createDummyUserWithMultiFCMToken(size: Int): User {
        val user = createDummyUser()
        user.deviceTokens = mutableSetOf()
        for (i in 0 until size) {
            user.deviceTokens.add(
                FCMToken(
                    token = UUID.randomUUID().toString(),
                )
            )
        }
        return userRepository.save(user)
    }

    fun createDummySpace(): Space {
        val image = Image(
            imageKey = SPACE_IMAGE_KEY,
            uploader = null
        )
        imageRepository.save(image)
        val space =  Space(
            spaceId = UUID.randomUUID(),
            name = createRandomString(10),
            imageKey = image,
            users = mutableSetOf(),
            referenceVideoKey = HISTORY_VIDEO_URL,
        )
        return spaceRepository.save(space)
    }

    fun createDummySpaceUser(space: Space, user: User): SpaceUser {
        val spaceUser = SpaceUser(
            spaceUserId = UUID.randomUUID(),
            space = space,
            user = user,
            role = SpaceRole.USER
        )
        spaceUserRepository.save(spaceUser)
        return spaceUser
    }

    fun creatDummyInvite(user: User, inviter: User, space: Space): Invite {
        val invite = Invite(
            inviteId = UUID.randomUUID(),
            invitee = user,
            inviter = inviter,
            space = space,
        )
        space.invites.add(invite)
        spaceRepository.save(space)
        return invite
    }

    fun createDummyUserList(size: Int): List<User> {
        return (0 until size).map { createDummyUser() }
    }

    fun createDummyAuthentication(user: User): Authentication {
        val jwt = createTestJwt(user).split(" ")[1].trim()
        val claims: Claims = Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jwt)
            .body
        val auth = claims["auth"] ?: throw RuntimeException("잘못된 토큰입니다.")
        val oauthProvider = claims["provider"] as String // Extract the OAuth provider
        val authorities: Collection<GrantedAuthority> = (auth as String)
            .split(",")
            .map { SimpleGrantedAuthority(it) }
        val userDetails = CustomUserDetails(claims.subject, "", oauthProvider, authorities)
        return UsernamePasswordAuthenticationToken(userDetails, null, authorities)
    }

    fun createDummyAuthenticationList(users: List<User>): List<Authentication> {
        return users.map {
            createDummyAuthentication(it)
        }
    }

    private fun createTestJwt(user: User): String {

        val authorities = user.role
        val accessToken = Jwts.builder()
            .setSubject(user.providerId)
            .addClaims(mapOf("auth" to authorities, "provider" to user.platform.name))
            .setIssuedAt(Date())
            .signWith(key, io.jsonwebtoken.SignatureAlgorithm.HS256)
            .compact()
        return "Bearer $accessToken"
    }

    private fun createRandomString(length: Int): String {
        val firstName = listOf(
            "김",
            "이",
            "박",
            "최",
            "정",
            "강",
            "조",
            "윤",
            "장",
            "임",
            "한",
            "오",
            "서",
            "신",
            "권",
            "황",
            "안",
            "송",
            "류",
            "전",
            "홍",
            "고",
            "문",
            "양",
            "손",
            "배",
            "조",
            "백",
            "허",
            "유",
            "남",
            "심",
            "노",
            "정",
            "하",
            "곽",
            "성",
            "차",
            "주",
            "우",
            "구",
            "신",
            "임",
            "나",
            "전",
            "민",
            "유",
            "진",
            "지",
            "엄",
            "채",
            "원",
            "천",
            "방",
            "공",
            "강",
            "현",
            "함",
            "변",
            "염",
            "양",
            "변",
            "여",
            "추",
            "노",
            "도",
            "소",
            "신",
            "석",
            "선",
            "설",
            "마",
            "길",
            "주",
            "연",
            "방",
            "위",
            "표",
            "명",
            "기",
            "반",
            "왕",
            "금",
            "옥",
            "육",
            "인",
            "맹",
            "제",
            "모",
            "장",
            "남",
            "탁",
            "국",
            "여",
            "진",
            "어",
            "은",
            "편",
            "구",
            "용"
        )
        val lastName = listOf(
            "가",
            "강",
            "건",
            "경",
            "고",
            "관",
            "광",
            "구",
            "규",
            "근",
            "기",
            "길",
            "나",
            "남",
            "노",
            "누",
            "다",
            "단",
            "달",
            "담",
            "대",
            "덕",
            "도",
            "동",
            "두",
            "라",
            "래",
            "로",
            "루",
            "리",
            "마",
            "만",
            "명",
            "무",
            "문",
            "미",
            "민",
            "바",
            "박",
            "백",
            "범",
            "별",
            "병",
            "보",
            "빛",
            "사",
            "산",
            "상",
            "새",
            "서",
            "석",
            "선",
            "설",
            "섭",
            "성",
            "세",
            "소",
            "솔",
            "수",
            "숙",
            "순",
            "숭",
            "슬",
            "승",
            "시",
            "신",
            "아",
            "안",
            "애",
            "엄",
            "여",
            "연",
            "영",
            "예",
            "오",
            "옥",
            "완",
            "요",
            "용",
            "우",
            "원",
            "월",
            "위",
            "유",
            "윤",
            "율",
            "으",
            "은",
            "의",
            "이",
            "익",
            "인",
            "일",
            "잎",
            "자",
            "잔",
            "장",
            "재",
            "전",
            "정",
            "제",
            "조",
            "종",
            "주",
            "준",
            "중",
            "지",
            "진",
            "찬",
            "창",
            "채",
            "천",
            "철",
            "초",
            "춘",
            "충",
            "치",
            "탐",
            "태",
            "택",
            "판",
            "하",
            "한",
            "해",
            "혁",
            "현",
            "형",
            "혜",
            "호",
            "홍",
            "화",
            "환",
            "회",
            "효",
            "훈",
            "휘",
            "희",
            "운",
            "모",
            "배",
            "부",
            "림",
            "봉",
            "혼",
            "황",
            "량",
            "린",
            "을",
            "비",
            "솜",
            "공",
            "면",
            "탁",
            "온",
            "디",
            "항",
            "후",
            "려",
            "균",
            "묵",
            "송",
            "욱",
            "휴",
            "언",
            "령",
            "섬",
            "들",
            "견",
            "추",
            "걸",
            "삼",
            "열",
            "웅",
            "분",
            "변",
            "양",
            "출",
            "타",
            "흥",
            "겸",
            "곤",
            "번",
            "식",
            "란",
            "더",
            "손",
            "술",
            "훔",
            "반",
            "빈",
            "실",
            "직",
            "흠",
            "흔",
            "악",
            "람",
            "뜸",
            "권",
            "복",
            "심",
            "헌",
            "엽",
            "학",
            "개",
            "롱",
            "평",
            "늘",
            "늬",
            "랑",
            "얀",
            "향",
            "울",
            "련"
        );
        return firstName.random() + lastName.random() + lastName.random()
    }

    fun createDummyFeedback(history: History, user: SpaceUser, recipients: MutableList<SpaceUser>): Feedback {
        val feedback =  Feedback(
            feedbackId = UUID.randomUUID(),
            timeline = "00:00:00",
            message = "피드백 내용",
            creator = user,
            recipients = recipients,
        )
        history.feedbacks.add(feedback)
        historyRepository.save(history)
        return feedback

    }

    fun createDummyHistory(space: Space): History {
        val video = Video(
            videoKey = HISTORY_VIDEO_URL,
            uploader = null,
            duration = "00:00:00",
        )
        val image = Image(
            uploader = null,
            imageKey = HISTORY_VIDEO_URL
        )
        imageRepository.save(image)
        videoRepository.save(video)
        val history = History(
            historyId = UUID.randomUUID(),
//            space = space,
            name = "히스토리 이름",
            videoKey = video,
            videoThumbnailUrl = image,
        )
        space.histories.add(history)
        spaceRepository.save(space)
        return history
    }
}