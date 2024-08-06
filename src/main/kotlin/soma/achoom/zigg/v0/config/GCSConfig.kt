package soma.achoom.zigg.v0.config

import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GCSConfig {
    @Bean
    fun gcsService(): Storage {
        return StorageOptions.getDefaultInstance().service
    }
}