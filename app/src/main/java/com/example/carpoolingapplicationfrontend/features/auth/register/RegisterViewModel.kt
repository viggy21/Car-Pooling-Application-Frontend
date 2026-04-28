package com.example.carpoolingapplicationfrontend.features.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carpoolingapplicationfrontend.features.Gender
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
    private val _gender = MutableLiveData(Gender.MALE)
    val gender: LiveData<Gender> = _gender
    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email
    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _registerResult = MutableLiveData<RegisterResponse?>()
    val registerResult: LiveData<RegisterResponse?> = _registerResult

    fun onNameUpdate(newName : String) {
        _name.value = newName
    }
    fun onPhoneUpdate(newPhone : String) {
        _phone.value = newPhone
    }
//    fun onSexUpdate(newSex : String) {
//        _sex.value = newSex
//    }
    fun onGenderUpdate(gender: Gender) {
        _gender.value = gender
    }
    fun onEmailUpdate(newEmail : String) {
        _email.value = newEmail
    }

    fun onPasswordUpdate(newPassword : String) {
        _password.value = newPassword
    }

    fun register() {
        val nameValue = _name.value ?: ""
        val phoneValue = _phone.value ?: ""
        //val sexValue = _sex.value ?: ""
        val genderValue = gender.value?.value ?: 0
        val emailValue = _email.value ?: ""
        val passwordValue = _password.value ?: ""

        // TODO: fix the 'is loading' below because it isn't showing the loading properly
        _isLoading.postValue(true)

        Log.d("REGISTER_DEBUG", "Register called")


        viewModelScope.launch {
            Log.d("REGISTER_DEBUG", "Coroutine started")
            try {
                val response = repository.register(nameValue, phoneValue, genderValue.toString(), emailValue, passwordValue)

                Log.d("REGISTER_DEBUG", "Response received: ${response.code()}")

                if (response.isSuccessful) {
                    Log.d("REGISTER_DEBUG", "Success: ${response.body()}")
                    _registerResult.value = response.body()
                }
                else {
                    // handle error e.g. wrong credentials
                    Log.d("REGISTER_DEBUG", "Error: ${response.errorBody()}")
                    _registerResult.value = null
                }
            } catch (e: Exception) {
                // network error
                Log.d("REGISTER_DEBUG", "Exception: ${e.message}")
                _registerResult.value = null
            }
            _isLoading.postValue(false)
        }
    }
}