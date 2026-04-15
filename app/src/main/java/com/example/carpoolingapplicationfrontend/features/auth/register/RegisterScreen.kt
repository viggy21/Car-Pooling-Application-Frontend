package com.example.carpoolingapplicationfrontend.features.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun RegisterScreen (modifier: Modifier = Modifier) {
    // first just create the layout of the login screen
    // then include the view model to take the user input and call the back end
    // in the view model, call API request to validate user
    // then if:
    // - accept: route the user to the next screen
    // - reject: show the user an error message that their login details are incorrect

    // Variables
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    // Layout
//    Column (
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        // Welcome message and logo
//        Column (
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.background(Color.Green).fillMaxWidth().padding(16.dp)
//        ) {
//            Text(text = "Welcome to Moober!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
//            Icon(painter = painterResource(id = R.drawable.directions_car_24px), contentDescription = null,
//                modifier = Modifier.size(100.dp))
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Email address and password input
//        Column (
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text("Login", fontWeight = FontWeight.SemiBold)
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            OutlinedTextField(value = email, onValueChange = {email = it}, singleLine = true, label = {
//                Text(text = "Email address")
//            })
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            OutlinedTextField(value = password, onValueChange = {password = it}, singleLine = true, label = {
//                Text(text = "Password")
//            }, visualTransformation = PasswordVisualTransformation())
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Button(
//                onClick = {
//                    // Use the email and password with the viewmodel to log in
//                },
//            ) {
//                Text(text = "Login")
//            }
//
//            Spacer(modifier = Modifier.height(4.dp))
//
//            Button(onClick = {
//                // Redirect the user to the registration page
//            }) {
//                Text(text = "Sign Up")
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//
//            Text(text = "Forgot Password?", modifier = Modifier.clickable {
//
//            })
//        }
//    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Register", fontWeight = FontWeight.Bold)
        OutlinedTextField()
    }


    Text("Welcome to the sign up page")
}