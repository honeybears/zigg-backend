package soma.achoom.zigg.s3.service

enum class S3DataType(val path:String) {
    HISTORY_THUMBNAIL("thumbnail/history/"),
    HISTORY_VIDEO("video/history/"),
    SPACE_IMAGE("image/space/"),
    SPACE_REFERENCE_VIDEO("reference/"),
    USER_PROFILE_IMAGE("profile/");

    fun getBucketName():String{
        return path
    }
}