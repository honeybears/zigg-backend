package soma.achoom.zigg.v0.service

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.HttpMethod
import com.google.cloud.storage.Storage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class GCSService @Autowired constructor(
    private val gcsService: Storage,
) {
    fun preSignedUrl(objectName: String,expiration: Long, httpMethod: HttpMethod): String {
        val blobId = BlobId.of("test-zigg",objectName )
        val option = BlobInfo.newBuilder(blobId).build()
        return gcsService.signUrl(
            option,
            expiration,
            TimeUnit.SECONDS,
            Storage.SignUrlOption.httpMethod(httpMethod),
            Storage.SignUrlOption.withV4Signature() // Use V4 signing for POST
        ).toString()

    }
}