package com.example.carpoolingapplicationfrontend.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
}

object Routes {
    const val loginScreen = "login_screen"
    const val registerScreen = "register_screen"
    const val testScreen = "test_screen"
    const val bookingListScreen = "booking_list_screen"
}
