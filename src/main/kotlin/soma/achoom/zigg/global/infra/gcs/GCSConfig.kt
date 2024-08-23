package soma.achoom.zigg.global.infra.gcs

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.ResourceUtils

@Configuration
class GCSConfig constructor(
    @Value("\${gcs.keyfile.name}")
    private val keyFileName: String
) {

    @Bean
    fun gcsService(): Storage {
        val keyFile = ResourceUtils.getURL("classpath:" + keyFileName).openStream()
        return StorageOptions
            .newBuilder()
            .setCredentials(GoogleCredentials.fromStream(keyFile))
            .build().service
    }
}