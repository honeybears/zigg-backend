package soma.achoom.zigg.global.infra.gcs

enum class GCSDataType (val path:String){
    HISTORY_THUMBNAIL("history/thumbnail/"),
    HISTORY_VIDEO("history/video/"),
    SPACE_IMAGE("space/image/"),
    SPACE_REFERENCE_VIDEO("space/reference/video/"),
    USER_PROFILE_IMAGE("user/profile/image/");

    fun getBucketName():String{
        return path
    }
}