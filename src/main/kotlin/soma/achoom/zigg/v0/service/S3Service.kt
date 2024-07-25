package soma.achoom.zigg.v0.service

import com.amazonaws.services.s3.AmazonS3
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import soma.achoom.zigg.v0.model.enums.S3Option

@Service
class S3Service @Autowired constructor(
    private val amazonS3: AmazonS3,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String
) {
    fun uploadFile(multipartFile: MultipartFile, identifyKey:Long , s3Option: S3Option):String {
        return when (s3Option) {
            S3Option.HISTORY_VIDEO -> {
                putS3(multipartFile, "history_video/$identifyKey")
            }
            S3Option.USER_IMAGE -> putS3(multipartFile, "user_image/$identifyKey")
            S3Option.SPACE_IMAGE -> putS3(multipartFile, "space_image/$identifyKey")
            S3Option.SPACE_REFERENCE_VIDEO -> putS3(multipartFile, "space_reference_video/$identifyKey")
        }
    }

    private fun putS3(multipartFile: MultipartFile, fileName: String): String {
        amazonS3.putObject(bucket, fileName, multipartFile.inputStream, null)
        return amazonS3.getUrl(bucket, fileName).toString()
    }
    private fun removeS3(fileName: String) {
        amazonS3.deleteObject(bucket, fileName)
    }
    private fun updateS3(multipartFile: MultipartFile, fileName: String) {
        removeS3(fileName)
        putS3(multipartFile, fileName)
    }
}