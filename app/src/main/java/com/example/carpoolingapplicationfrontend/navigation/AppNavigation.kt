package com.example.carpoolingapplicationfrontend.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.carpoolingapplicationfrontend.ui.screens.CreateRideScreen
import com.example.carpoolingapplicationfrontend.ui.screens.ForgotPasswordScreen
import com.example.carpoolingapplicationfrontend.ui.screens.HomeScreen
import com.example.carpoolingapplicationfrontend.ui.screens.LoginScreen
import com.example.carpoolingapplicationfrontend.ui.screens.MainScreen
import com.example.carpoolingapplicationfrontend.ui.screens.MatchDetailsScreen
import com.example.carpoolingapplicationfrontend.ui.screens.MyRidesScreen
import com.example.carpoolingapplicationfrontend.ui.screens.ProfileScreen
import com.example.carpoolingapplicationfrontend.ui.screens.RegisterScreen
import com.example.carpoolingapplicationfrontend.viewmodel.AuthViewModel
import com.example.carpoolingapplicationfrontend.viewmodel.BookingViewModel
import com.example.carpoolingapplicationfrontend.ui.screens.RideDetailsScreen
import com.example.carpoolingapplicationfrontend.viewmodel.BookingRequestUiState
import com.example.carpoolingapplicationfrontend.viewmodel.RideDetailsUiModel
import com.example.carpoolingapplicationfrontend.viewmodel.toRideDetailsUiModel

@Composable
fun App() {
    val navController = rememberNavController()
    //AppNavHost(navController = navController)
    MainScreen(navController = navController)
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    paddingValues: PaddingValues,
    authViewModel: AuthViewModel
) {
    val bookingViewModel: BookingViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        composable(Screen.Home.route) {
            //val bookingViewModel: BookingViewModel = viewModel()

            HomeScreen(
                viewModel = bookingViewModel,
                currentUserId = authViewModel.getCurrentUserId() ?: -1L,
                onViewRideDetails = { rideId ->
                    navController.navigate(Screen.RideDetails.createRoute(rideId))
                },
                onViewMatch = { matchId ->
                    navController.navigate(Screen.MatchDetails.createRoute(matchId))
                },
                onOfferRide = {
                    navController.navigate(Screen.CreateRide.route)
                },
                onRequestRide = {
                    navController.navigate(Screen.CreateRide.route)
                },
                onViewMyRides = {
                    navController.navigate(Screen.MyRides.route)
                }
            )
        }

        composable(
            route = Screen.MatchDetails.route,
            arguments = listOf(navArgument("matchId") { type = NavType.StringType })
        ) { backStackEntry ->
            //val bookingViewModel: BookingViewModel = viewModel()
            val matchId = backStackEntry.arguments?.getString("matchId").orEmpty()

            MatchDetailsScreen(
                matchId = matchId,
                viewModel = bookingViewModel,
                onAcceptClick = {
                    navController.popBackStack()
                },
                onDeclineClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

//        composable(
//            route = Screen.RideDetails.route,
//            arguments = listOf(
//                navArgument("rideId") {
//                    type = NavType.StringType
//                }
//            )
//        ) { backStackEntry ->
//
//            val rideId =
//                backStackEntry.arguments?.getString("rideId") ?: ""
//
////            RideDetailsScreen(
////                rideId = rideId,
////                onBackClick = {
////                    navController.popBackStack()
////                }
////            )
//            //val bookingViewModel: BookingViewModel = viewModel()
//
//            LaunchedEffect(rideId) {
//                bookingViewModel.getBookingById(rideId.toLong())
//            }
//
////            val rideState = bookingViewModel.bookingDetailState
////
////            val rideUiModel = bookingViewModel.getRideByIdUiModel(rideState)
//            val rideState = bookingViewModel.bookingDetailState.collectAsState().value
//            val rideUiModel = bookingViewModel.getRideByIdUiModel(
//                BookingRequestUiState.Success(rideState.response)
//            )
//
//            RideDetailsScreen(
//                rideId = rideId,
//                ride = rideUiModel,
//                onBackClick = {
//                    navController.popBackStack()
//                }
//            )
//        }
        composable(
            route = Screen.RideDetails.route,
            arguments = listOf(
                navArgument("rideId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val rideId = backStackEntry.arguments?.getString("rideId") ?: ""

            LaunchedEffect(rideId) {
                bookingViewModel.getBookingById(rideId.toLong())
            }

            val rideState = bookingViewModel.bookingDetailState.collectAsState().value

            val ride: RideDetailsUiModel? = when (rideState) {

                is BookingRequestUiState.Success -> {
                    rideState.response.data?.toRideDetailsUiModel()
                }

                else -> null
            }

            RideDetailsScreen(
                rideId = rideId,
                ride = ride,
                bookingViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) {
                            inclusive = true
                        }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen()
        }

        composable(Screen.CreateRide.route) {
            //val bookingViewModel: BookingViewModel = viewModel()

            CreateRideScreen(
                viewModel = bookingViewModel,
                userId = authViewModel.getCurrentUserId(),
                onBackClick = {
                    navController.popBackStack()
                },
                onRideCreated = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.MyRides.route) {
//            MyRidesScreen(
//                onRideClick = { rideId ->
//                    navController.navigate(Screen.RideDetails.createRoute(rideId))
//                }
//            )
            MyRidesScreen(
                currentUserId = authViewModel.getCurrentUserId() ?: -1L,
                viewModel = bookingViewModel,
                onRideClick = { rideId ->
                    navController.navigate(Screen.RideDetails.createRoute(rideId))
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                authViewModel = authViewModel,
                onEditProfileClick = {

                },
                onVehicleInfoClick = {

                },
                onChangePasswordClick = {

                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
