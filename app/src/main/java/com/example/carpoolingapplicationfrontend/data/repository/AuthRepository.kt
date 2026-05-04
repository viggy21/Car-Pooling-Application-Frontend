package com.example.carpoolingapplicationfrontend.data.repository

import com.example.carpoolingapplicationfrontend.data.api.AuthApiService
import com.example.carpoolingapplicationfrontend.data.api.RetrofitClient
import com.example.carpoolingapplicationfrontend.data.models.ApiResponse
import com.example.carpoolingapplicationfrontend.data.models.LoginRequest
import com.example.carpoolingapplicationfrontend.data.models.LoginResponse
import com.example.carpoolingapplicationfrontend.data.models.RegisterRequest
import com.example.carpoolingapplicationfrontend.data.models.RegisterResponse
import retrofit2.Response

class AuthRepository(
    private val authApiService: AuthApiService = RetrofitClient.authApiService
) {
    suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            handleApiResponse(authApiService.register(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            handleApiResponse(authApiService.login(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAuth(): Result<String> {
        return try {
            val response = authApiService.getAuth()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception(response.message()))
            }
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
