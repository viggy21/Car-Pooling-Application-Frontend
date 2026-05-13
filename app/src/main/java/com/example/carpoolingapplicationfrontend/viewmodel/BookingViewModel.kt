package com.example.carpoolingapplicationfrontend.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.i18n.DateTimeFormatter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.carpoolingapplicationfrontend.data.datastore.AuthPreferences
import com.example.carpoolingapplicationfrontend.data.models.AcceptMatchResponse
import com.example.carpoolingapplicationfrontend.data.models.BookingDetailResponse
import com.example.carpoolingapplicationfrontend.data.models.BookingDto
import com.example.carpoolingapplicationfrontend.data.models.BookingLocationResponse
import com.example.carpoolingapplicationfrontend.data.models.BookingSearchRequest
import com.example.carpoolingapplicationfrontend.data.models.BookingStatus
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
import com.example.carpoolingapplicationfrontend.data.remote.ApiProvider
import com.example.carpoolingapplicationfrontend.data.repository.BookingRepository
import com.example.carpoolingapplicationfrontend.ui.screens.PassengerUiModel
import com.example.carpoolingapplicationfrontend.ui.screens.VehicleUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale
import com.example.carpoolingapplicationfrontend.ui.screens.RideTab

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

data class RideUiModel(
    val id: String,
    val fromLocation: String,
    val toLocation: String,
    val date: String,
    val time: String,
    val seats: String?,
    val ride_type: String,
    val ride_status: Int,
    val remark: String?
)

data class RideDetailsUiModel(
    val id: String,
    val type: String,
    val from: String,
    val to: String,
    val date: String,
    val time: String,

    val seats: Int,
    val passengers: List<PassengerUiModel>,

    val remark: String?,

    val estimatedCost: String,
    val estimatedEnergy: String,
    val estimatedCO2: String,

    val vehicle: VehicleUiModel?
)

data class MatchUiModel(
    val id: String,
    val name: String,
    val route: String
)

data class MatchDetailsUiModel(
    val id: String,
    val riderName: String,
    val fromLocation: String,
    val toLocation: String,
    val date: String,
    val time: String,
    val pickupPoint: String,
    val notes: String?
)

class BookingViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val authPreferences = AuthPreferences(application)
    private val repository = BookingRepository(
        bookingApiService = ApiProvider(authPreferences).bookingApiService
    )

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

    private val _matchDetailsState = MutableStateFlow(
        MatchDetailsUiModel(
            id = "",
            riderName = "Sarah Johnson",
            fromLocation = "Clayton Campus",
            toLocation = "Caulfield Campus",
            date = "2026-05-03",
            time = "09:00 AM",
            pickupPoint = "Main bus stop near Monash Sport",
            notes = "I will be waiting near the bus stop. Happy to leave a few minutes earlier if needed."
        )
    )
    val matchDetailsState: StateFlow<MatchDetailsUiModel> = _matchDetailsState.asStateFlow()

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
        Log.d("BookingViewModel", "Fetching bookings for userId=$id")
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

    fun loadMockMatchDetails(matchId: String) {
        _matchDetailsState.value = MatchDetailsUiModel(
            id = matchId,
            riderName = "Sarah Johnson",
            fromLocation = "Clayton Campus",
            toLocation = "Caulfield Campus",
            date = "2026-05-03",
            time = "09:00 AM",
            pickupPoint = "Main bus stop near Monash Sport",
            notes = "I will be waiting near the bus stop. Happy to leave a few minutes earlier if needed."
        )
    }

//    fun getUpcomingRideUiModel(
//        state: BookingRequestUiState<UserBookingsResponse>
//    ): RideUiModel? {
//        val bookings = (state as? BookingRequestUiState.Success)?.response?.data?.data
//        return bookings?.firstOrNull()?.toRideUiModel()
//            ?: if (state is BookingRequestUiState.Idle) {
//                RideUiModel(
//                    id = "mock-upcoming-ride",
//                    fromLocation = "Clayton Campus",
//                    toLocation = "Caulfield Campus",
//                    date = "2026-05-03",
//                    time = "09:00 AM",
//                    seats = "3 seats",
//                    status = "Offering",
//                    remark = "Good ride"
//                )
//            } else {
//                null
//            }
//    }

//    fun getUpcomingRideUiModels(
//        state: BookingRequestUiState<UserBookingsResponse>
//    ): List<RideUiModel> {
//
//        val bookings =
//            (state as? BookingRequestUiState.Success)
//                ?.response
//                ?.data
//                ?.data
//                ?: emptyList()
//
//        return bookings
//            .map { it.toRideUiModel() }
//            .sortedBy { it.date + it.time }
//    }
fun getUpcomingRideUiModels(
    state: BookingRequestUiState<UserBookingsResponse>
): List<RideUiModel> {

    val bookings =
        (state as? BookingRequestUiState.Success)
            ?.response
            ?.data
            ?.data
            ?: emptyList()

    return bookings
        .filter {
            it.status in listOf(
                BookingStatus.REQUESTED,
                BookingStatus.MATCHED,
                BookingStatus.ONGOING
            )
        }
        .map { it.toRideUiModel() }
        .sortedBy { it.date + it.time }
}

    fun getAllRideUiModels(
        state: BookingRequestUiState<UserBookingsResponse>
    ): List<RideUiModel> {

        val bookings =
            (state as? BookingRequestUiState.Success)
                ?.response
                ?.data
                ?.data
                ?: emptyList()

        return bookings
            .map { it.toRideUiModel() }
            .sortedBy { it.date + it.time }
    }

    fun getMatchUiModels(
        state: BookingRequestUiState<MatchedBookingsResponse>
    ): List<MatchUiModel> {
        val bookings = (state as? BookingRequestUiState.Success)?.response?.data
        return bookings?.map { booking ->
            MatchUiModel(
                id = booking.id.toString(),
                name = "User ${booking.userId}",
                route = "${booking.originName} -> ${booking.destName}"
            )
        } ?: if (state is BookingRequestUiState.Idle) {
            listOf(
                MatchUiModel(
                    id = "mock-match-1",
                    name = "Sarah Johnson",
                    route = "Clayton Campus -> Caulfield Campus"
                ),
                MatchUiModel(
                    id = "mock-match-2",
                    name = "Michael Chen",
                    route = "Clayton Campus -> Caulfield Campus"
                )
            )
        } else {
            emptyList()
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

            Log.d("BookingViewModel", "Request result = $result")

            state.value = result.fold(
                onSuccess = { BookingRequestUiState.Success(it) },
                onFailure = {
                    BookingRequestUiState.Error(it.message ?: fallbackErrorMessage)
                }
            )
        }
    }

//    private fun BookingDto.toRideUiModel(): RideUiModel {
//        return RideUiModel(
//            id = id.toString(),
//            fromLocation = originName,
//            toLocation = destName,
//            date = expectedStartTime.substringBefore("T", expectedStartTime),
//            time = expectedStartTime.substringAfter("T", expectedStartTime)
//                .substringBefore(".")
//                .takeIf { it != expectedStartTime }
//                ?: expectedStartTime,
//            seats = null,
//            status = if (role == 0) "Offering" else "Requesting"
//        )
//    }
private fun BookingDto.toRideUiModel(): RideUiModel {

    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    val dateTime: Date? = try {
        inputFormat.parse(expectedStartTime)
    } catch (e: Exception) {
        null
    }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    val date = dateTime?.let { dateFormat.format(it) } ?: ""
    val time = dateTime?.let { timeFormat.format(it) } ?: ""

    return RideUiModel(
        id = id.toString(),
        fromLocation = originName,
        toLocation = destName,
        date = date,
        time = time,
        seats = null,
        ride_type = if (role == 0) "Offering" else "Requesting",
        ride_status = status,
        remark = remark
    )
}
    fun getRideByIdUiModel(
        state: BookingRequestUiState<BookingDetailResponse>
    ): RideUiModel? {

        val booking = (state as? BookingRequestUiState.Success)?.response?.data

        return booking?.toRideUiModel()
    }



    fun observeBookingDetail(): StateFlow<BookingRequestUiState<BookingDetailResponse>> {
        return bookingDetailState
    }

}

fun BookingDto.toRideDetailsUiModel(): RideDetailsUiModel {

    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val dateTime = runCatching { inputFormat.parse(expectedStartTime) }.getOrNull()

    val date = dateTime?.let {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
    } ?: ""

    val time = dateTime?.let {
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(it)
    } ?: ""

    return RideDetailsUiModel(
        id = id.toString(),
        type = if (role == 0) "offer" else "request",
        from = originName,
        to = destName,
        date = date,
        time = time,

        seats = 1, // TODO: backend doesn't currently provide passenger count per booking DTO, temporary default

        passengers = emptyList(), // TODO: needs separate API later

        remark = remark,

        // TODO: the below are placeholders for now
        estimatedCost = "$0.00",
        estimatedEnergy = "0 kWh",
        estimatedCO2 = "0 kg",

        vehicle = null // not in BookingDto
    )
}
