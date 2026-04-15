package com.example.carpoolingapplicationfrontend.features.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository = LoginRepository()
) : ViewModel() {
//    val userRepository : UserRepository = UserRepository()
//    private val _userData = MutableLiveData<UserData>()
//    val userData : LiveData<UserData> = _userData

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading : LiveData<Boolean> = _isLoading
    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email
    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> = _loginResult

    fun onEmailUpdate(newEmail : String) {
        _email.value = newEmail
    }

    fun onPasswordUpdate(newPassword : String) {
        _password.value = newPassword
    }

    fun login() {
        val emailValue = _email.value ?: ""
        val passwordValue = _password.value ?: ""

        // TODO: fix the 'is loading' below because it isn't showing the loading properly
        _isLoading.postValue(true)

        Log.d("LOGIN_DEBUG", "Login called")


        viewModelScope.launch {
            Log.d("LOGIN_DEBUG", "Coroutine started")
            try {
                val response = repository.login(emailValue, passwordValue)

                Log.d("LOGIN_DEBUG", "Response received: ${response.code()}")

                if (response.isSuccessful) {
                    Log.d("LOGIN_DEBUG", "Success: ${response.body()}")
                    _loginResult.value = response.body()
                }
                else {
                    // handle error e.g. wrong credentials
                    Log.d("LOGIN_DEBUG", "Error: ${response.errorBody()}")
                    _loginResult.value = null
                }
            } catch (e: Exception) {
                // network error
                Log.d("LOGIN_DEBUG", "Exception: ${e.message}")
                _loginResult.value = null
            }
            _isLoading.postValue(false)
        }
    }

//    fun getUserData() {
//        _isLoading.postValue(true)
//        viewModelScope.launch {
//            val userResult = userRepository.fetchUserData()
//            _userData.postValue(userResult)
//            _isLoading.postValue(false)
//        }
//    }

}