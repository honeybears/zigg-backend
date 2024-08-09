package soma.achoom.zigg.v0.service

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.HttpMethod
import com.google.cloud.storage.Option
import com.google.cloud.storage.Storage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.Date
import java.util.concurrent.TimeUnit

@Service
class GCSService @Autowired constructor(
    private val gcsService: Storage,
    @Value("\${gcp.bucket.name}")
    private val bucketName: String,
) {

    fun preSignedUrl(objectName: String,expiration: Long, httpMethod: HttpMethod): String {
        val blobId = BlobId.of(bucketName,objectName)
        val option = BlobInfo.newBuilder(blobId).build()
        return gcsService.signUrl(
            option,
            expiration,
            TimeUnit.SECONDS,
            Storage.SignUrlOption.httpMethod(httpMethod),
            Storage.SignUrlOption.withV4Signature() // Use V4 signing for POST
        ).toString()
    }
    fun uploadFile(objectName: String, filePath: String) {
        val blobId = BlobId.of(bucketName,objectName)
        val option = BlobInfo.newBuilder(blobId).build()
        gcsService.create(option,java.io.File(filePath).readBytes())
    }
}