package soma.achoom.zigg.firebase.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.jasypt.encryption.StringEncryptor
import org.jasypt.util.text.AES256TextEncryptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class FCMConfig(
    @Value("\${app.firebase-configuration.file}")
    private val keyFileName: String,
    @Qualifier("jasyptStringEncryptor") // 특정 Bean을 선택하여 주입
    private val stringEncryptor: StringEncryptor)
{
    @PostConstruct
    fun initFirebase() {
        val keyFile = this.javaClass.classLoader.getResourceAsStream(keyFileName)
            ?: throw IllegalArgumentException("Firebase key file not found: $keyFileName")

        // JSON 내용 읽기
        val jsonContent = keyFile.bufferedReader().use { it.readText() }

        // JSON 파싱
        val objectMapper = ObjectMapper()
        val jsonMap: MutableMap<String, Any> = objectMapper.readValue(jsonContent, MutableMap::class.java) as MutableMap<String, Any>

        // 암호화된 값만 복호화
        jsonMap.forEach { (key, value) ->
            if (value is String && value.startsWith("ENC(") && value.endsWith(")")) {
                val base64Encrypted = value.removePrefix("ENC(").removeSuffix(")")
                try {
                    jsonMap[key] = stringEncryptor.decrypt(base64Encrypted)
                } catch (e: Exception) {
                    throw RuntimeException("Decryption failed for key: $key. Please verify the encryption password and input format.", e)
                }
            }
        }

        // 파싱된 맵을 다시 JSON 문자열로 변환
        val decryptedJsonContent = objectMapper.writeValueAsString(jsonMap)

        // FirebaseOptions 설정
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(decryptedJsonContent.byteInputStream()))
            .build()

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }

    }
//    @PostConstruct
//    fun initFirebase() {
//        val keyFile = this.javaClass.classLoader.getResourceAsStream(keyFileName)
//            ?: throw IllegalArgumentException("Firebase key file not found: $keyFileName")
//
//        val options = FirebaseOptions.builder()
//            .setCredentials(GoogleCredentials.fromStream(keyFile))
//            .build()
//
//        if (FirebaseApp.getApps().isEmpty()) {
//            FirebaseApp.initializeApp(options)
//        }
//    }

    @Bean
    fun firebaseApp(): FirebaseApp {
        return FirebaseApp.getInstance()
    }

    @Bean
    fun googleCredentials(): GoogleCredentials {
        val keyFile = this.javaClass.classLoader.getResourceAsStream(keyFileName)
            ?: throw IllegalArgumentException("Firebase key file not found: $keyFileName")

        val jsonContent = keyFile.bufferedReader().use { it.readText() }

        // JSON 파싱
        val objectMapper = ObjectMapper()
        val jsonMap: MutableMap<String, Any> = objectMapper.readValue(jsonContent)

        // 암호화된 값만 복호화 (private_key 등)
        jsonMap.forEach { (key, value) ->
            if (value is String && value.startsWith("ENC(") && value.endsWith(")")) {
                val base64Encrypted = value.removePrefix("ENC(").removeSuffix(")")
                try {
                    jsonMap[key] = stringEncryptor.decrypt(base64Encrypted)
                } catch (e: Exception) {
                    throw RuntimeException("Decryption failed for key: $key. Please verify the encryption password and input format.", e)
                }
            }
        }

        // 복호화된 JSON 맵을 다시 JSON 문자열로 변환
        val decryptedJsonContent = objectMapper.writeValueAsString(jsonMap)

        // GoogleCredentials 생성
        return GoogleCredentials.fromStream(decryptedJsonContent.byteInputStream())
    }
}