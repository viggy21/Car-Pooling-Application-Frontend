package com.example.carpoolingapplicationfrontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carpoolingapplicationfrontend.data.models.RegisterRequest
import com.example.carpoolingapplicationfrontend.data.models.RegisterResponse
import com.example.carpoolingapplicationfrontend.ui.components.AppPrimaryGreen
import com.example.carpoolingapplicationfrontend.ui.components.CarIcon
import com.example.carpoolingapplicationfrontend.ui.components.EmailIcon
import com.example.carpoolingapplicationfrontend.ui.components.LockIcon
import com.example.carpoolingapplicationfrontend.ui.components.LoginErrorMessage
import com.example.carpoolingapplicationfrontend.ui.components.LoginInputField
import com.example.carpoolingapplicationfrontend.ui.components.LoginLogo
import com.example.carpoolingapplicationfrontend.ui.components.PersonIcon
import com.example.carpoolingapplicationfrontend.viewmodel.AuthUiState
import com.example.carpoolingapplicationfrontend.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val registerState by viewModel.registerState.collectAsState()
    val isLoading = registerState is AuthUiState.Loading

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var vehicleMake by remember { mutableStateOf("") }
    var vehicleModel by remember { mutableStateOf("") }
    var vehiclePlate by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(registerState) {
        if (registerState is AuthUiState.Success<RegisterResponse>) {
            onRegisterSuccess()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(44.dp))

            Column(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RegisterHeader()

                Spacer(modifier = Modifier.height(24.dp))

                RegisterForm(
                    fullName = fullName,
                    onFullNameChange = {
                        fullName = it
                        localError = null
                    },
                    email = email,
                    onEmailChange = {
                        email = it
                        localError = null
                    },
                    password = password,
                    onPasswordChange = {
                        password = it
                        localError = null
                    },
                    confirmPassword = confirmPassword,
                    onConfirmPasswordChange = {
                        confirmPassword = it
                        localError = null
                    },
                    vehicleMake = vehicleMake,
                    onVehicleMakeChange = { vehicleMake = it },
                    vehicleModel = vehicleModel,
                    onVehicleModelChange = { vehicleModel = it },
                    vehiclePlate = vehiclePlate,
                    onVehiclePlateChange = { vehiclePlate = it },
                    isLoading = isLoading,
                    errorMessage = localError ?: (registerState as? AuthUiState.Error)?.message,
                    onRegisterClick = {
                        val validationError = viewModel.validateRegisterInput(
                            email = email,
                            password = password,
                            confirmPassword = confirmPassword
                        )

                        if (validationError == null) {
                            localError = null
                            viewModel.register(
                                RegisterRequest(
                                    fullName.trim(),
                                    email.trim(),
                                    password,
                                    vehicleMake.trim(),
                                    vehicleModel.trim(),
                                    vehiclePlate.trim()
                                )
                            )
                        } else {
                            localError = validationError
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                LoginPrompt(onLoginClick = onLoginClick)

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun RegisterHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginLogo()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Create Account",
            color = Color(0xFF111827),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Join the green movement",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun RegisterForm(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    vehicleMake: String,
    onVehicleMakeChange: (String) -> Unit,
    vehicleModel: String,
    onVehicleModelChange: (String) -> Unit,
    vehiclePlate: String,
    onVehiclePlateChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LoginInputField(
            label = "Full Name",
            value = fullName,
            onValueChange = onFullNameChange,
            placeholder = "John Doe",
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = PersonIcon
        )

        LoginInputField(
            label = "Monash Email",
            value = email,
            onValueChange = onEmailChange,
            placeholder = "your.name@monash.edu",
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = EmailIcon
        )

        LoginInputField(
            label = "Password",
            value = password,
            onValueChange = onPasswordChange,
            placeholder = "Enter password",
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = LockIcon,
            visualTransformation = PasswordVisualTransformation()
        )

        LoginInputField(
            label = "Confirm Password",
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            placeholder = "Confirm password",
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = LockIcon,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Vehicle Information (Optional)",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )

        LoginInputField(
            label = "Vehicle Make",
            value = vehicleMake,
            onValueChange = onVehicleMakeChange,
            placeholder = "Make (e.g., Toyota)",
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = CarIcon
        )

        LoginInputField(
            label = "Vehicle Model",
            value = vehicleModel,
            onValueChange = onVehicleModelChange,
            placeholder = "Model (e.g., Camry)",
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        LoginInputField(
            label = "License Plate",
            value = vehiclePlate,
            onValueChange = onVehiclePlateChange,
            placeholder = "License Plate",
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        if (errorMessage != null) {
            LoginErrorMessage(message = errorMessage)
        }

        Button(
            onClick = onRegisterClick,
            enabled = !isLoading,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppPrimaryGreen,
                contentColor = Color.White,
                disabledContainerColor = AppPrimaryGreen.copy(alpha = 0.58f),
                disabledContentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text(
                    text = "Register",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun LoginPrompt(
    onLoginClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = buildAnnotatedString {
                append("Already have an account? ")
                withStyle(
                    SpanStyle(
                        color = AppPrimaryGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append("Log In")
                }
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.clickable(onClick = onLoginClick)
        )
    }
}
