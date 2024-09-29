package soma.achoom.zigg.s3.dto


data class FileMetaDataRequestDto(
    val fileName: String,
    val fileType: String,
    val fileSize: Long,
    val duration: String? = null
)