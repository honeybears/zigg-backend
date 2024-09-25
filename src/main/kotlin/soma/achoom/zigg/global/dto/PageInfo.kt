package soma.achoom.zigg.global.dto

data class PageInfo(
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int,
    val size: Int
) {

}