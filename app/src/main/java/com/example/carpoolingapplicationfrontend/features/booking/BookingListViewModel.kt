package com.example.carpoolingapplicationfrontend.features.booking


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.carpoolingapplicationfrontend.features.auth.login.LoginRepository

sealed class BookingUiState {
    object Loading : BookingUiState()
    data class Success(val bookings: List<BookingDto>) : BookingUiState()
    data class Error(val message: String) : BookingUiState()
}

class BookingListViewModel(
    private val repository: BookingRepository = BookingRepository()
) : ViewModel() {

    var uiState by mutableStateOf<BookingUiState>(BookingUiState.Loading)
        private set

    private val useFakeData = true // TODO: set to false when actually using backend API request
    fun fetchBookings(userId: Long) {
        uiState = BookingUiState.Loading
        if (useFakeData) {
            uiState = BookingUiState.Success(FakeBookingData.bookings)
            return
        }

        viewModelScope.launch {
            uiState = BookingUiState.Loading

            val result = repository.getBookings(
                userId = userId,
                page = 1,
                pageSize = 10
            )

            uiState = result.fold(
                onSuccess = { BookingUiState.Success(it) },
                onFailure = { BookingUiState.Error(it.message ?: "Unknown error") }
            )
        }


    }
}