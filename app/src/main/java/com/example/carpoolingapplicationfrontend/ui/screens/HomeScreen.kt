package com.example.carpoolingapplicationfrontend.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carpoolingapplicationfrontend.ui.components.AddCircleIcon
import com.example.carpoolingapplicationfrontend.ui.components.AppPrimaryGreen
import com.example.carpoolingapplicationfrontend.ui.components.CalendarIcon
import com.example.carpoolingapplicationfrontend.ui.components.CarIcon
import com.example.carpoolingapplicationfrontend.ui.components.GroupIcon
import com.example.carpoolingapplicationfrontend.ui.components.ListIcon
import com.example.carpoolingapplicationfrontend.ui.components.LocationIcon
import com.example.carpoolingapplicationfrontend.ui.components.NotificationsIcon
import com.example.carpoolingapplicationfrontend.viewmodel.BookingRequestUiState
import com.example.carpoolingapplicationfrontend.viewmodel.BookingViewModel
import com.example.carpoolingapplicationfrontend.viewmodel.MatchUiModel
import com.example.carpoolingapplicationfrontend.viewmodel.RideUiModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.example.carpoolingapplicationfrontend.data.models.PageQueryRequest

@Composable
fun HomeScreen(
    viewModel: BookingViewModel,
    currentUserId: Long,
    onViewRideDetails: (rideId: String) -> Unit,
    onViewMatch: (matchId: String) -> Unit,
    onOfferRide: () -> Unit,
    onRequestRide: () -> Unit,
    onViewMyRides: () -> Unit,
    modifier: Modifier = Modifier,
    userName: String = "User"
) {
    val matchedBookingsState by viewModel.matchedBookingsState.collectAsState()
    val userBookingsState by viewModel.userBookingsState.collectAsState()
    //val upcomingRide = viewModel.getUpcomingRideUiModel(userBookingsState)
    val upcomingBookings = viewModel.getUpcomingRideUiModels(userBookingsState)
    val upcomingRide = upcomingBookings.firstOrNull()
    val matches = viewModel.getMatchUiModels(matchedBookingsState)
    val isLoading = matchedBookingsState is BookingRequestUiState.Loading ||
        userBookingsState is BookingRequestUiState.Loading
    val errorMessage = (matchedBookingsState as? BookingRequestUiState.Error)?.message
        ?: (userBookingsState as? BookingRequestUiState.Error)?.message

    LaunchedEffect(currentUserId) {

        Log.d("HomeScreen", "user id: " + currentUserId)
        // Load current user's bookings
        viewModel.getBookingsByUserId(
            id = currentUserId,
            request = PageQueryRequest(
                pageNum = 1,
                pageSize = 20,
                userId = currentUserId
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6)),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            HomeHeader(userName = userName)
        }

        item {
            Column(
                modifier = Modifier
                    .offset(y = (-34).dp)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                when {
                    isLoading -> LoadingCard()
                    errorMessage != null -> ErrorCard(message = errorMessage)
                    upcomingRide != null -> {
                        RideCard(
                            ride = upcomingRide,
                            onViewDetails = { onViewRideDetails(upcomingRide.id) }
                        )

                        // Commenting out the upcoming bookings section (not required)
//                        if (upcomingBookings.size > 1) {
//                            UpcomingBookingsCard(
//                                rides = upcomingBookings.drop(1),
//                                onViewRideDetails = onViewRideDetails
//                            )
//                        }
                    }
                    }
                MatchNotificationsCard(
                    matches = matches,
                    onViewMatch = onViewMatch
                )

                QuickActionsCard(
                    onOfferRide = onOfferRide,
                    onRequestRide = onRequestRide,
                    onViewMyRides = onViewMyRides
                )

                Spacer(modifier = Modifier.height(16.dp))

                }
        }
    }
}


@Composable
private fun HomeHeader(
    userName: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(Color(0xFF4CCB62), Color(0xFF2F9E61))
                )
            )
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 56.dp)
    ) {
        Column {
            Text(
                text = "Hello, $userName!",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Let's make today greener",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun RideCard(
    ride: RideUiModel,
    onViewDetails: () -> Unit
) {
    ElevatedSectionCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionHeader(title = "Upcoming Ride")
            StatusBadge(text = ride.ride_type)
        }

        Spacer(modifier = Modifier.height(22.dp))

        RouteBlock(
            fromLocation = ride.fromLocation,
            toLocation = ride.toLocation
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DetailText(icon = CalendarIcon, text = ride.date)
            DetailText(icon = null, text = ride.time)
            if (ride.seats != null) {
                DetailText(icon = GroupIcon, text = ride.seats)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onViewDetails,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppPrimaryGreen,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "View Details",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
@Composable
private fun MatchNotificationsCard(
    matches: List<MatchUiModel>,
    onViewMatch: (matchId: String) -> Unit
) {
    ElevatedSectionCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionHeader(title = "Match Notifications")
            Icon(
                imageVector = NotificationsIcon,
                contentDescription = null,
                tint = AppPrimaryGreen,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (matches.isEmpty()) {
            Text(
                text = "No matches yet",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                matches.forEach { match ->
                    MatchItem(
                        match = match,
                        onViewClick = { onViewMatch(match.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionsCard(
    onOfferRide: () -> Unit,
    onRequestRide: () -> Unit,
    onViewMyRides: () -> Unit
) {
    ElevatedSectionCard {
        SectionHeader(title = "Quick Actions")
        Spacer(modifier = Modifier.height(18.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            QuickActionButton(
                text = "Offer Ride",
                icon = CarIcon,
                onClick = onOfferRide,
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                text = "Request Ride",
                icon = AddCircleIcon,
                onClick = onRequestRide,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionButton(
            text = "View My Rides",
            icon = ListIcon,
            onClick = onViewMyRides,
            backgroundColor = Color(0xFFF8FAFC),
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MatchItem(
    match: MatchUiModel,
    onViewClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF0FDF4))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFBBF7D0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = GroupIcon,
                contentDescription = null,
                tint = AppPrimaryGreen,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = match.name,
                color = Color(0xFF111827),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = match.route,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = onViewClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppPrimaryGreen,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "View",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF0FDF4),
    contentColor: Color = AppPrimaryGreen
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 18.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun RouteBlock(
    fromLocation: String,
    toLocation: String
) {
    Row {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = LocationIcon,
                contentDescription = null,
                tint = AppPrimaryGreen,
                modifier = Modifier.size(22.dp)
            )
            Box(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .width(1.dp)
                    .height(24.dp)
                    .background(Color(0xFFD1D5DB))
            )
            Spacer(modifier = Modifier.size(22.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = fromLocation,
                color = Color(0xFF111827),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = toLocation,
                color = Color(0xFF111827),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DetailText(
    icon: ImageVector?,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun StatusBadge(
    text: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFDCFCE7))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color(0xFF15803D),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SectionHeader(
    title: String
) {
    Text(
        text = title,
        color = Color(0xFF111827),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun ElevatedSectionCard(
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
        ) {
            content()
        }
    }
}

@Composable
private fun LoadingCard() {
    ElevatedSectionCard {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = AppPrimaryGreen)
        }
    }
}

@Composable
private fun ErrorCard(
    message: String
) {
    ElevatedSectionCard {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}
//
//@Composable
//private fun UpcomingBookingsCard(
//    rides: List<RideUiModel>,
//    onViewRideDetails: (String) -> Unit
//) {
//    ElevatedSectionCard {
//
//        SectionHeader(title = "Upcoming Bookings")
//
//        Spacer(modifier = Modifier.height(18.dp))
//
//        Column(
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//
//            rides.forEach { ride ->
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(12.dp))
//                        .background(Color(0xFFF8FAFC))
//                        .clickable {
//                            onViewRideDetails(ride.id)
//                        }
//                        .padding(16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//                    Column(
//                        modifier = Modifier.weight(1f)
//                    ) {
//
//                        Text(
//                            text = "${ride.fromLocation} → ${ride.toLocation}",
//                            style = MaterialTheme.typography.bodyLarge,
//                            fontWeight = FontWeight.Bold,
//                            color = Color(0xFF111827)
//                        )
//
//                        Spacer(modifier = Modifier.height(6.dp))
//
//                        Text(
//                            text = "${ride.date} • ${ride.time}",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                    }
//
//                    StatusBadge(text = ride.status)
//                }
//            }
//        }
//    }
//}
