package soma.achoom.zigg.global.infra.gcs

enum class GCSDataType (val path:String){
    HISTORY_THUMBNAIL("thumbnail/history/"),
    HISTORY_VIDEO("video/history/"),
    SPACE_IMAGE("image/space/"),
    SPACE_REFERENCE_VIDEO("reference/"),
    USER_PROFILE_IMAGE("image/profile/");

    fun getBucketName():String{
        return path
    }
}