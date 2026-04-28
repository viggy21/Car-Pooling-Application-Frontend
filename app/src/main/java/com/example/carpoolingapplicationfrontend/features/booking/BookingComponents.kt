package com.example.carpoolingapplicationfrontend.features.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.carpoolingapplicationfrontend.ui.theme.DarkGrey
import com.example.carpoolingapplicationfrontend.ui.theme.LightGreen
import com.example.carpoolingapplicationfrontend.ui.theme.LightGrey

@Composable
fun BookingCard(
    origin: String,
    destination: String,
    startTime: String,
    status: Int,
    role: Int,
    remark: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = LightGreen)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Top row: route + status
            Row(
//                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$origin → $destination",
                    //style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f), // takes remaining space
                    maxLines = 2,                   // allows wrapping
                    overflow = TextOverflow.Ellipsis // optional fallback
                )

                Spacer(modifier = Modifier.width(8.dp))

                StatusChip(status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Time
            Text(
                text = "Start: $startTime",
                style = MaterialTheme.typography.bodyMedium
            )

            // Role
            Text(
                text = roleToText(role),
                style = MaterialTheme.typography.bodySmall,
                //color = Color.Gray
            )

            // Optional remark
            if (!remark.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = remark,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun roleToText(role: Int): String {
    return when (role) {
        0 -> "Passenger"
        1 -> "Driver"
        else -> "Unknown"
    }
}

@Composable
fun StatusChip(status: Int) {
    val (text, color) = when (status) {
        0 -> "Pending" to Color.Gray
        1 -> "Confirmed" to Color.Green
        2 -> "Cancelled" to Color.Red
        else -> "Unknown" to Color.DarkGray
    }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(color.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, color = color, style = MaterialTheme.typography.labelSmall)
    }
}