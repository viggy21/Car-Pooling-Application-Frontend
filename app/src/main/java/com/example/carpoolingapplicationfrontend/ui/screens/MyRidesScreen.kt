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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.ui.text.style.TextOverflow
import com.example.carpoolingapplicationfrontend.ui.components.AppPrimaryGreen
import com.example.carpoolingapplicationfrontend.ui.components.CalendarIcon
import com.example.carpoolingapplicationfrontend.ui.components.GroupIcon
import com.example.carpoolingapplicationfrontend.ui.components.LocationIcon
import com.example.carpoolingapplicationfrontend.ui.components.ProfileIcon
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
enum class RideTab(
    val title: String
) {
    UPCOMING("Upcoming"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

data class MyRideUiModel(
    val id: String,
    val type: RideType,
    val from: String,
    val to: String,
    val date: String,
    val time: String,
    val seats: Int? = null,
    val passengers: Int? = null,
    val driver: String? = null,
    val cost: String? = null,
    val energy: String? = null,
    val co2: String? = null,
    val cancellationReason: String? = null
)

enum class RideType {
    OFFER,
    REQUEST
}

@Composable
fun MyRidesScreen(
    onRideClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var selectedTab by remember {
        mutableStateOf(RideTab.UPCOMING)
    }

    val rides = remember {
        mapOf(
            RideTab.UPCOMING to listOf(
                MyRideUiModel(
                    id = "1",
                    type = RideType.OFFER,
                    from = "Clayton Campus",
                    to = "Caulfield Campus",
                    date = "2026-05-03",
                    time = "09:00 AM",
                    seats = 3,
                    passengers = 2
                ),
                MyRideUiModel(
                    id = "2",
                    type = RideType.REQUEST,
                    from = "Home",
                    to = "Clayton Campus",
                    date = "2026-05-05",
                    time = "08:30 AM",
                    driver = "John Smith"
                )
            ),

            RideTab.IN_PROGRESS to listOf(
                MyRideUiModel(
                    id = "3",
                    type = RideType.OFFER,
                    from = "Clayton Campus",
                    to = "Melbourne CBD",
                    date = "2026-05-02",
                    time = "05:00 PM",
                    seats = 4,
                    passengers = 3
                )
            ),

            RideTab.COMPLETED to listOf(
                MyRideUiModel(
                    id = "4",
                    type = RideType.OFFER,
                    from = "Clayton Campus",
                    to = "Caulfield Campus",
                    date = "2026-04-28",
                    time = "09:00 AM",
                    seats = 3,
                    passengers = 3,
                    cost = "$12.50",
                    energy = "2.5 kWh",
                    co2 = "1.8 kg"
                ),
                MyRideUiModel(
                    id = "5",
                    type = RideType.REQUEST,
                    from = "Home",
                    to = "Clayton Campus",
                    date = "2026-04-25",
                    time = "08:30 AM",
                    driver = "Emma Wilson",
                    cost = "$8.00",
                    energy = "1.8 kWh",
                    co2 = "1.2 kg"
                )
            ),

            RideTab.CANCELLED to listOf(
                MyRideUiModel(
                    id = "6",
                    type = RideType.OFFER,
                    from = "Clayton Campus",
                    to = "Caulfield Campus",
                    date = "2026-04-20",
                    time = "09:00 AM",
                    cancellationReason = "Weather conditions"
                )
            )
        )
    }

    val currentRides = rides[selectedTab].orEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
    ) {

        MyRidesHeader()

        RideTabs(
            selectedTab = selectedTab,
            onTabSelected = {
                selectedTab = it
            }
        )

        if (currentRides.isEmpty()) {
            EmptyRidesCard(
                selectedTab = selectedTab
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 16.dp,
                    vertical = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(currentRides) { ride ->
                    MyRideCard(
                        ride = ride,
                        selectedTab = selectedTab,
                        onClick = {
                            onRideClick(ride.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MyRidesHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF22C55E),
                        Color(0xFF059669)
                    )
                )
            )
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 18.dp,
                bottom = 26.dp
            )
    ) {
        Column {
            Text(
                text = "My Rides",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Track all your journeys",
                color = Color.White.copy(alpha = 0.92f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun RideTabs(
    selectedTab: RideTab,
    onTabSelected: (RideTab) -> Unit
) {

    val tabs = RideTab.entries

    ScrollableTabRow(
        selectedTabIndex = tabs.indexOf(selectedTab),
        edgePadding = 0.dp,
        containerColor = Color.White,
        contentColor = AppPrimaryGreen,
        divider = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFE5E7EB))
            )
        },
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(
                    tabPositions[tabs.indexOf(selectedTab)]
                ),
                color = AppPrimaryGreen,
                height = 2.dp
            )
        }
    ) {

        tabs.forEach { tab ->

            val isSelected = selectedTab == tab

            Tab(
                selected = isSelected,
                onClick = {
                    onTabSelected(tab)
                },
                text = {
                    Text(
                        text = tab.title,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelected) {
                            AppPrimaryGreen
                        } else {
                            Color(0xFF4B5563)
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun MyRideCard(
    ride: MyRideUiModel,
    selectedTab: RideTab,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                RideTypeBadge(type = ride.type)

                if (selectedTab == RideTab.COMPLETED && ride.cost != null) {
                    Text(
                        text = ride.cost,
                        color = AppPrimaryGreen,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            RideRoute(
                from = ride.from,
                to = ride.to
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = CalendarIcon,
                        contentDescription = null,
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = ride.date,
                        color = Color(0xFF6B7280),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "  •  ",
                        color = Color(0xFF9CA3AF)
                    )

                    Text(
                        text = ride.time,
                        color = Color(0xFF6B7280),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                when {
                    ride.type == RideType.OFFER &&
                            ride.passengers != null &&
                            ride.seats != null -> {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = GroupIcon,
                                contentDescription = null,
                                tint = Color(0xFF6B7280),
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "${ride.passengers}/${ride.seats}",
                                color = Color(0xFF6B7280),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    ride.type == RideType.REQUEST &&
                            ride.driver != null -> {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = ProfileIcon,
                                contentDescription = null,
                                tint = Color(0xFF6B7280),
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = ride.driver,
                                color = Color(0xFF6B7280),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            if (
                selectedTab == RideTab.COMPLETED &&
                ride.energy != null &&
                ride.co2 != null
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFF1F5F9))
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    StatsItem(
                        value = ride.energy,
                        label = "Energy saved",
                        modifier = Modifier.weight(1f)
                    )

                    StatsItem(
                        value = ride.co2,
                        label = "CO₂ reduced",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (
                selectedTab == RideTab.CANCELLED &&
                ride.cancellationReason != null
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFEF2F2))
                        .padding(12.dp)
                ) {

                    Text(
                        text = "Cancelled: ${ride.cancellationReason}",
                        color = Color(0xFFB91C1C),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun RideTypeBadge(
    type: RideType
) {

    val backgroundColor = if (type == RideType.OFFER) {
        Color(0xFFDCFCE7)
    } else {
        Color(0xFFDBEAFE)
    }

    val textColor = if (type == RideType.OFFER) {
        Color(0xFF15803D)
    } else {
        Color(0xFF1D4ED8)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {

        Text(
            text = if (type == RideType.OFFER) {
                "Offering"
            } else {
                "Requesting"
            },
            color = textColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RideRoute(
    from: String,
    to: String
) {

    Row {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = LocationIcon,
                contentDescription = null,
                tint = AppPrimaryGreen,
                modifier = Modifier.size(22.dp)
            )

            Box(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .width(1.dp)
                    .height(22.dp)
                    .background(Color(0xFFD1D5DB))
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {

            Text(
                text = from,
                color = Color(0xFF111827),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = to,
                color = Color(0xFF111827),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StatsItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {

        Text(
            text = value,
            color = Color(0xFF111827),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            color = Color(0xFF6B7280),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun EmptyRidesCard(
    selectedTab: RideTab
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "No ${selectedTab.title.lowercase()} rides",
                    color = Color(0xFF6B7280),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}