package com.example.carpoolingapplicationfrontend.features.auth.login

import com.example.carpoolingapplicationfrontend.api.RetrofitInstance
import retrofit2.Response

class LoginRepository {

    private val authApi = RetrofitInstance.authApi

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        val request = LoginRequest(email, password)
        return authApi.login(request)
    }

}