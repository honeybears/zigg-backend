package soma.achoom.zigg.firebase.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.firebase.dto.FCMEvent
import soma.achoom.zigg.firebase.dto.FCMTokenRequestDto
import soma.achoom.zigg.firebase.entity.FCMToken
import soma.achoom.zigg.firebase.repository.FCMRepository
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.service.UserService

@Service
class FCMService(
    private val fcmRepository: FCMRepository,
) {
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    fun sendMessageTo(fcmEvent: FCMEvent) {
        val tokens = fcmEvent.users.map {
            fcmRepository.findFCMTokenByUser(it)?.token ?: throw RuntimeException("Token not found")
        }
        runCatching {
            val multicastMessage = MulticastMessage.builder()
                .setNotification(
                    Notification.builder()
                        .setTitle(fcmEvent.title)
                        .setBody(fcmEvent.body)
                        .build()
                )
                .addAllTokens(tokens)
                //.putAllData(fcmEvent.data)
                //.setApnsConfig(fcmEvent.apns)
                //.setAndroidConfig(fcmEvent.android)
                .build()

            FirebaseMessaging.getInstance().sendEachForMulticastAsync(multicastMessage)
        }.onFailure {
            throw RuntimeException("Failed to send message to FCM", it)
        }

    }

    fun registerToken(user:User, token: FCMTokenRequestDto) {
        val fcmToken =
            fcmRepository.findFCMTokenByUser(user) ?: fcmRepository.save(FCMToken(user = user, token = token.token))
        fcmToken.token = token.token
        fcmRepository.save(fcmToken)
    }

    fun unregisterToken(user: User) {

        val fcmToken = fcmRepository.findFCMTokenByUser(user) ?: throw RuntimeException("Token not found")
        fcmRepository.delete(fcmToken)
    }


}