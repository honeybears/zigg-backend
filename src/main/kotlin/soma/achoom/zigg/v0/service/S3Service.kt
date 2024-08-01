package soma.achoom.zigg.v0.service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import soma.achoom.zigg.v0.model.enums.S3Option
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

@Service
class S3Service @Autowired constructor(
    private val amazonS3: AmazonS3,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String
) {
    fun uploadFile(multipartFile: MultipartFile, s3Option: S3Option):String {
        val file = convertMultiPartToFile(multipartFile)
        val identifyKey = UUID.randomUUID().toString()

        val s3Path = when (s3Option) {
            S3Option.HISTORY_VIDEO -> "dev/history_video/$identifyKey"+"_" + "${multipartFile.originalFilename}"
            S3Option.USER_IMAGE -> "dev/user_image/$identifyKey"+"_" + "${multipartFile.originalFilename}"
            S3Option.SPACE_IMAGE -> "dev/space_image/$identifyKey" +"_" + "${multipartFile.originalFilename}"
            S3Option.SPACE_REFERENCE_VIDEO -> "dev/space_reference_video/$identifyKey"+"_" + "${multipartFile.originalFilename}"
            S3Option.SPACE_THUMBNAIL -> "dev/space_thumbnail/$identifyKey"+"_" + "${multipartFile.originalFilename}"
        }

        val fileUrl = putS3(file, s3Path)
        file.delete()
        return fileUrl
    }
    private fun convertMultiPartToFile(multipartFile: MultipartFile): File {
        val convFile = multipartFile.originalFilename?.let { File(it) }?: File("temp")

        FileOutputStream(convFile).use { fos -> fos.write(multipartFile.bytes) }
            .runCatching {
                throw IOException("Error converting the multipart file to file")
            }

        return convFile
    }
    fun updateFile(file: File, s3Path: String) {
        val key = parseUrlToKey(s3Path)
        updateS3(file, key)

    }
    fun deleteFile(s3Path:String) {
        removeS3(s3Path)

    }
    fun generatePreSignedUrlToGet(identifyKey:Long, s3Option: S3Option, expirationInMinutes:Int):String {
        val s3Path = when (s3Option) {
            S3Option.HISTORY_VIDEO -> "dev/history_video/$identifyKey"
            S3Option.USER_IMAGE -> "dev/user_image/$identifyKey"
            S3Option.SPACE_IMAGE -> "dev/space_image/$identifyKey"
            S3Option.SPACE_REFERENCE_VIDEO -> "dev/space_reference_video/$identifyKey"
            S3Option.SPACE_THUMBNAIL -> "dev/space_thumbnail/$identifyKey"
        }
        val expiration = Date(System.currentTimeMillis() + expirationInMinutes * 60 * 1000)
        val generatePreSignedUrlRequest = GeneratePresignedUrlRequest(bucket, s3Path)
            .withMethod(HttpMethod.GET)
            .withExpiration(expiration)
        return amazonS3.generatePresignedUrl(generatePreSignedUrlRequest).toString()
    }

    fun generatePreSignedUrlToGet(s3Path: String, expirationInMinutes: Int):String {
        val expiration = Date(System.currentTimeMillis() + expirationInMinutes * 60 * 1000)
        val generatePreSignedUrlRequest = GeneratePresignedUrlRequest(bucket, s3Path)
            .withMethod(HttpMethod.GET)
            .withExpiration(expiration)
        return amazonS3.generatePresignedUrl(generatePreSignedUrlRequest).toString()
    }
    fun generatePreSignedUrlToPut(s3Path: String, expirationInMinutes: Int):String {
        val expiration = Date(System.currentTimeMillis() + expirationInMinutes * 60 * 1000)
        val generatePreSignedUrlRequest = GeneratePresignedUrlRequest(bucket, s3Path)
            .withMethod(HttpMethod.PUT)
            .withExpiration(expiration)
        return amazonS3.generatePresignedUrl(generatePreSignedUrlRequest).toString()
    }
    
    private fun putS3(file: File, s3Path: String): String {
        amazonS3.putObject(bucket, s3Path, file)
        return s3Path
    }
    private fun removeS3(fileName: String) {
        amazonS3.deleteObject(bucket, fileName)
    }
    private fun updateS3(file: File, fileName: String) {
        putS3(file, fileName)
    }
    private fun parseUrlToKey(url: String): String {
        return url.split("amazonaws.com/")[1]
    }

}