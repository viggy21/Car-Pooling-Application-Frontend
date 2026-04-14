package com.example.carpoolingapplicationfrontend.features.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carpoolingapplicationfrontend.MainActivity
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginScreen
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginViewModel
import com.example.carpoolingapplicationfrontend.features.auth.register.RegisterScreen

@Composable
fun AppNavigation(activity : MainActivity,modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // Create view models
    val loginViewModel = ViewModelProvider(activity)[LoginViewModel::class.java]

    NavHost(navController = navController, startDestination = Routes.loginScreen, builder = {
        composable(Routes.loginScreen) {
            LoginScreen(navController = navController, viewModel = loginViewModel)
        }
        composable(Routes.registerScreen) {
            RegisterScreen()
        }
    })
}