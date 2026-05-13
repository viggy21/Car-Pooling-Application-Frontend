package com.example.carpoolingapplicationfrontend.data.api

import com.example.carpoolingapplicationfrontend.data.models.AcceptMatchResponse
import com.example.carpoolingapplicationfrontend.data.models.BookingDetailResponse
import com.example.carpoolingapplicationfrontend.data.models.BookingLocationResponse
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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface BookingApiService {
    @POST("api/user/booking")
    suspend fun createBooking(
        @Body request: CreateBookingRequest
    ): Response<CreateBookingResponse>

    @PUT("api/user/booking")
    suspend fun updateBooking(
        @Body request: UpdateBookingRequest
    ): Response<UpdateBookingResponse>

    @PUT("api/user/booking/tripstart/{bookingId}")
    suspend fun startTrip(
        @Path("bookingId") bookingId: String
    ): Response<StartTripResponse>

    @PUT("api/user/booking/tripend/{bookingId}")
    suspend fun endTrip(
        @Path("bookingId") bookingId: String
    ): Response<EndTripResponse>

    @PUT("api/user/booking/savelocation")
    suspend fun saveLocation(
        @Body request: SaveLocationRequest
    ): Response<SaveLocationResponse>

    @GET("api/user/booking/rejectmatch/{tripId}/{bookingId}")
    suspend fun rejectMatch(
        @Path("tripId") tripId: String,
        @Path("bookingId") bookingId: String
    ): Response<RejectMatchResponse>

    @POST("api/user/booking/acceptmatch/{tripId}/{bookingId}")
    suspend fun acceptMatch(
        @Path("tripId") tripId: String,
        @Path("bookingId") bookingId: String
    ): Response<AcceptMatchResponse>

    @GET("api/user/booking/search")
    suspend fun searchBookings(
        @Query("id") id: Long?,
        @Query("userId") userId: Long?,
        @Query("tripId") tripId: Long?,
        @Query("status") status: Int?,
        @Query("role") role: Int?,
        @Query("expectedStartTimeStart") expectedStartTimeStart: String?,
        @Query("expectedStartTimeEnd") expectedStartTimeEnd: String?,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int,
        @Query("totalSize") totalSize: Int?
    ): Response<SearchBookingsResponse>

    @POST("api/user/booking/query/userid/{id}")
    suspend fun getBookingsByUserId(
        @Path("id") id: Long,
        @Body request: PageQueryRequest
    ): Response<UserBookingsResponse>

    @GET("api/user/booking/query/booking/{id}")
    suspend fun getBookingById(
        @Path("id") id: Long
    ): Response<BookingDetailResponse>

    @GET("api/user/booking/prematch/{tripId}")
    suspend fun getPrematchBookings(
        @Path("tripId") tripId: String
    ): Response<PrematchResponse>

    @GET("api/user/booking/matched/{tripId}")
    suspend fun getMatchedBookings(
        @Path("tripId") tripId: String
    ): Response<MatchedBookingsResponse>

    @GET("api/user/booking/getlocation/{bookingId}")
    suspend fun getLocation(
        @Path("bookingId") bookingId: String
    ): Response<BookingLocationResponse>
}
