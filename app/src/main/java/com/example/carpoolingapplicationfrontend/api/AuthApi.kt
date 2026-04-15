package com.example.carpoolingapplicationfrontend.api

import com.example.carpoolingapplicationfrontend.features.auth.login.LoginRequest
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginResponse
import com.example.carpoolingapplicationfrontend.features.auth.register.RegisterRequest
import com.example.carpoolingapplicationfrontend.features.auth.register.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("user/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response <RegisterResponse>

    @POST("user/auth/login")
    suspend fun login(@Body request: LoginRequest): Response <LoginResponse>
}