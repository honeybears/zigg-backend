package soma.achoom.zigg.s3.service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3Client
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.history.dto.UploadContentTypeRequestDto
import java.util.*

@Service
class S3Service (
    val amazonS3Client: AmazonS3Client,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String
) {
    @Transactional(readOnly = true)
    fun getPreSignedGetUrl(objectName: String): String {
        return amazonS3Client.generatePresignedUrl(bucket, objectName,DateTime.now().plusMinutes(10).toDate(),HttpMethod.GET).toString()
    }
    @Transactional(readOnly = true)
    fun getPreSignedPutUrl(objectType:S3DataType, id: UUID,uploadContentTypeRequestDto: UploadContentTypeRequestDto): String {
        val objectName = objectType.path+id.toString()+"."+uploadContentTypeRequestDto.fileExtension
        return amazonS3Client.generatePresignedUrl(bucket, objectName, DateTime.now().plusMinutes(10).toDate(),HttpMethod.PUT).toString()
    }

}