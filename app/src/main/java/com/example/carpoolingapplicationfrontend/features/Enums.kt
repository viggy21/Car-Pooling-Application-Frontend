package com.example.carpoolingapplicationfrontend.features

enum class BookingStatus {
    PENDING, CONFIRMED, CANCELLED
}

enum class UserRole {
    PASSENGER, DRIVER
}

enum class Gender(val value: Int) {
    MALE(0),
    FEMALE(1),
    PREFER_NOT_TO_SAY(2)
}