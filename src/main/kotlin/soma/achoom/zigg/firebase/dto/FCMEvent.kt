package soma.achoom.zigg.firebase.dto

import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.ApnsConfig
import soma.achoom.zigg.user.entity.User

data class FCMEvent(
    val users:MutableSet<User>,
    val title: String,
    val body: String,
    val data: Map<String, String>,
    val android:AndroidConfig?,
    val apns:ApnsConfig?,

)