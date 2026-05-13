package com.example.carpoolingapplicationfrontend.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carpoolingapplicationfrontend.data.models.CreateBookingRequest
import com.example.carpoolingapplicationfrontend.viewmodel.BookingViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


private const val GOOGLE_PLACES_API_KEY = "INSERT_KEY"

data class PlacePredictionUiModel(
    val placeId: String,
    val primaryText: String,
    val secondaryText: String?
)

data class SelectedPlaceUiModel(
    val name: String,
    val latitude: Double,
    val longitude: Double
)

data class RideFormState(
    val from: String,
    val fromLat: Double,
    val fromLng: Double,
    val to: String,
    val toLat: Double,
    val toLng: Double,
    val date: String,
    val time: String,
    val seats: String,
    val notes: String
)

@Composable
fun CreateRideScreen(
    viewModel: BookingViewModel,
    userId: Long?,
    onBackClick: () -> Unit,
    onRideCreated: () -> Unit
) {
    var selectedRideType by remember {
        mutableStateOf<RideType?>(null)
    }

    if (selectedRideType == null) {
        CreateRideSelectionContent(
            onBackClick = onBackClick,
            onOfferRideClick = {
                selectedRideType = RideType.OFFER
            },
            onRequestRideClick = {
                selectedRideType = RideType.REQUEST
            }
        )
    } else {
        CreateRideFormContent(
            rideType = selectedRideType!!,
            onBackClick = {
                selectedRideType = null
            },
            onSubmit = {
                val authenticatedUserId = userId ?: return@CreateRideFormContent
                val request = CreateBookingRequest(
                    userId = authenticatedUserId,
                    role = if (selectedRideType == RideType.OFFER) 0 else 1,
                    originName = it.from,
                    originLat = it.fromLat,
                    originLng = it.fromLng,
                    destName = it.to,
                    destLat = it.toLat,
                    destLng = it.toLng,
                    freeSeat = if (selectedRideType == RideType.OFFER) {
                        it.seats.toIntOrNull() ?: 1
                    } else {
                        0
                    },
                    expectedStartTime = "${it.date}T${it.time}",
                    remark = it.notes
                )
                viewModel.createBooking(request)
                onRideCreated()
            }
        )
    }
}

@Composable
fun CreateRideSelectionContent(
    onBackClick: () -> Unit,
    onOfferRideClick: () -> Unit,
    onRequestRideClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
    ) {
        HeaderSection(
            title = "Create Ride",
            subtitle = "Choose your ride type",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .offset(y = (-34).dp)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RideTypeCard(
                title = "Offer a Ride",
                description = "Share your car and help others travel green",
                icon = {
                    Icon(
                        Icons.Default.CarRental,
                        contentDescription = null,
                        tint = Color(0xFF16A34A)
                    )
                },
                iconBackground = Color(0xFFDCFCE7),
                onClick = onOfferRideClick
            )

            RideTypeCard(
                title = "Request a Ride",
                description = "Find someone heading your way",
                icon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF2563EB)
                    )
                },
                iconBackground = Color(0xFFDBEAFE),
                onClick = onRequestRideClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRideFormContent(
    rideType: RideType,
    onBackClick: () -> Unit,
    onSubmit: (RideFormState) -> Unit
) {
    var from by remember { mutableStateOf("") }
    var fromLat by remember { mutableStateOf(0.0) }
    var fromLng by remember { mutableStateOf(0.0) }
    var to by remember { mutableStateOf("") }
    var toLat by remember { mutableStateOf(0.0) }
    var toLng by remember { mutableStateOf(0.0) }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var seats by remember { mutableStateOf("3") }
    var notes by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val seatOptions = listOf("1", "2", "3", "4", "5")
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            date = String.format(
                Locale.US,
                "%04d-%02d-%02d",
                year,
                month + 1,
                dayOfMonth
            )
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            time = String.format(
                Locale.US,
                "%02d:%02d:00",
                hourOfDay,
                minute
            )
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
            .verticalScroll(rememberScrollState())
    ) {
        HeaderSection(
            title = if (rideType == RideType.OFFER) {
                "Offer a Ride"
            } else {
                "Request a Ride"
            },
            subtitle = "Fill in the details below",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .offset(y = (-34).dp)
                .padding(horizontal = 24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp)
                ) {
                    LocationAutocompleteField(
                        value = from,
                        onValueChange = {
                            from = it
                            fromLat = 0.0
                            fromLng = 0.0
                        },
                        onPlaceSelected = {
                            from = it.name
                            fromLat = it.latitude
                            fromLng = it.longitude
                        },
                        label = "Departure Point",
                        placeholder = "e.g., Clayton Campus - Building 11",
                        iconTint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LocationAutocompleteField(
                        value = to,
                        onValueChange = {
                            to = it
                            toLat = 0.0
                            toLng = 0.0
                        },
                        onPlaceSelected = {
                            to = it.name
                            toLat = it.latitude
                            toLng = it.longitude
                        },
                        label = "Destination",
                        placeholder = "e.g., Caulfield Campus - Library",
                        iconTint = Color(0xFF16A34A)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Date",
                                style = MaterialTheme.typography.labelLarge
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        datePickerDialog.show()
                                    }
                            ) {
                                OutlinedTextField(
                                    value = date,
                                    onValueChange = {},
                                    enabled = false,
                                    placeholder = {
                                        Text("yyyy-mm-dd")
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.CalendarToday, null)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Time",
                                style = MaterialTheme.typography.labelLarge
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        timePickerDialog.show()
                                    }
                            ) {
                                OutlinedTextField(
                                    value = time,
                                    onValueChange = {},
                                    enabled = false,
                                    placeholder = {
                                        Text("--:--")
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.AccessTime, null)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }
                    }

                    if (rideType == RideType.OFFER) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Available Seats",
                            style = MaterialTheme.typography.labelLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = {
                                expanded = !expanded
                            }
                        ) {
                            OutlinedTextField(
                                value = "$seats seats",
                                onValueChange = {},
                                readOnly = true,
                                leadingIcon = {
                                    Icon(Icons.Default.People, null)
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                                },
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = {
                                    expanded = false
                                }
                            ) {
                                seatOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = {
                                            Text("$option seats")
                                        },
                                        onClick = {
                                            seats = option
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    RideTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = "Additional Notes (Optional)",
                        placeholder = "Any special instructions or preferences...",
                        singleLine = false,
                        minLines = 4,
                        icon = {
                            Icon(Icons.Default.Description, null)
                        },
                        alignIconToTop = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            onSubmit(
                                RideFormState(
                                    from = from,
                                    fromLat = fromLat,
                                    fromLng = fromLng,
                                    to = to,
                                    toLat = toLat,
                                    toLng = toLng,
                                    date = date,
                                    time = time,
                                    seats = seats,
                                    notes = notes
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF16A34A)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (rideType == RideType.OFFER) {
                                "Create Offer"
                            } else {
                                "Create Request"
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationAutocompleteField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onPlaceSelected: (SelectedPlaceUiModel) -> Unit,
    label: String,
    placeholder: String,
    iconTint: Color
) {
    val context = LocalContext.current
    val placesClient = rememberPlacesClient(context)
    val sessionToken = remember { AutocompleteSessionToken.newInstance() }
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var suggestions by remember { mutableStateOf<List<PlacePredictionUiModel>>(emptyList()) }
    var searchJob by remember { mutableStateOf<Job?>(null) }

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded && suggestions.isNotEmpty(),
            onExpandedChange = {
                expanded = suggestions.isNotEmpty() && !expanded
            }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = { query ->
                    onValueChange(query)
                    searchJob?.cancel()

                    if (query.length < 3) {
                        suggestions = emptyList()
                        expanded = false
                    } else {
                        searchJob = scope.launch {
                            delay(250)
                            suggestions = placesClient
                                ?.findPlacePredictions(query, sessionToken)
                                .orEmpty()
                            expanded = suggestions.isNotEmpty()
                        }
                    }
                },
                placeholder = {
                    Text(placeholder)
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = iconTint
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryEditable)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded && suggestions.isNotEmpty(),
                onDismissRequest = {
                    expanded = false
                }
            ) {
                suggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = suggestion.primaryText,
                                    color = Color(0xFF111827),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (!suggestion.secondaryText.isNullOrBlank()) {
                                    Text(
                                        text = suggestion.secondaryText,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        },
                        onClick = {
                            expanded = false
                            suggestions = emptyList()
                            scope.launch {
                                val selectedPlace = placesClient?.fetchSelectedPlace(suggestion.placeId)
                                if (selectedPlace != null) {
                                    onPlaceSelected(selectedPlace)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberPlacesClient(context: Context): PlacesClient? {
    return remember(context) {
        runCatching {
            if (!Places.isInitialized()) {
                Places.initialize(context.applicationContext, GOOGLE_PLACES_API_KEY)
            }
            Places.createClient(context.applicationContext)
        }.getOrNull()
    }
}

private suspend fun PlacesClient.findPlacePredictions(
    query: String,
    sessionToken: AutocompleteSessionToken
): List<PlacePredictionUiModel> {
    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .setSessionToken(sessionToken)
        .build()

    return suspendCancellableCoroutine { continuation ->
        findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                if (continuation.isActive) {
                    continuation.resume(
                        response.autocompletePredictions.map { it.toUiModel() }
                    )
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PlacesAPI", "Autocomplete failed", exception)

                if (continuation.isActive) {
                    continuation.resume(emptyList())
                }
            }
    }
}

private suspend fun PlacesClient.fetchSelectedPlace(placeId: String): SelectedPlaceUiModel? {
    val request = FetchPlaceRequest.builder(
        placeId,
        listOf(
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )
    ).build()

    return suspendCancellableCoroutine { continuation ->
        fetchPlace(request)
            .addOnSuccessListener { response ->
                val place = response.place
                val latLng = place.latLng
                val selectedPlace = if (latLng == null) {
                    null
                } else {
                    SelectedPlaceUiModel(
                        name = place.address ?: place.name.orEmpty(),
                        latitude = latLng.latitude,
                        longitude = latLng.longitude
                    )
                }

                if (continuation.isActive) {
                    continuation.resume(selectedPlace)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PlacesAPI", "Fetch place failed", exception)

                if (continuation.isActive) {
                    continuation.resume(null)
                }
            }
    }
}

private fun AutocompletePrediction.toUiModel(): PlacePredictionUiModel {
    return PlacePredictionUiModel(
        placeId = placeId,
        primaryText = getPrimaryText(null).toString(),
        secondaryText = getSecondaryText(null).toString().ifBlank { null }
    )
}

@Composable
fun HeaderSection(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF4CCB62),
                        Color(0xFF2F9E61)
                    )
                )
            )
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 16.dp,
                bottom = 56.dp
            )
    ) {
        TextButton(onClick = onBackClick) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Back",
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = subtitle,
            color = Color.White.copy(alpha = 0.9f),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun RideTypeCard(
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    iconBackground: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Row(
            modifier = Modifier.padding(22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        iconBackground,
                        CircleShape
                    )
                    .padding(18.dp),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    color = Color(0xFF111827),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun RideTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: @Composable () -> Unit,
    singleLine: Boolean = true,
    minLines: Int = 1,
    alignIconToTop: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (alignIconToTop) {
            Box {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(placeholder)
                    },
                    leadingIcon = {
                        Spacer(modifier = Modifier.width(24.dp))
                    },
                    singleLine = singleLine,
                    minLines = minLines,
                    shape = RoundedCornerShape(12.dp)
                )

                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp)
                        .size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }
        } else {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(placeholder)
                },
                leadingIcon = icon,
                singleLine = singleLine,
                minLines = minLines,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}
