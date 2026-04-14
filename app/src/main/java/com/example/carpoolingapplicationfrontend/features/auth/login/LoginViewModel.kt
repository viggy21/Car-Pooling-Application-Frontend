package com.example.carpoolingapplicationfrontend.features.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel() : ViewModel() {
    private val _email = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()
    val email : LiveData<String> = _email
    val password : LiveData<String> = _password

    fun onEmailUpdate(newEmail : String) {
        _email.value = newEmail
    }

    fun onPasswordUpdate(newPassword : String) {
        _password.value = newPassword
    }
}