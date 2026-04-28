package com.example.carpoolingapplicationfrontend.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carpoolingapplicationfrontend.MainActivity
import com.example.carpoolingapplicationfrontend.features.TestScreen
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginScreen
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginViewModel
import com.example.carpoolingapplicationfrontend.features.auth.register.RegisterScreen
import com.example.carpoolingapplicationfrontend.features.auth.register.RegisterViewModel
import com.example.carpoolingapplicationfrontend.features.booking.BookingListScreen
import com.example.carpoolingapplicationfrontend.features.booking.BookingListViewModel

@Composable
fun AppNavigation(activity : MainActivity,modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // Create view models
    val loginViewModel = ViewModelProvider(activity)[LoginViewModel::class.java]
    val registerViewModel = ViewModelProvider(activity)[RegisterViewModel::class.java]
    val bookingListViewModel = ViewModelProvider(activity)[BookingListViewModel::class.java]

    NavHost(navController = navController, startDestination = Routes.loginScreen, builder = {
        composable(Routes.loginScreen) {
            LoginScreen(navController = navController, viewModel = loginViewModel)
        }
        composable(Routes.registerScreen) {
            RegisterScreen(navController = navController, viewModel = registerViewModel)
        }
        composable(Routes.testScreen) {
            TestScreen()
        }
        composable(Routes.bookingListScreen) {
            BookingListScreen(navController = navController, viewModel = bookingListViewModel)
        }
    })
}