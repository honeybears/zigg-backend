package soma.achoom.zigg.firebase.listener

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import soma.achoom.zigg.firebase.dto.FCMEvent
import soma.achoom.zigg.firebase.service.FCMService
import soma.achoom.zigg.user.entity.User

@Component
class FCMEventHandler(
    private val fcmService: FCMService
) {
    @EventListener
    fun handleFCMEvent(event: FCMEvent) {
        fcmService.sendMessageTo(event)
    }
}