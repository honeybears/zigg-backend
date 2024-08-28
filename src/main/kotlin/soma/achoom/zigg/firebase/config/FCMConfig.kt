package soma.achoom.zigg.firebase.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.ResourceUtils

@Configuration
class FCMConfig(
    @Value("\${app.firebase-configuration.file}")
    private val keyFileName : String
) {
    @Bean
    fun firebaseApp():FirebaseApp{
        val keyFile = ResourceUtils.getURL("classpath:" + keyFileName).openStream()
        val option = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(keyFile))
            .build()
        return FirebaseApp.initializeApp(option)
    }
}