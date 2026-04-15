package com.example.carpoolingapplicationfrontend.features.auth.login

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val code: Int,
    val `data`: SessionData,
    val message: String
)

data class SessionData(
    val id: Int,
    val name: String,
    val token: String
)