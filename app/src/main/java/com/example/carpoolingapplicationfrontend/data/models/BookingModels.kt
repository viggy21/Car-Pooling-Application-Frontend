package com.example.carpoolingapplicationfrontend.data.models

data class CreateBookingRequest(
    val id: Long = 0,
    val userId: Long,
    val role: Int,
    val originName: String,
    val originLat: Double,
    val originLng: Double,
    val destName: String,
    val destLat: Double,
    val destLng: Double,
    val freeSeat: Int,
    val expectedStartTime: String,
    val remark: String
)

data class UpdateBookingRequest(
    val id: Long,
    val userId: Long,
    val role: Int,
    val originName: String,
    val originLat: Double,
    val originLng: Double,
    val destName: String,
    val destLat: Double,
    val destLng: Double,
    val freeSeat: Int,
    val expectedStartTime: String,
    val remark: String
)

data class SaveLocationRequest(
    val bookingId: Long,
    val longitude: Double,
    val latitude: Double
)

data class BookingSearchRequest(
    val id: Long? = null,
    val userId: Long? = null,
    val tripId: Long? = null,
    val status: Int? = null,
    val role: Int? = null,
    val expectedStartTimeStart: String? = null,
    val expectedStartTimeEnd: String? = null,
    val pageNum: Int,
    val pageSize: Int,
    val totalSize: Int? = null
)

data class PageQueryRequest(
    val pageNum: Int,
    val pageSize: Int,
    val totalSize: Int? = null,
    val userId: Long
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

data class PageResult<T>(
    val totalNum: Int,
    val data: List<T>
)

data class BookingLocation(
    val bookingId: Long,
    val longitude: Double,
    val latitude: Double
)

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
)

object BookingStatus {
    const val REQUESTED = 0
    const val MATCHED = 1
    const val ONGOING = 2
    const val COMPLETED = 3
    const val CANCELLED = 4
}

typealias CreateBookingResponse = ApiResponse<Map<String, Any>>
typealias UpdateBookingResponse = ApiResponse<Map<String, Any>>
typealias StartTripResponse = ApiResponse<Map<String, Any>>
typealias EndTripResponse = ApiResponse<Map<String, Any>>
typealias SaveLocationResponse = ApiResponse<Map<String, Any>>
typealias RejectMatchResponse = ApiResponse<Map<String, Any>>
typealias AcceptMatchResponse = ApiResponse<Map<String, Any>>
typealias SearchBookingsResponse = ApiResponse<PageResult<BookingDto>>
typealias UserBookingsResponse = ApiResponse<PageResult<BookingDto>>
typealias BookingDetailResponse = ApiResponse<BookingDto>
typealias PrematchResponse = ApiResponse<List<String>>
typealias MatchedBookingsResponse = ApiResponse<List<BookingDto>>
typealias BookingLocationResponse = ApiResponse<BookingLocation>
