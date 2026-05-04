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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.carpoolingapplicationfrontend.data.models.LoginRequest
import com.example.carpoolingapplicationfrontend.data.models.LoginResponse
import com.example.carpoolingapplicationfrontend.ui.components.AppPrimaryGreen
import com.example.carpoolingapplicationfrontend.ui.components.EmailIcon
import com.example.carpoolingapplicationfrontend.ui.components.LockIcon
import com.example.carpoolingapplicationfrontend.ui.components.LoginErrorMessage
import com.example.carpoolingapplicationfrontend.ui.components.LoginInputField
import com.example.carpoolingapplicationfrontend.ui.components.LoginLogo
import com.example.carpoolingapplicationfrontend.viewmodel.AuthUiState
import com.example.carpoolingapplicationfrontend.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val loginState by viewModel.loginState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }
    val isLoading = loginState is AuthUiState.Loading

    LaunchedEffect(loginState) {
        if (loginState is AuthUiState.Success<LoginResponse>) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.72f))

            Column(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginHeader()

                Spacer(modifier = Modifier.height(24.dp))

                LoginForm(
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
                    isLoading = isLoading,
                    errorMessage = localError ?: (loginState as? AuthUiState.Error)?.message,
                    onForgotPasswordClick = onForgotPasswordClick,
                    onLoginClick = {
                        if (isValidMonashEmail(email)) {
                            localError = null
                            viewModel.login(LoginRequest(email.trim(), password))
                        } else {
                            localError = "Please use a valid Monash email address"
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                RegisterPrompt(onRegisterClick = onRegisterClick)
            }

            Spacer(modifier = Modifier.weight(1.28f))
        }
    }
}

@Composable
private fun LoginHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginLogo()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Welcome Back",
            color = Color(0xFF111827),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Sign in to continue",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun LoginForm(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onForgotPasswordClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            LoginInputField(
                label = "Monash Email",
                value = email,
                onValueChange = onEmailChange,
                placeholder = "your.name@monash.edu",
                leadingIcon = EmailIcon,
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            LoginInputField(
                label = "Password",
                value = password,
                onValueChange = onPasswordChange,
                placeholder = "Enter your password",
                leadingIcon = LockIcon,
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )
        }

        if (errorMessage != null) {
            LoginErrorMessage(message = errorMessage)
        }

        Text(
            text = "Forgot password?",
            color = AppPrimaryGreen,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.End)
                .clickable(enabled = !isLoading, onClick = onForgotPasswordClick)
        )

        Button(
            onClick = onLoginClick,
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
                    text = "Log In",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun RegisterPrompt(
    onRegisterClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = buildAnnotatedString {
                append("Don't have an account? ")
                withStyle(
                    SpanStyle(
                        color = AppPrimaryGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append("Register")
                }
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.clickable(onClick = onRegisterClick)
        )
    }
}

private fun isValidMonashEmail(email: String): Boolean {
    val normalizedEmail = email.trim().lowercase()
    return normalizedEmail.endsWith("@monash.edu") ||
        normalizedEmail.endsWith("@student.monash.edu")
}
