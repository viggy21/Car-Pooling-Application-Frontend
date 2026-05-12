package com.example.carpoolingapplicationfrontend.data.models

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val vehicleMake: String?,
    val vehicleModel: String?,
    val vehiclePlate: String?
)

typealias RegisterResponse = ApiResponse<Map<String, Any>>

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginData(
    val id: Long,
    val name: String,
    val token: String
)

typealias LoginResponse = ApiResponse<LoginData>

data class ResetPasswordRequest(
    val email: String,
    val pwd: String
)

data class SendMailCodeRequest(
    val mailAddr: String
)

data class CheckMailCodeRequest(
    val mailAddr: String,
    val code: String
)

typealias ResetPasswordResponse = ApiResponse<Map<String, Any>>
typealias SendMailCodeResponse = ApiResponse<Map<String, Any>>
typealias CheckMailCodeResponse = ApiResponse<Boolean>
