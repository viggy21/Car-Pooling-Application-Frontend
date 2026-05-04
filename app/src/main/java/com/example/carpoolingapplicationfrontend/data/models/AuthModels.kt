package com.example.carpoolingapplicationfrontend.data.models

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val vehicleMake: String,
    val vehicleModel: String,
    val vehiclePlate: String
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
