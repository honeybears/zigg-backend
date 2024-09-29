package soma.achoom.zigg.s3.controller

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import soma.achoom.zigg.s3.service.S3Service

@RestController
@RequestMapping("/api/v0/contents")
class ContentController(
    private val s3Service: S3Service
) {

//    @PostMapping
//    fun generateS3ContentEndpoint(authentication: Authentication,@RequestParam type:String) {
//        s3Service.generateS3ContentEndpoint(authentication,type)
//    }



}