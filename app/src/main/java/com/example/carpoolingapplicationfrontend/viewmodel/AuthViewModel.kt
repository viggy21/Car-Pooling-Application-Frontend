package com.example.carpoolingapplicationfrontend.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.carpoolingapplicationfrontend.data.datastore.AuthPreferences
import com.example.carpoolingapplicationfrontend.data.models.AuthState
import com.example.carpoolingapplicationfrontend.data.models.CheckMailCodeRequest
import com.example.carpoolingapplicationfrontend.data.models.CheckMailCodeResponse
import com.example.carpoolingapplicationfrontend.data.models.RegisterRequest
import com.example.carpoolingapplicationfrontend.data.models.RegisterResponse
import com.example.carpoolingapplicationfrontend.data.models.ResetPasswordRequest
import com.example.carpoolingapplicationfrontend.data.models.ResetPasswordResponse
import com.example.carpoolingapplicationfrontend.data.models.SendMailCodeRequest
import com.example.carpoolingapplicationfrontend.data.models.SendMailCodeResponse
import com.example.carpoolingapplicationfrontend.data.remote.ApiProvider
import com.example.carpoolingapplicationfrontend.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

sealed class AuthUiState<out T> {
    object Idle : AuthUiState<Nothing>()
    object Loading : AuthUiState<Nothing>()
    data class Success<T>(val response: T) : AuthUiState<T>()
    data class Error(val message: String) : AuthUiState<Nothing>()
}

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val authPreferences = AuthPreferences(application)
    private val repository = AuthRepository(
        authApiService = ApiProvider(authPreferences).authApiService
    )

    private val _authState = MutableStateFlow(AuthState(isLoading = true))
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _registerState = MutableStateFlow<AuthUiState<RegisterResponse>>(
        AuthUiState.Idle
    )
    val registerState: StateFlow<AuthUiState<RegisterResponse>> = _registerState.asStateFlow()

    private val _resetPasswordState = MutableStateFlow<AuthUiState<ResetPasswordResponse>>(
        AuthUiState.Idle
    )
    val resetPasswordState: StateFlow<AuthUiState<ResetPasswordResponse>> =
        _resetPasswordState.asStateFlow()

    private val _sendMailCodeState = MutableStateFlow<AuthUiState<SendMailCodeResponse>>(
        AuthUiState.Idle
    )
    val sendMailCodeState: StateFlow<AuthUiState<SendMailCodeResponse>> =
        _sendMailCodeState.asStateFlow()

    private val _checkMailCodeState = MutableStateFlow<AuthUiState<CheckMailCodeResponse>>(
        AuthUiState.Idle
    )
    val checkMailCodeState: StateFlow<AuthUiState<CheckMailCodeResponse>> =
        _checkMailCodeState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                authPreferences.userIdFlow,
                authPreferences.tokenFlow
            ) { userId, token ->
                AuthState(
                    isLoggedIn = userId != null && !token.isNullOrBlank(),
                    userId = userId,
                    token = token,
                    isLoading = false
                )
            }.collect { restoredState ->
                _authState.value = restoredState
            }
        }
    }

    fun login(email: String, password: String) {
        _authState.value = _authState.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            val result = repository.login(
                email = email.trim(),
                password = password
            )

            result.fold(
                onSuccess = { authUser ->
                    authPreferences.saveAuth(
                        userId = authUser.userId,
                        token = authUser.token
                    )
                    _authState.value = AuthState(
                        isLoggedIn = true,
                        userId = authUser.userId,
                        token = authUser.token,
                        isLoading = false
                    )
                },
                onFailure = {
                    _authState.value = AuthState(
                        isLoggedIn = false,
                        isLoading = false,
                        error = it.message ?: "Failed to log in"
                    )
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authPreferences.clearAuth()
            _authState.value = AuthState()
        }
    }

    fun getCurrentUserId(): Long? {
        return _authState.value.userId
    }

    fun getToken(): String? {
        return _authState.value.token
    }

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

    fun resetPassword(request: ResetPasswordRequest) {
        launchAuthRequest(_resetPasswordState, "Failed to reset password") {
            repository.resetPassword(request)
        }
    }

    fun sendMailCode(request: SendMailCodeRequest) {
        launchAuthRequest(_sendMailCodeState, "Failed to send mail code") {
            repository.sendMailCode(request)
        }
    }

    fun checkMailCode(request: CheckMailCodeRequest) {
        launchAuthRequest(_checkMailCodeState, "Failed to check mail code") {
            repository.checkMailCode(request)
        }
    }

    fun resetRegisterState() {
        _registerState.value = AuthUiState.Idle
    }

    fun resetResetPasswordState() {
        _resetPasswordState.value = AuthUiState.Idle
    }

    fun resetSendMailCodeState() {
        _sendMailCodeState.value = AuthUiState.Idle
    }

    fun resetCheckMailCodeState() {
        _checkMailCodeState.value = AuthUiState.Idle
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
