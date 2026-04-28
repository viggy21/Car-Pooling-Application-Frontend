package com.example.carpoolingapplicationfrontend.features.booking

import com.example.carpoolingapplicationfrontend.api.RetrofitInstance
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginRequest
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

class BookingRepository {

    private val bookingApi = RetrofitInstance.bookingApi

    suspend fun getBookings(userId: Long, page: Int, pageSize: Int) :
            Result<List<BookingDto>> {
        return try {
            val response = bookingApi.getBookings(
                userId = userId,
                pageNum = page,
                pageSize = pageSize,
                queryUserId = userId
            )

            if (response.code == 0) {
                Result.success(response.data.items)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}