package com.example.carpoolingapplicationfrontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carpoolingapplicationfrontend.data.models.AcceptMatchResponse
import com.example.carpoolingapplicationfrontend.data.models.BookingDetailResponse
import com.example.carpoolingapplicationfrontend.data.models.BookingLocationResponse
import com.example.carpoolingapplicationfrontend.data.models.BookingSearchRequest
import com.example.carpoolingapplicationfrontend.data.models.CreateBookingRequest
import com.example.carpoolingapplicationfrontend.data.models.CreateBookingResponse
import com.example.carpoolingapplicationfrontend.data.models.EndTripResponse
import com.example.carpoolingapplicationfrontend.data.models.MatchedBookingsResponse
import com.example.carpoolingapplicationfrontend.data.models.PageQueryRequest
import com.example.carpoolingapplicationfrontend.data.models.PrematchResponse
import com.example.carpoolingapplicationfrontend.data.models.RejectMatchResponse
import com.example.carpoolingapplicationfrontend.data.models.SaveLocationRequest
import com.example.carpoolingapplicationfrontend.data.models.SaveLocationResponse
import com.example.carpoolingapplicationfrontend.data.models.SearchBookingsResponse
import com.example.carpoolingapplicationfrontend.data.models.StartTripResponse
import com.example.carpoolingapplicationfrontend.data.models.UpdateBookingRequest
import com.example.carpoolingapplicationfrontend.data.models.UpdateBookingResponse
import com.example.carpoolingapplicationfrontend.data.models.UserBookingsResponse
import com.example.carpoolingapplicationfrontend.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CreateBookingUiState {
    object Idle : CreateBookingUiState()
    object Loading : CreateBookingUiState()
    data class Success(val response: CreateBookingResponse) : CreateBookingUiState()
    data class Error(val message: String) : CreateBookingUiState()
}

sealed class UpdateBookingUiState {
    object Idle : UpdateBookingUiState()
    object Loading : UpdateBookingUiState()
    data class Success(val response: UpdateBookingResponse) : UpdateBookingUiState()
    data class Error(val message: String) : UpdateBookingUiState()
}

sealed class StartTripUiState {
    object Idle : StartTripUiState()
    object Loading : StartTripUiState()
    data class Success(val response: StartTripResponse) : StartTripUiState()
    data class Error(val message: String) : StartTripUiState()
}

sealed class BookingRequestUiState<out T> {
    object Idle : BookingRequestUiState<Nothing>()
    object Loading : BookingRequestUiState<Nothing>()
    data class Success<T>(val response: T) : BookingRequestUiState<T>()
    data class Error(val message: String) : BookingRequestUiState<Nothing>()
}

class BookingViewModel(
    private val repository: BookingRepository = BookingRepository()
) : ViewModel() {

    private val _createBookingState = MutableStateFlow<CreateBookingUiState>(
        CreateBookingUiState.Idle
    )
    val createBookingState: StateFlow<CreateBookingUiState> = _createBookingState.asStateFlow()

    private val _updateBookingState = MutableStateFlow<UpdateBookingUiState>(
        UpdateBookingUiState.Idle
    )
    val updateBookingState: StateFlow<UpdateBookingUiState> = _updateBookingState.asStateFlow()

    private val _startTripState = MutableStateFlow<StartTripUiState>(
        StartTripUiState.Idle
    )
    val startTripState: StateFlow<StartTripUiState> = _startTripState.asStateFlow()

    private val _endTripState = MutableStateFlow<BookingRequestUiState<EndTripResponse>>(
        BookingRequestUiState.Idle
    )
    val endTripState: StateFlow<BookingRequestUiState<EndTripResponse>> =
        _endTripState.asStateFlow()

    private val _saveLocationState = MutableStateFlow<BookingRequestUiState<SaveLocationResponse>>(
        BookingRequestUiState.Idle
    )
    val saveLocationState: StateFlow<BookingRequestUiState<SaveLocationResponse>> =
        _saveLocationState.asStateFlow()

    private val _rejectMatchState = MutableStateFlow<BookingRequestUiState<RejectMatchResponse>>(
        BookingRequestUiState.Idle
    )
    val rejectMatchState: StateFlow<BookingRequestUiState<RejectMatchResponse>> =
        _rejectMatchState.asStateFlow()

    private val _acceptMatchState = MutableStateFlow<BookingRequestUiState<AcceptMatchResponse>>(
        BookingRequestUiState.Idle
    )
    val acceptMatchState: StateFlow<BookingRequestUiState<AcceptMatchResponse>> =
        _acceptMatchState.asStateFlow()

    private val _searchBookingsState =
        MutableStateFlow<BookingRequestUiState<SearchBookingsResponse>>(
            BookingRequestUiState.Idle
        )
    val searchBookingsState: StateFlow<BookingRequestUiState<SearchBookingsResponse>> =
        _searchBookingsState.asStateFlow()

    private val _userBookingsState = MutableStateFlow<BookingRequestUiState<UserBookingsResponse>>(
        BookingRequestUiState.Idle
    )
    val userBookingsState: StateFlow<BookingRequestUiState<UserBookingsResponse>> =
        _userBookingsState.asStateFlow()

    private val _bookingDetailState =
        MutableStateFlow<BookingRequestUiState<BookingDetailResponse>>(
            BookingRequestUiState.Idle
        )
    val bookingDetailState: StateFlow<BookingRequestUiState<BookingDetailResponse>> =
        _bookingDetailState.asStateFlow()

    private val _prematchState = MutableStateFlow<BookingRequestUiState<PrematchResponse>>(
        BookingRequestUiState.Idle
    )
    val prematchState: StateFlow<BookingRequestUiState<PrematchResponse>> =
        _prematchState.asStateFlow()

    private val _matchedBookingsState =
        MutableStateFlow<BookingRequestUiState<MatchedBookingsResponse>>(
            BookingRequestUiState.Idle
        )
    val matchedBookingsState: StateFlow<BookingRequestUiState<MatchedBookingsResponse>> =
        _matchedBookingsState.asStateFlow()

    private val _bookingLocationState =
        MutableStateFlow<BookingRequestUiState<BookingLocationResponse>>(
            BookingRequestUiState.Idle
        )
    val bookingLocationState: StateFlow<BookingRequestUiState<BookingLocationResponse>> =
        _bookingLocationState.asStateFlow()

    fun createBooking(request: CreateBookingRequest) {
        _createBookingState.value = CreateBookingUiState.Loading

        viewModelScope.launch {
            val result = repository.createBooking(request)

            _createBookingState.value = result.fold(
                onSuccess = { CreateBookingUiState.Success(it) },
                onFailure = {
                    CreateBookingUiState.Error(it.message ?: "Failed to create booking")
                }
            )
        }
    }

    fun updateBooking(request: UpdateBookingRequest) {
        _updateBookingState.value = UpdateBookingUiState.Loading

        viewModelScope.launch {
            val result = repository.updateBooking(request)

            _updateBookingState.value = result.fold(
                onSuccess = { UpdateBookingUiState.Success(it) },
                onFailure = {
                    UpdateBookingUiState.Error(it.message ?: "Failed to update booking")
                }
            )
        }
    }

    fun startTrip(bookingId: String) {
        _startTripState.value = StartTripUiState.Loading

        viewModelScope.launch {
            val result = repository.startTrip(bookingId)

            _startTripState.value = result.fold(
                onSuccess = { StartTripUiState.Success(it) },
                onFailure = {
                    StartTripUiState.Error(it.message ?: "Failed to start trip")
                }
            )
        }
    }

    fun endTrip(bookingId: String) {
        launchBookingRequest(_endTripState, "Failed to end trip") {
            repository.endTrip(bookingId)
        }
    }

    fun saveLocation(request: SaveLocationRequest) {
        launchBookingRequest(_saveLocationState, "Failed to save location") {
            repository.saveLocation(request)
        }
    }

    fun rejectMatch(tripId: String, bookingId: String) {
        launchBookingRequest(_rejectMatchState, "Failed to reject match") {
            repository.rejectMatch(tripId, bookingId)
        }
    }

    fun acceptMatch(tripId: String, bookingId: String) {
        launchBookingRequest(_acceptMatchState, "Failed to accept match") {
            repository.acceptMatch(tripId, bookingId)
        }
    }

    fun searchBookings(request: BookingSearchRequest) {
        launchBookingRequest(_searchBookingsState, "Failed to search bookings") {
            repository.searchBookings(request)
        }
    }

    fun getBookingsByUserId(id: Long, request: PageQueryRequest) {
        launchBookingRequest(_userBookingsState, "Failed to load user bookings") {
            repository.getBookingsByUserId(id, request)
        }
    }

    fun getBookingById(id: Long) {
        launchBookingRequest(_bookingDetailState, "Failed to load booking") {
            repository.getBookingById(id)
        }
    }

    fun getPrematchBookings(tripId: String) {
        launchBookingRequest(_prematchState, "Failed to load prematch bookings") {
            repository.getPrematchBookings(tripId)
        }
    }

    fun getMatchedBookings(tripId: String) {
        launchBookingRequest(_matchedBookingsState, "Failed to load matched bookings") {
            repository.getMatchedBookings(tripId)
        }
    }

    fun getLocation(bookingId: String) {
        launchBookingRequest(_bookingLocationState, "Failed to load booking location") {
            repository.getLocation(bookingId)
        }
    }

    fun resetCreateBookingState() {
        _createBookingState.value = CreateBookingUiState.Idle
    }

    fun resetUpdateBookingState() {
        _updateBookingState.value = UpdateBookingUiState.Idle
    }

    fun resetStartTripState() {
        _startTripState.value = StartTripUiState.Idle
    }

    fun resetEndTripState() {
        _endTripState.value = BookingRequestUiState.Idle
    }

    fun resetSaveLocationState() {
        _saveLocationState.value = BookingRequestUiState.Idle
    }

    fun resetRejectMatchState() {
        _rejectMatchState.value = BookingRequestUiState.Idle
    }

    fun resetAcceptMatchState() {
        _acceptMatchState.value = BookingRequestUiState.Idle
    }

    fun resetSearchBookingsState() {
        _searchBookingsState.value = BookingRequestUiState.Idle
    }

    fun resetUserBookingsState() {
        _userBookingsState.value = BookingRequestUiState.Idle
    }

    fun resetBookingDetailState() {
        _bookingDetailState.value = BookingRequestUiState.Idle
    }

    fun resetPrematchState() {
        _prematchState.value = BookingRequestUiState.Idle
    }

    fun resetMatchedBookingsState() {
        _matchedBookingsState.value = BookingRequestUiState.Idle
    }

    fun resetBookingLocationState() {
        _bookingLocationState.value = BookingRequestUiState.Idle
    }

    private fun <T> launchBookingRequest(
        state: MutableStateFlow<BookingRequestUiState<T>>,
        fallbackErrorMessage: String,
        request: suspend () -> Result<T>
    ) {
        state.value = BookingRequestUiState.Loading

        viewModelScope.launch {
            val result = request()

            state.value = result.fold(
                onSuccess = { BookingRequestUiState.Success(it) },
                onFailure = {
                    BookingRequestUiState.Error(it.message ?: fallbackErrorMessage)
                }
            )
        }
    }
}
