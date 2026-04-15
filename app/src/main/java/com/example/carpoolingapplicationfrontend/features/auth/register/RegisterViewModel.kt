package com.example.carpoolingapplicationfrontend.features.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginRepository
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginResponse
import kotlinx.coroutines.launch

class RegisterViewModel (
    private val repository: RegisterRepository = RegisterRepository()
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading : LiveData<Boolean> = _isLoading
    private val _name = MutableLiveData<String>()
    val name : LiveData<String> = _name
    private val _phone = MutableLiveData<String>()
    val phone : LiveData<String> = _phone
    private val _sex = MutableLiveData<String>()
    val sex : LiveData<String> = _sex
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


//        viewModelScope.launch {
//            Log.d("LOGIN_DEBUG", "Coroutine started")
//            try {
//                val response = repository.login(emailValue, passwordValue)
//
//                Log.d("LOGIN_DEBUG", "Response received: ${response.code()}")
//
//                if (response.isSuccessful) {
//                    Log.d("LOGIN_DEBUG", "Success: ${response.body()}")
//                    _loginResult.value = response.body()
//                }
//                else {
//                    // handle error e.g. wrong credentials
//                    Log.d("LOGIN_DEBUG", "Error: ${response.errorBody()}")
//                    _loginResult.value = null
//                }
//            } catch (e: Exception) {
//                // network error
//                Log.d("LOGIN_DEBUG", "Exception: ${e.message}")
//                _loginResult.value = null
//            }
//            _isLoading.postValue(false)
//        }
    }

}