package com.example.carpoolingapplicationfrontend.data.api

import com.example.carpoolingapplicationfrontend.data.models.CheckMailCodeResponse
import com.example.carpoolingapplicationfrontend.data.models.LoginRequest
import com.example.carpoolingapplicationfrontend.data.models.LoginResponse
import com.example.carpoolingapplicationfrontend.data.models.RegisterRequest
import com.example.carpoolingapplicationfrontend.data.models.RegisterResponse
import com.example.carpoolingapplicationfrontend.data.models.ResetPasswordResponse
import com.example.carpoolingapplicationfrontend.data.models.SendMailCodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiService {
    @POST("user/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("user/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("user/auth")
    suspend fun getAuth(): Response<String>

    @GET("user/auth/resetpwd")
    suspend fun resetPassword(
        @Query("email") email: String,
        @Query("pwd") pwd: String
    ): Response<ResetPasswordResponse>

    @GET("user/auth/mail")
    suspend fun sendMailCode(
        @Query("mailAddr") mailAddr: String
    ): Response<SendMailCodeResponse>

    @GET("user/auth/checkmailcode")
    suspend fun checkMailCode(
        @Query("mailAddr") mailAddr: String,
        @Query("code") code: String
    ): Response<CheckMailCodeResponse>
}
