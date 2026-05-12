package com.example.carpoolingapplicationfrontend.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.carpoolingapplicationfrontend.navigation.AppNavHost
import com.example.carpoolingapplicationfrontend.navigation.Screen
import com.example.carpoolingapplicationfrontend.ui.components.BottomNavBar
import com.example.carpoolingapplicationfrontend.viewmodel.AuthViewModel

@Composable
fun MainScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isAuthRoute = currentRoute == Screen.Login.route ||
        currentRoute == Screen.Register.route ||
        currentRoute == Screen.ForgotPassword.route

    LaunchedEffect(authState.isLoading, authState.isLoggedIn, currentRoute) {
        if (!authState.isLoading) {
            when {
                authState.isLoggedIn && (currentRoute == null || isAuthRoute) -> {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
                !authState.isLoggedIn && !isAuthRoute -> {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    val showBottomBar = authState.isLoggedIn && shouldShowBottomBar(currentRoute)

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController)
            }
        }
    ) { padding ->

        AppNavHost(
            navController = navController,
            paddingValues = padding,
            authViewModel = authViewModel
        )
    }
}


private fun shouldShowBottomBar(route: String?): Boolean {
    return when (route) {

        Screen.Login.route -> false
        Screen.Register.route -> false
        Screen.ForgotPassword.route -> false

        null -> false

        else -> true
    }
}
