package soma.achoom.zigg.global.infra

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.HttpMethod
import com.google.cloud.storage.Storage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import soma.achoom.zigg.v0.history.dto.UploadContentTypeRequestDto
import java.net.URI
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class GCSService @Autowired constructor(
    private val googleCloudStorage: Storage,
    @Value("\${gcp.bucket.name}")
    private val bucketName: String,
) {

    fun getPreSignedGetUrl(objectName: String): String {
        val blobId = BlobId.of(bucketName,objectName)
        val option = BlobInfo.newBuilder(blobId).build()
        return googleCloudStorage.signUrl(
            option,
            1000L,
            TimeUnit.SECONDS,
            Storage.SignUrlOption.httpMethod(HttpMethod.GET),
            Storage.SignUrlOption.withV4Signature() // Use V4 signing for POST
        ).toString()
    }
    fun uploadFile(objectType: GCSDataType, multipartFile: MultipartFile, id:UUID):String {
        val blobId = BlobId.of(bucketName,objectType.path+id.toString())
        val option = BlobInfo.newBuilder(blobId).setContentType(multipartFile.contentType).build()
        val blob = googleCloudStorage.create(option,multipartFile.inputStream.readBytes())
        return blob.name
    }


    fun getPreSignedPutUrl(objectType: GCSDataType, id: UUID, uploadContentTypeRequestDto: UploadContentTypeRequestDto): String {
        val blobId = BlobId.of(bucketName,objectType.path+id.toString()+"."+uploadContentTypeRequestDto.fileExtension)

        val option = BlobInfo.newBuilder(blobId).build()

        val preSignedUrl = googleCloudStorage.signUrl(
            option,
            1000L,
            TimeUnit.SECONDS,
            Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
            Storage.SignUrlOption.withV4Signature()
        ).toString()
        return preSignedUrl
    }

    fun convertPreSignedUrlToGeneralKey(preSignedUrl: String): String {
        val uri = URI.create(preSignedUrl)
        val path = uri.path.split("?")[0]
        val bucketName = path.split("/")[1]
        val objectKey = path.substring(bucketName.length + 2)
        return objectKey
    }



}