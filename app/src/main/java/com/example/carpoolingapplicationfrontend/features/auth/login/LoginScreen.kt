package com.example.carpoolingapplicationfrontend.features.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.carpoolingapplicationfrontend.R
import com.example.carpoolingapplicationfrontend.navigation.Routes

@Composable
fun LoginScreen (navController: NavController, viewModel: LoginViewModel, modifier: Modifier = Modifier) {
    // first just create the layout of the login screen
    // then include the view model to take the user input and call the back end
    // in the view model, call API request to validate user
    // then if:
    // - accept: route the user to the next screen
    // - reject: show the user an error message that their login details are incorrect

    // Variables
    val email by viewModel.email.observeAsState(initial = "")
    val password by viewModel.password.observeAsState(initial = "")
    //val userData = viewModel.userData.observeAsState()
    val isLoading = viewModel.isLoading.observeAsState()
    val loginResult by viewModel.loginResult.observeAsState()

    LaunchedEffect(loginResult) {
        loginResult?.let {
            // successful result means navigating to another page
            navController.navigate(Routes.testScreen)
        }
    }

    // UI Section
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // Welcome message and logo
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(Color.Green).fillMaxWidth().padding(16.dp)
        ) {
            Text(text = "Welcome to Moober!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Icon(painter = painterResource(id = R.drawable.directions_car_24px), contentDescription = null,
                modifier = Modifier.size(100.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Email address and password input
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = email, onValueChange = {viewModel.onEmailUpdate(it)}, singleLine = true, label = {
                Text(text = "Email address")
            })

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = password, onValueChange = {viewModel.onPasswordUpdate(it)}, singleLine = true, label = {
                Text(text = "Password")
            }, visualTransformation = PasswordVisualTransformation())

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Use the email and password with the viewmodel to log in
                    viewModel.login()
                },
                ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(onClick = {
                // Redirect the user to the registration page
                navController.navigate(Routes.registerScreen)
            }) {
                Text(text = "Sign Up")
            }

            if (isLoading.value == true) {
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.height(32.dp))


            Text(text = "Forgot Password?", modifier = Modifier.clickable {

            })
        }
    }
}

//@Preview
//@Composable
//private fun LoginScreenPreview() {
//    LoginScreen()
//}