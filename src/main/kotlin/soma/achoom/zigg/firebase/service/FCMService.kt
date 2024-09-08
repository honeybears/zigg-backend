package soma.achoom.zigg.firebase.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.firebase.dto.FCMEvent
import soma.achoom.zigg.firebase.dto.FCMTokenRequestDto
import soma.achoom.zigg.firebase.entity.FCMToken
import soma.achoom.zigg.firebase.exception.FCMMessagingFailException
import soma.achoom.zigg.firebase.repository.FCMRepository
import soma.achoom.zigg.user.entity.User
import soma.achoom.zigg.user.repository.UserRepository

@Service
class FCMService(
    private val fcmRepository: FCMRepository,
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    fun sendMessageTo(fcmEvent: FCMEvent) {
        val tokens = fcmEvent.users.filterNotNull().map {
            it.deviceTokens.map { fcmToken -> fcmToken.token }
        }.toMutableSet().flatten()

        try{
            if (tokens.isNotEmpty()) {
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
                    .build() ?: throw FCMMessagingFailException()
                FirebaseMessaging.getInstance().sendEachForMulticastAsync(multicastMessage)
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }


    }

    @Transactional(readOnly = false)
    fun registerToken(user: User, token: FCMTokenRequestDto) {
        user.deviceTokens.add(
            FCMToken(
                token = token.token,
                user = user
            )
        )
        userRepository.save(user)
    }

    @Transactional(readOnly = false)
    fun unregisterToken(user: User, fcmToken: FCMTokenRequestDto) {
        val destroyToken = fcmRepository.findFCMTokenByToken(fcmToken.token)
            ?: throw RuntimeException("Token not found")
        user.deviceTokens.remove(destroyToken)
        userRepository.save(user)
    }

}