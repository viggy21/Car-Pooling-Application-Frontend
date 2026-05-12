package com.example.carpoolingapplicationfrontend.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.carpoolingapplicationfrontend.navigation.Screen

@Composable
fun BottomNavBar(navController: NavController) {

    val items = listOf(
        Screen.Home,
        Screen.CreateRide,
        Screen.MyRides,
        Screen.Profile
    )

    NavigationBar {
        val currentRoute = navController
            .currentBackStackEntryAsState().value?.destination?.route

        items.forEach { screen ->

            val icon = when (screen) {
                Screen.Home -> Icons.Default.Home
                Screen.CreateRide -> Icons.Default.AddCircle
                Screen.MyRides -> Icons.AutoMirrored.Filled.List
                Screen.Profile -> Icons.Default.Person
                else -> Icons.Default.Home
            }

            //val selected = currentRoute == screen.route
            val selected = when (screen) {
                Screen.Home -> currentRoute == Screen.Home.route
                Screen.CreateRide -> currentRoute == Screen.CreateRide.route
                Screen.MyRides -> currentRoute == Screen.MyRides.route
                Screen.Profile -> currentRoute == Screen.Profile.route
                else -> false
            }

            NavigationBarItem(
                //icon = { Icon(icon, contentDescription = screen.route) },
                //label = { Text(screen.route.replace("_", " ").capitalize()) },
                //selected = currentRoute == screen.route,
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
//                        popUpTo(Screen.Home.route) { saveState = true }
//                        launchSingleTop = true
//                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = screen.route,
                        tint = if (selected) AppPrimaryGreen else LocalContentColor.current
                    )
                },
                label = {
                    Text(
                        text = screen.route.replace("_", " ").replaceFirstChar {
                            it.uppercase()
                        },
                        color = if (selected) AppPrimaryGreen else LocalContentColor.current
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppPrimaryGreen,
                    selectedTextColor = AppPrimaryGreen,
                    unselectedIconColor = LocalContentColor.current,
                    unselectedTextColor = LocalContentColor.current,
                    indicatorColor = AppPrimaryGreen.copy(alpha = 0.12f)
                )

            )
        }
    }
}