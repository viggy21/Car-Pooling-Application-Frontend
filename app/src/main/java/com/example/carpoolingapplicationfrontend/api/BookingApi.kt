package com.example.carpoolingapplicationfrontend.api

import com.example.carpoolingapplicationfrontend.features.auth.login.LoginRequest
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginResponse
import com.example.carpoolingapplicationfrontend.features.auth.register.RegisterRequest
import com.example.carpoolingapplicationfrontend.features.auth.register.RegisterResponse
import com.example.carpoolingapplicationfrontend.features.booking.ApiResponse
import com.example.carpoolingapplicationfrontend.features.booking.BookingDto
import com.example.carpoolingapplicationfrontend.features.booking.PageResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BookingApi {
    @GET("api/user/booking/query/userid/{id}")
    suspend fun getBookings(
        @Path("id") userId: Long,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int,
        @Query("userId") queryUserId: Long
    ): ApiResponse<PageResult<BookingDto>>
}