package com.example.carpoolingapplicationfrontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carpoolingapplicationfrontend.data.models.LoginRequest
import com.example.carpoolingapplicationfrontend.data.models.LoginResponse
import com.example.carpoolingapplicationfrontend.data.models.RegisterRequest
import com.example.carpoolingapplicationfrontend.data.models.RegisterResponse
import com.example.carpoolingapplicationfrontend.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState<out T> {
    object Idle : AuthUiState<Nothing>()
    object Loading : AuthUiState<Nothing>()
    data class Success<T>(val response: T) : AuthUiState<T>()
    data class Error(val message: String) : AuthUiState<Nothing>()
}

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _registerState = MutableStateFlow<AuthUiState<RegisterResponse>>(
        AuthUiState.Idle
    )
    val registerState: StateFlow<AuthUiState<RegisterResponse>> = _registerState.asStateFlow()

    private val _loginState = MutableStateFlow<AuthUiState<LoginResponse>>(
        AuthUiState.Idle
    )
    val loginState: StateFlow<AuthUiState<LoginResponse>> = _loginState.asStateFlow()

    private val _authState = MutableStateFlow<AuthUiState<String>>(
        AuthUiState.Idle
    )
    val authState: StateFlow<AuthUiState<String>> = _authState.asStateFlow()

    fun register(request: RegisterRequest) {
        launchAuthRequest(_registerState, "Failed to register") {
            repository.register(request)
        }
    }

    fun validateRegisterInput(
        email: String,
        password: String,
        confirmPassword: String
    ): String? {
        val normalizedEmail = email.trim().lowercase()

        return when {
            !normalizedEmail.endsWith("@monash.edu") &&
                !normalizedEmail.endsWith("@student.monash.edu") -> {
                "Please use a valid Monash email address"
            }
            password != confirmPassword -> {
                "Passwords do not match"
            }
            else -> null
        }
    }

    fun login(request: LoginRequest) {
        launchAuthRequest(_loginState, "Failed to log in") {
            repository.login(request)
        }
    }

    fun getAuth() {
        launchAuthRequest(_authState, "Failed to load auth") {
            repository.getAuth()
        }
    }

    fun resetRegisterState() {
        _registerState.value = AuthUiState.Idle
    }

    fun resetLoginState() {
        _loginState.value = AuthUiState.Idle
    }

    fun resetAuthState() {
        _authState.value = AuthUiState.Idle
    }

    private fun <T> launchAuthRequest(
        state: MutableStateFlow<AuthUiState<T>>,
        fallbackErrorMessage: String,
        request: suspend () -> Result<T>
    ) {
        state.value = AuthUiState.Loading

        viewModelScope.launch {
            val result = request()

            state.value = result.fold(
                onSuccess = { AuthUiState.Success(it) },
                onFailure = {
                    AuthUiState.Error(it.message ?: fallbackErrorMessage)
                }
            )
        }
    }
}
