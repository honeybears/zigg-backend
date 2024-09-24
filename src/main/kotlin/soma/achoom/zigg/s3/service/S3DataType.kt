package soma.achoom.zigg.s3.service

enum class S3DataType(val path:String) {
    HISTORY_THUMBNAIL("thumbnail/history/"),
    HISTORY_VIDEO("video/history/"),
    SPACE_IMAGE("image/space/"),
    USER_BANNER_IMAGE("banner/"),
    USER_PROFILE_IMAGE("profile/"),
    POST_IMAGE("image/post/"),
    POST_VIDEO("video/post/");

    fun getBucketName():String{
        return path
    }
}