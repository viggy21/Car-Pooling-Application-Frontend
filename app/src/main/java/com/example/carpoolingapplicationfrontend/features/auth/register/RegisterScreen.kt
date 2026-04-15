package com.example.carpoolingapplicationfrontend.features.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginViewModel
import com.example.carpoolingapplicationfrontend.navigation.Routes

@Composable
fun RegisterScreen (navController: NavController, viewModel: RegisterViewModel, modifier: Modifier = Modifier) {
    // TODO: make sex/gender a single input item

    // Variables
    val name by viewModel.name.observeAsState(initial = "")
    val phone by viewModel.phone.observeAsState(initial = "")
    val sex by viewModel.sex.observeAsState(initial = "")
    val email by viewModel.email.observeAsState(initial = "")
    val password by viewModel.password.observeAsState(initial = "")
    val isLoading = viewModel.isLoading.observeAsState()
    val registerResult by viewModel.registerResult.observeAsState()

    LaunchedEffect(registerResult) {
        registerResult?.let {
            // successful result means navigating to another page
            navController.navigate(Routes.testScreen)
        }
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Register", fontWeight = FontWeight.Bold)
        OutlinedTextField(value = name, onValueChange = {viewModel.onNameUpdate(it)}, singleLine = true, label = {
            Text(text = "Name")
        })

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = phone, onValueChange = {viewModel.onPhoneUpdate(it)}, singleLine = true, label = {
            Text(text = "Phone")
        })

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = sex, onValueChange = {viewModel.onSexUpdate(it)}, singleLine = true, label = {
            Text(text = "Sex")
        })

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
                viewModel.register()
            },
        ) {
            Text(text = "Confirm details")
        }
    }



}