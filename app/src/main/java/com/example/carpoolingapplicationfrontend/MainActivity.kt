package com.example.carpoolingapplicationfrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.carpoolingapplicationfrontend.navigation.App
import com.example.carpoolingapplicationfrontend.ui.theme.CarPoolingApplicationFrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        enableEdgeToEdge()

        setContent {
            CarPoolingApplicationFrontendTheme {
                App()
            }
        }
    }
}
