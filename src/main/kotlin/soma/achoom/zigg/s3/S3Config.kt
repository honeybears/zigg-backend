package soma.achoom.zigg.s3

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config {
    @Value("\${cloud.aws.credentials.accessKey}")
    private val accessKey: String? = null
    @Value("\${cloud.aws.credentials.secretKey}")
    private val secretKey: String? = null
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String? = null
    @Value("\${cloud.aws.s3.region}")
    private val region: String? = null

    @Bean
    fun amazonS3Client(): AmazonS3Client {
        val credential = createAWSCredentials()
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(credential))
            .build() as AmazonS3Client
    }

    private fun createAWSCredentials(): BasicAWSCredentials {
        return BasicAWSCredentials(accessKey, secretKey)
    }
}