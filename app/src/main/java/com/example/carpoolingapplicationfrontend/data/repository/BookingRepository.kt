package com.example.carpoolingapplicationfrontend.data.repository

import android.util.Log
import com.example.carpoolingapplicationfrontend.data.api.BookingApiService
import com.example.carpoolingapplicationfrontend.data.models.AcceptMatchResponse
import com.example.carpoolingapplicationfrontend.data.models.ApiResponse
import com.example.carpoolingapplicationfrontend.data.models.BookingDetailResponse
import com.example.carpoolingapplicationfrontend.data.models.BookingLocationResponse
import com.example.carpoolingapplicationfrontend.data.models.BookingSearchRequest
import com.example.carpoolingapplicationfrontend.data.models.CreateBookingRequest
import com.example.carpoolingapplicationfrontend.data.models.CreateBookingResponse
import com.example.carpoolingapplicationfrontend.data.models.EndTripResponse
import com.example.carpoolingapplicationfrontend.data.models.MatchedBookingsResponse
import com.example.carpoolingapplicationfrontend.data.models.PageQueryRequest
import com.example.carpoolingapplicationfrontend.data.models.PrematchResponse
import com.example.carpoolingapplicationfrontend.data.models.RejectMatchResponse
import com.example.carpoolingapplicationfrontend.data.models.SaveLocationRequest
import com.example.carpoolingapplicationfrontend.data.models.SaveLocationResponse
import com.example.carpoolingapplicationfrontend.data.models.SearchBookingsResponse
import com.example.carpoolingapplicationfrontend.data.models.StartTripResponse
import com.example.carpoolingapplicationfrontend.data.models.UpdateBookingRequest
import com.example.carpoolingapplicationfrontend.data.models.UpdateBookingResponse
import com.example.carpoolingapplicationfrontend.data.models.UserBookingsResponse
import retrofit2.Response

class BookingRepository(
    private val bookingApiService: BookingApiService
) {
    suspend fun createBooking(request: CreateBookingRequest): Result<CreateBookingResponse> {
        return try {
            handleApiResponse(bookingApiService.createBooking(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBooking(request: UpdateBookingRequest): Result<UpdateBookingResponse> {
        return try {
            handleApiResponse(bookingApiService.updateBooking(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun startTrip(bookingId: String): Result<StartTripResponse> {
        return try {
            handleApiResponse(bookingApiService.startTrip(bookingId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun endTrip(bookingId: String): Result<EndTripResponse> {
        return try {
            handleApiResponse(bookingApiService.endTrip(bookingId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveLocation(request: SaveLocationRequest): Result<SaveLocationResponse> {
        return try {
            handleApiResponse(bookingApiService.saveLocation(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun rejectMatch(tripId: String, bookingId: String): Result<RejectMatchResponse> {
        return try {
            handleApiResponse(bookingApiService.rejectMatch(tripId, bookingId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun acceptMatch(tripId: String, bookingId: String): Result<AcceptMatchResponse> {
        return try {
            handleApiResponse(bookingApiService.acceptMatch(tripId, bookingId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchBookings(request: BookingSearchRequest): Result<SearchBookingsResponse> {
        return try {
            handleApiResponse(
                bookingApiService.searchBookings(
                    id = request.id,
                    userId = request.userId,
                    tripId = request.tripId,
                    status = request.status,
                    role = request.role,
                    expectedStartTimeStart = request.expectedStartTimeStart,
                    expectedStartTimeEnd = request.expectedStartTimeEnd,
                    pageNum = request.pageNum,
                    pageSize = request.pageSize,
                    totalSize = request.totalSize
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookingsByUserId(
        id: Long,
        request: PageQueryRequest
    ): Result<UserBookingsResponse> {
        return try {
            Log.d("BookingRepository", "User id: $id")
            handleApiResponse(
                bookingApiService.getBookingsByUserId(
                    id = id,
                    request = request
                )
            )
        } catch (e: Exception) {
            Log.d("BookingRepository", "Getting bookings for user id $id failed: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getBookingById(id: Long): Result<BookingDetailResponse> {
        return try {
            handleApiResponse(bookingApiService.getBookingById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPrematchBookings(tripId: String): Result<PrematchResponse> {
        return try {
            handleApiResponse(bookingApiService.getPrematchBookings(tripId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMatchedBookings(tripId: String): Result<MatchedBookingsResponse> {
        return try {
            handleApiResponse(bookingApiService.getMatchedBookings(tripId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLocation(bookingId: String): Result<BookingLocationResponse> {
        return try {
            handleApiResponse(bookingApiService.getLocation(bookingId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun <T : ApiResponse<*>> handleApiResponse(response: Response<T>): Result<T> {
        val body = response.body()

        return when {
            response.isSuccessful && body != null && body.code == 0 -> {
                Result.success(body)
            }
            response.isSuccessful && body != null -> {
                Result.failure(Exception(body.message))
            }
            else -> {
                Result.failure(Exception(response.message()))
            }
        }
    }
}
