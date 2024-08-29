package soma.achoom.zigg.s3.service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3Client
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class S3Service (
    val amazonS3Client: AmazonS3Client,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String
) {
    fun getPreSignedGetUrl(objectName: String): String {
        return amazonS3Client.generatePresignedUrl(bucket, objectName,DateTime.now().plusMinutes(10).toDate(),HttpMethod.GET).toString()
    }

    fun getPreSignedPutUrl(objectName: String): String {
        return amazonS3Client.generatePresignedUrl(bucket, objectName, DateTime.now().plusMinutes(10).toDate(),HttpMethod.PUT).toString()
    }

    fun convertPreSignedUrlToGeneralKey(preSignedUrl: String): String {
        return preSignedUrl.split("?")[0]
    }
}