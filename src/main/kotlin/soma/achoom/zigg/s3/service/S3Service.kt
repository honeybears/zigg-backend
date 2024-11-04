package soma.achoom.zigg.s3.service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3Client
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.history.dto.UploadContentTypeRequestDto
import soma.achoom.zigg.user.service.UserService
import java.util.*

@Service
class S3Service(
    val amazonS3Client: AmazonS3Client,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
) {
    @Transactional(readOnly = true)
    fun getPreSignedGetUrl(objectName: String?): String {
        if (objectName == null) {
            return ""
        }
        return amazonS3Client.generatePresignedUrl(bucket, objectName,DateTime.now().plusMinutes(10).toDate(),HttpMethod.GET).toString()
    }
    @Transactional(readOnly = true)
    fun getPreSignedPutUrl(objectType:S3DataType, id: UUID, uploadContentTypeRequestDto: UploadContentTypeRequestDto): String {
        val objectName = objectType.path+id.toString()+"."+uploadContentTypeRequestDto.fileExtension
        return amazonS3Client.generatePresignedUrl(bucket, objectName, DateTime.now().plusMinutes(10).toDate(),HttpMethod.PUT).toString()
    }
    @Transactional(readOnly = true)
    fun generateS3ContentEndpoint(authentication: Authentication, uploadContentTypeRequestDto: UploadContentTypeRequestDto,type: String) {
        when (type) {
            "history_video" ->{
                getPreSignedPutUrl(S3DataType.HISTORY_VIDEO, UUID.randomUUID(), uploadContentTypeRequestDto)
            }
            "history_thumbnail" ->{
                getPreSignedPutUrl(S3DataType.HISTORY_THUMBNAIL, UUID.randomUUID(),uploadContentTypeRequestDto)
            }
            "user_profile_image" ->{
                getPreSignedPutUrl(S3DataType.USER_PROFILE_IMAGE, UUID.randomUUID(), uploadContentTypeRequestDto)
            }
            "user_banner_image" ->{
                getPreSignedPutUrl(S3DataType.USER_BANNER_IMAGE, UUID.randomUUID(), uploadContentTypeRequestDto)
            }
            "space_image" ->{
                getPreSignedPutUrl(S3DataType.SPACE_IMAGE, UUID.randomUUID(), uploadContentTypeRequestDto)
            }
            "post_image" ->{
                getPreSignedPutUrl(S3DataType.POST_IMAGE, UUID.randomUUID(), uploadContentTypeRequestDto)
            }
            "post_video" ->{
                getPreSignedPutUrl(S3DataType.POST_VIDEO, UUID.randomUUID(), uploadContentTypeRequestDto)
            }
            else -> {
                throw IllegalArgumentException("Invalid type")
            }

        }

    }

}