package com.example.carpoolingapplicationfrontend.features.booking

import com.google.gson.annotations.SerializedName

//data class PageQueryDTO(
//    val pageNum: Int,
//    val pageSize: Int,
//    val totalSize: Int? = null, // usually not needed when sending
//    val userId: Long
//)

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T
)

data class PageResult<T>(
    @SerializedName("totalNum")
    val total: Int,

    @SerializedName("data")
    val items: List<T>
)

data class BookingDto(
    val id: Long,
    val tripId: Long,
    val userId: Long,
    val role: Int,
    val originName: String,
    val originLat: Double,
    val originLng: Double,
    val destName: String,
    val destLat: Double,
    val destLng: Double,
    val expectedStartTime: String,
    val status: Int,
    val remark: String?,
    val createTime: String
)