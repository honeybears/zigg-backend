package soma.achoom.zigg.firebase.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class FCMConfig(
    @Value("\${app.firebase-configuration.file}")
    private val keyFileName : String
) {
    @PostConstruct
    fun initFirebase() {
        val keyFile = this.javaClass.classLoader.getResourceAsStream(keyFileName)
            ?: throw IllegalArgumentException("Firebase key file not found: $keyFileName")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(keyFile))
            .build()

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }
    }

    @Bean
    fun firebaseApp(): FirebaseApp {
        return FirebaseApp.getInstance()
    }
}