package com.example.carpoolingapplicationfrontend.features.auth.register

data class RegisterRequest(
    val name: String,
    val phone: String,
    val sex: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val code: String,
    val message: String,
    val `data`: String // TODO: this shouldn't be a string, it should be {}
)
