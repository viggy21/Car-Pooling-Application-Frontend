package com.example.carpoolingapplicationfrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginViewModel
import com.example.carpoolingapplicationfrontend.features.navigation.AppNavigation
import com.example.carpoolingapplicationfrontend.ui.theme.CarPoolingApplicationFrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        enableEdgeToEdge()

        setContent {
            CarPoolingApplicationFrontendTheme {
                AppNavigation(this)
            }
        }
    }
}

