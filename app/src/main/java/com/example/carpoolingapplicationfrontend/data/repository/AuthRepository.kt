package com.example.carpoolingapplicationfrontend.data.repository

import com.example.carpoolingapplicationfrontend.data.api.AuthApiService
import com.example.carpoolingapplicationfrontend.data.models.ApiResponse
import com.example.carpoolingapplicationfrontend.data.models.AuthUser
import com.example.carpoolingapplicationfrontend.data.models.CheckMailCodeRequest
import com.example.carpoolingapplicationfrontend.data.models.CheckMailCodeResponse
import com.example.carpoolingapplicationfrontend.data.models.LoginRequest
import com.example.carpoolingapplicationfrontend.data.models.RegisterRequest
import com.example.carpoolingapplicationfrontend.data.models.RegisterResponse
import com.example.carpoolingapplicationfrontend.data.models.ResetPasswordRequest
import com.example.carpoolingapplicationfrontend.data.models.ResetPasswordResponse
import com.example.carpoolingapplicationfrontend.data.models.SendMailCodeRequest
import com.example.carpoolingapplicationfrontend.data.models.SendMailCodeResponse
import retrofit2.Response

class AuthRepository(
    private val authApiService: AuthApiService
) {
    suspend fun login(email: String, password: String): Result<AuthUser> {
        return try {
            val response = authApiService.login(LoginRequest(email = email, password = password))
            val body = response.body()
            val loginData = body?.data

            when {
                response.isSuccessful && body != null && body.code == 0 && loginData != null -> {
                    Result.success(
                        AuthUser(
                            userId = loginData.id,
                            token = loginData.token,
                            name = loginData.name
                        )
                    )
                }
                response.isSuccessful && body != null -> {
                    Result.failure(Exception(body.message))
                }
                else -> {
                    Result.failure(Exception(response.message()))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            handleApiResponse(authApiService.register(request))
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

    suspend fun resetPassword(request: ResetPasswordRequest): Result<ResetPasswordResponse> {
        return try {
            handleApiResponse(
                authApiService.resetPassword(
                    email = request.email,
                    pwd = request.pwd
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendMailCode(request: SendMailCodeRequest): Result<SendMailCodeResponse> {
        return try {
            handleApiResponse(
                authApiService.sendMailCode(
                    mailAddr = request.mailAddr
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkMailCode(request: CheckMailCodeRequest): Result<CheckMailCodeResponse> {
        return try {
            handleApiResponse(
                authApiService.checkMailCode(
                    mailAddr = request.mailAddr,
                    code = request.code
                )
            )
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
