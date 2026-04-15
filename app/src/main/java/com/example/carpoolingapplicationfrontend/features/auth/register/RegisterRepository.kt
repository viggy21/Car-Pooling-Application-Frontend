package com.example.carpoolingapplicationfrontend.features.auth.register

import com.example.carpoolingapplicationfrontend.api.RetrofitInstance
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginRequest
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginResponse
import retrofit2.Response

class RegisterRepository {

    private val authApi = RetrofitInstance.authApi

    suspend fun register(name: String, phone: String, sex: String, email: String, password: String): Response<RegisterResponse> {
        val request = RegisterRequest(name, phone, sex, email, password)
        return authApi.register(request)
    }

}