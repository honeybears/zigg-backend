package soma.achoom.zigg.v0.service

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.HttpMethod
import com.google.cloud.storage.Storage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import soma.achoom.zigg.v0.model.enums.GCSDataType
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class GCSService @Autowired constructor(
    private val gcsService: Storage,
    @Value("\${gcp.bucket.name}")
    private val bucketName: String,
) {

    fun generatePreSignedUrl(objectName: String): String {
        val blobId = BlobId.of(bucketName,objectName)
        val option = BlobInfo.newBuilder(blobId).build()
        return gcsService.signUrl(
            option,
            1000L,
            TimeUnit.SECONDS,
            Storage.SignUrlOption.httpMethod(HttpMethod.GET),
            Storage.SignUrlOption.withV4Signature() // Use V4 signing for POST
        ).toString()
    }
    fun uploadFile(objectType:GCSDataType, multipartFile: MultipartFile,id:UUID):String {
        val blobId = BlobId.of(bucketName,objectType.path+id.toString())
        val option = BlobInfo.newBuilder(blobId).setContentType(multipartFile.contentType).build()
        val blob = gcsService.create(option,multipartFile.inputStream.readBytes())
        println(blob.name.toString())
        println(blob.blobId.name.toString())
        return blob.name
    }

    //TODO: createThumbnail
    fun getPreSignedPutUrl(objectType: GCSDataType, id: UUID): String {
        val blobId = BlobId.of(bucketName,objectType.path+id.toString())
        val option = BlobInfo.newBuilder(blobId).build()
        return gcsService.signUrl(
            option,
            1000L,
            TimeUnit.SECONDS,
            Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
            Storage.SignUrlOption.withV4Signature()
        ).toString()
    }
    fun getPreSignedGetUrl(objectType: GCSDataType, id: UUID): String {
        val blobId = BlobId.of(bucketName,objectType.path+id.toString())
        val option = BlobInfo.newBuilder(blobId).build()
        return gcsService.signUrl(
            option,
            1000L,
            TimeUnit.SECONDS,
            Storage.SignUrlOption.httpMethod(HttpMethod.GET),
            Storage.SignUrlOption.withV4Signature()
        ).toString()
    }

}