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
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.EnergySavingsLeaf
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.carpoolingapplicationfrontend.ui.components.AppPrimaryGreen
import com.example.carpoolingapplicationfrontend.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onEditProfileClick: () -> Unit = {},
    onVehicleInfoClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val authState by authViewModel.authState.collectAsState()

    val userName = "User"
    val userEmail = "user@monash.edu"

    val stats = ProfileStats(
        ridesOffered = 24,
        ridesRequested = 18,
        totalTripCost = "$342.50",
        energySaved = "58.4 kWh",
        co2Reduced = "42.8 kg"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6)),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            ProfileHeader()
        }

        item {
            Column(
                modifier = Modifier
                    .offset(y = (-34).dp)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                UserProfileCard(
                    userName = userName,
                    userEmail = userEmail
                )

                DashboardSummaryCard(stats = stats)

                AccountSettingsCard(
                    onEditProfileClick = onEditProfileClick,
                    onVehicleInfoClick = onVehicleInfoClick,
                    onChangePasswordClick = onChangePasswordClick,
                    onLogout = {
                        authViewModel.logout()
                        onLogout()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ProfileHeader() {
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
                start = 24.dp,
                end = 24.dp,
                top = 16.dp,
                bottom = 56.dp
            )
    ) {
        Column {
            Text(
                text = "Profile",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Your green journey",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun UserProfileCard(
    userName: String,
    userEmail: String
) {
    ElevatedProfileCard {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(82.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFBBF7D0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Color(0xFF15803D),
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column {
                Text(
                    text = userName,
                    color = Color(0xFF111827),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = userEmail,
                    color = Color(0xFF6B7280),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFF0FDF4))
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.EmojiEvents,
                contentDescription = null,
                tint = AppPrimaryGreen,
                modifier = Modifier.size(34.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "Eco Warrior",
                    color = Color(0xFF166534),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Keep up the great work!",
                    color = Color(0xFF15803D),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun DashboardSummaryCard(
    stats: ProfileStats
) {
    ElevatedProfileCard {
        Text(
            text = "Dashboard Summary",
            color = Color(0xFF111827),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SummaryStatCard(
                title = "Rides Offered",
                value = stats.ridesOffered.toString(),
                backgroundColor = Color(0xFFF0FDF4),
                valueColor = Color(0xFF16A34A),
                modifier = Modifier.weight(1f)
            )

            SummaryStatCard(
                title = "Rides Requested",
                value = stats.ridesRequested.toString(),
                backgroundColor = Color(0xFFEFF6FF),
                valueColor = Color(0xFF2563EB),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        SummaryInfoRow(
            icon = Icons.Outlined.AttachMoney,
            iconTint = Color(0xFF16A34A),
            title = "Total Trip Cost",
            value = stats.totalTripCost
        )

        Spacer(modifier = Modifier.height(12.dp))

        SummaryInfoRow(
            icon = Icons.Outlined.Bolt,
            iconTint = Color(0xFFEAB308),
            title = "Energy Saved",
            value = stats.energySaved
        )

        Spacer(modifier = Modifier.height(12.dp))

        SummaryInfoRow(
            icon = Icons.Outlined.EnergySavingsLeaf,
            iconTint = Color(0xFF2563EB),
            title = "CO₂ Reduced",
            value = stats.co2Reduced
        )
    }
}

@Composable
private fun SummaryStatCard(
    title: String,
    value: String,
    backgroundColor: Color,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .padding(vertical = 22.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = valueColor,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            color = Color(0xFF6B7280),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun SummaryInfoRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9FAFB))
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                color = Color(0xFF4B5563),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = value,
            color = Color(0xFF111827),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AccountSettingsCard(
    onEditProfileClick: () -> Unit,
    onVehicleInfoClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onLogout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column {
            Text(
                text = "Account Settings",
                color = Color(0xFF111827),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    start = 22.dp,
                    end = 22.dp,
                    top = 22.dp,
                    bottom = 8.dp
                )
            )

            SettingsItem(
                icon = Icons.Outlined.Person,
                title = "Edit Profile",
                onClick = onEditProfileClick
            )

            HorizontalDivider(color = Color(0xFFF3F4F6))

            SettingsItem(
                icon = Icons.Outlined.DirectionsCar,
                title = "Vehicle Information",
                onClick = onVehicleInfoClick
            )

            HorizontalDivider(color = Color(0xFFF3F4F6))

            SettingsItem(
                icon = Icons.Outlined.Lock,
                title = "Change Password",
                onClick = onChangePasswordClick
            )

            HorizontalDivider(color = Color(0xFFF3F4F6))

            LogoutItem(onClick = onLogout)
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF4B5563),
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = title,
                color = Color(0xFF111827),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = Color(0xFF9CA3AF),
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun LogoutItem(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Logout,
            contentDescription = null,
            tint = Color(0xFFDC2626),
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = "Log Out",
            color = Color(0xFFDC2626),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ElevatedProfileCard(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(22.dp)
        ) {
            content()
        }
    }
}

data class ProfileStats(
    val ridesOffered: Int,
    val ridesRequested: Int,
    val totalTripCost: String,
    val energySaved: String,
    val co2Reduced: String
)