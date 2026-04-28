package com.example.carpoolingapplicationfrontend.features.booking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun BookingListScreen(
    navController: NavController,
    viewModel: BookingListViewModel
) {
    val state = viewModel.uiState

    // trigger API call once

    LaunchedEffect(Unit) {
        viewModel.fetchBookings(userId = 1) // TODO: replace with actual user Id
    }

    Column (modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Booking List",
//            style = MaterialTheme.typography.headlineMedium,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        when (state) {

            is BookingUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is BookingUiState.Success -> {
                BookingList(state.bookings)
            }

            is BookingUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${state.message}")
                }
            }
        }
    }

}

@Composable
fun BookingList(
    bookings: List<BookingDto>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = bookings,
            key = { it.id } // improves performance + avoids recomposition issues
        ) { booking ->

            BookingCard(
                origin = booking.originName,
                destination = booking.destName,
                startTime = booking.expectedStartTime, // TODO: formatTime(booking.expectedStartTime),
                status = booking.status,
                role = booking.role,
                remark = booking.remark,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

// TODO: implement the below
//fun formatTime(isoString: String): String {
//    return try {
//        val instant = java.time.Instant.parse(isoString)
//        val formatter = java.time.format.DateTimeFormatter
//            .ofPattern("dd MMM, hh:mm a")
//            .withZone(java.time.ZoneId.systemDefault())
//
//        formatter.format(instant)
//    } catch (e: Exception) {
//        isoString // fallback
//    }
//}