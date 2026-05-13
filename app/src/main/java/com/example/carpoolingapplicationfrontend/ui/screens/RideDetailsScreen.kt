package com.example.carpoolingapplicationfrontend.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carpoolingapplicationfrontend.data.models.BookingStatus
import com.example.carpoolingapplicationfrontend.ui.components.AppPrimaryGreen
import com.example.carpoolingapplicationfrontend.viewmodel.BookingRequestUiState
import com.example.carpoolingapplicationfrontend.viewmodel.BookingViewModel
import com.example.carpoolingapplicationfrontend.viewmodel.RideDetailsUiModel
import com.example.carpoolingapplicationfrontend.viewmodel.RideUiModel
import com.example.carpoolingapplicationfrontend.viewmodel.StartTripUiState

enum class RideStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}

data class PassengerUiModel(
    val id: String,
    val name: String
)

data class VehicleUiModel(
    val make: String,
    val model: String,
    val licensePlate: String
)

//data class RideDetailsUiModel(
//    val id: String,
//    val type: String,
//    val from: String,
//    val to: String,
//    val date: String,
//    val time: String,
//    val seats: Int,
//    val passengers: List<PassengerUiModel>,
//    val notes: String?,
//    val estimatedCost: String,
//    val estimatedEnergy: String,
//    val estimatedCO2: String,
//    val vehicle: VehicleUiModel?
//)

@Composable
fun RideDetailsScreen(
    rideId: String,
    ride: RideDetailsUiModel?,
    bookingViewModel: BookingViewModel,
    onBackClick: () -> Unit
) {

    val startTripState = bookingViewModel.startTripState.collectAsState().value
    val endTripState = bookingViewModel.endTripState.collectAsState().value

    LaunchedEffect(startTripState) {

        if (startTripState is StartTripUiState.Success) {

            bookingViewModel.getBookingById(rideId.toLong())

            bookingViewModel.resetStartTripState()
        }
    }

    LaunchedEffect(endTripState) {

        if (endTripState is BookingRequestUiState.Success) {

            bookingViewModel.getBookingById(rideId.toLong())

            bookingViewModel.resetEndTripState()
        }
    }

//    var rideStatus by rememberSaveable {
//        mutableStateOf(RideStatus.NOT_STARTED)
//    }

//    val ride = RideDetailsUiModel(
//        id = rideId,
//        type = "offer",
//        from = "Clayton Campus - Building 11",
//        to = "Caulfield Campus - Library",
//        date = "2026-05-03",
//        time = "09:00 AM",
//        seats = 3,
//        passengers = listOf(
//            PassengerUiModel("1", "Sarah Johnson"),
//            PassengerUiModel("2", "Michael Chen")
//        ),
//        notes = "Will wait at main entrance for 5 minutes",
//        estimatedCost = "$15.00",
//        estimatedEnergy = "3.2 kWh",
//        estimatedCO2 = "2.4 kg",
//        vehicle = VehicleUiModel(
//            make = "Toyota",
//            model = "Camry",
//            licensePlate = "ABC123"
//        )
//    )

    if (ride == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading ride details...")
        }
        return
    }

    val rideStatus = when (ride.ride_status) {

        BookingStatus.ONGOING -> RideStatus.IN_PROGRESS

        BookingStatus.COMPLETED -> RideStatus.COMPLETED

        else -> RideStatus.NOT_STARTED
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6)),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            RideDetailsHeader(
                ride = ride,
                rideStatus = rideStatus,
                onBackClick = onBackClick
            )
        }

        item {
            Column(
                modifier = Modifier
                    .offset(y = (-30).dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                RouteCard(ride)

                if (ride.type == "offer" && ride.passengers.isNotEmpty()) {
                    PassengersCard(ride)
                }

                if (ride.vehicle != null) {
                    VehicleCard(ride.vehicle)
                }

                TripMetricsCard(ride)

                RideActionSection(
                    rideStatus = rideStatus,

                    onStartRide = {
                        bookingViewModel.startTrip(ride.id)
                    },

                    onCompleteRide = {
                        bookingViewModel.endTrip(ride.id)
                    },

                    onCancelRide = {
                        onBackClick()
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun RideDetailsHeader(
    ride: RideDetailsUiModel,
    rideStatus: RideStatus,
    onBackClick: () -> Unit
) {

    val subtitle = when (rideStatus) {
        RideStatus.NOT_STARTED -> "Scheduled"
        RideStatus.IN_PROGRESS -> "In Progress"
        RideStatus.COMPLETED -> "Completed"
    }

    Box(
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
                start = 20.dp,
                end = 20.dp,
                top = 16.dp,
                bottom = 60.dp
            )
    ) {

        Column {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        onBackClick()
                    }
                    .padding(4.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Back",
                    color = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Transparent)
                        .padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "Ride Details",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = subtitle,
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                RideTypeChip(ride.type)
            }
        }

        Box(
            modifier = Modifier.matchParentSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 0.dp)
            )
        }
    }
}

@Composable
private fun RideTypeChip(
    type: String
) {

    val background =
        if (type == "offer") Color(0xFFDCFCE7)
        else Color(0xFFDBEAFE)

    val textColor =
        if (type == "offer") Color(0xFF15803D)
        else Color(0xFF1D4ED8)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = if (type == "offer") "Offering" else "Requesting",
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun RouteCard(
    ride: RideDetailsUiModel
    //ride: RideUiModel
) {

    ElevatedCardContainer {

        Text(
            text = "Route",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row {

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = AppPrimaryGreen,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {

                Text(
                    text = "Departure",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = ride.from,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .width(1.dp)
                        .height(28.dp)
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Destination",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = ride.to,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null,
                tint = AppPrimaryGreen,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {

                Text(
                    text = "Date & Time",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "${ride.date} at ${ride.time}",
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (ride.remark != null) {

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEFF6FF))
                    .padding(14.dp)
            ) {

                Column {

                    Text(
                        text = "Notes:",
                        color = Color(0xFF1E3A8A),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = ride.remark,
                        color = Color(0xFF1D4ED8)
                    )
                }
            }
        }
    }
}

@Composable
private fun PassengersCard(
    ride: RideDetailsUiModel
) {

    ElevatedCardContainer {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Passengers",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "${ride.passengers.size}/${ride.seats}",
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ride.passengers.forEach { passenger ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF9FAFB))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                Color(0xFFBBF7D0),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = AppPrimaryGreen
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Text(
                        text = passenger.name,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun VehicleCard(
    vehicle: VehicleUiModel
) {

    ElevatedCardContainer {

        Text(
            text = "Vehicle Information",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        Color(0xFFDBEAFE),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Text(
                    text = "${vehicle.make} ${vehicle.model}",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "License Plate: ${vehicle.licensePlate}",
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun TripMetricsCard(
    ride: RideDetailsUiModel
) {

    ElevatedCardContainer {

        Text(
            text = "Trip Metrics",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            MetricCard(
                modifier = Modifier.weight(1f),
                backgroundColor = Color(0xFFECFDF5),
                icon = Icons.Default.AttachMoney,
                value = ride.estimatedCost,
                label = "Cost"
            )

            MetricCard(
                modifier = Modifier.weight(1f),
                backgroundColor = Color(0xFFFEFCE8),
                icon = Icons.Default.Bolt,
                value = ride.estimatedEnergy,
                label = "Energy"
            )

            MetricCard(
                modifier = Modifier.weight(1f),
                backgroundColor = Color(0xFFEFF6FF),
                icon = Icons.Default.Eco,
                value = ride.estimatedCO2,
                label = "CO₂ Saved"
            )
        }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun RideActionSection(
    rideStatus: RideStatus,
    onStartRide: () -> Unit,
    onCompleteRide: () -> Unit,
    onCancelRide: () -> Unit
) {

    when (rideStatus) {

        RideStatus.NOT_STARTED -> {

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Button(
                    onClick = onStartRide,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppPrimaryGreen
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Start Ride")
                }

                OutlinedButton(
                    onClick = onCancelRide,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFDC2626)
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Cancel Ride")
                }
            }
        }

        RideStatus.IN_PROGRESS -> {

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Button(
                    onClick = onCompleteRide,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppPrimaryGreen
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Complete Ride")
                }

                OutlinedButton(
                    onClick = onCancelRide,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFDC2626)
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Cancel Ride")
                }
            }
        }

        RideStatus.COMPLETED -> {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFECFDF5))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AppPrimaryGreen,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Ride Completed!",
                        color = Color(0xFF166534),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Thank you for riding green",
                        color = Color(0xFF15803D)
                    )
                }
            }
        }
    }
}

@Composable
private fun ElevatedCardContainer(
    content: @Composable () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            content()
        }
    }
}