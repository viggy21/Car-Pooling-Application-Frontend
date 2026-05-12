package com.example.carpoolingapplicationfrontend.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")

    object CreateRide : Screen("create_ride")

    object MyRides : Screen("my_rides")

    object Profile : Screen("profile")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object MatchDetails : Screen("match_details/{matchId}") {
        fun createRoute(matchId: String): String {
            return "match_details/$matchId"
        }
    }

    object RideDetails : Screen("ride_details/{rideId}") {
        fun createRoute(rideId: String): String {
            return "ride_details/$rideId"
        }
    }
}

object Routes {
    const val loginScreen = "login_screen"
    const val registerScreen = "register_screen"
    const val testScreen = "test_screen"
    const val bookingListScreen = "booking_list_screen"
}
