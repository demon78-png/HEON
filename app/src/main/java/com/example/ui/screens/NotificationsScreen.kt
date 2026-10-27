package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Game
import com.example.model.NotificationItem
import com.example.model.NotificationType
import com.example.ui.components.EmptyStateView
import com.example.ui.theme.HeonAccentBlue
import com.example.ui.theme.HeonAccentNeon
import com.example.ui.theme.HeonAccentPurple
import com.example.ui.theme.HeonBackgroundPrimary
import com.example.ui.theme.HeonBorderSubtle
import com.example.ui.theme.HeonCardElevated
import com.example.ui.theme.HeonTextMuted
import com.example.ui.theme.HeonTextPrimary
import com.example.ui.theme.HeonTextSecondary

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    allGames: List<Game>,
    onMarkRead: (String) -> Unit,
    onMarkAllRead: () -> Unit,
    onDismiss: (String) -> Unit,
    onSelectGame: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }

    val filtered = notifications.filter {
        when (selectedCategory) {
            "Updates" -> it.type == NotificationType.UPDATE
            "System" -> it.type == NotificationType.SYSTEM
            "Community" -> it.type == NotificationType.COMMUNITY
            else -> true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HeonBackgroundPrimary)
            .statusBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Player Alerts",
                    color = HeonTextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Platform announcements, game updates & community events",
                    color = HeonTextSecondary,
                    fontSize = 12.sp
                )
            }

            if (notifications.any { !it.isRead }) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E2812))
                        .clickable(onClick = onMarkAllRead)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("mark_all_read_button"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = null,
                        tint = HeonAccentNeon,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Mark read",
                        color = HeonAccentNeon,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Category Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Updates", "System", "Community").forEach { cat ->
                val isSelected = selectedCategory == cat
                item {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) Color(0xFF263D08) else HeonCardElevated)
                            .border(1.dp, if (isSelected) HeonAccentNeon else HeonBorderSubtle, RoundedCornerShape(16.dp))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) HeonAccentNeon else HeonTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (filtered.isEmpty()) {
            EmptyStateView(
                title = "No notifications in this category",
                message = "You're all caught up! Game patch notes and platform notices will show here.",
                icon = Icons.Default.Notifications,
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered, key = { it.id }) { item ->
                    NotificationCard(
                        item = item,
                        onClick = {
                            onMarkRead(item.id)
                            if (item.targetGameId != null) {
                                val target = allGames.find { it.id == item.targetGameId }
                                if (target != null) onSelectGame(target)
                            }
                        },
                        onDismiss = { onDismiss(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    item: NotificationItem,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val (icon, tint) = when (item.type) {
        NotificationType.UPDATE -> Icons.Default.SystemUpdate to HeonAccentNeon
        NotificationType.SYSTEM -> Icons.Default.Campaign to HeonAccentPurple
        NotificationType.COMMUNITY -> Icons.Default.SportsEsports to HeonAccentBlue
        NotificationType.DISCOUNT -> Icons.Default.Info to Color(0xFFFBBF24)
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!item.isRead) Color(0xFF1C2230) else HeonCardElevated
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (!item.isRead) Color(0x66B9FF39) else HeonBorderSubtle,
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .testTag("notif_card_${item.id}")
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF141722))
                    .border(1.dp, Color(0xFF262D40), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        color = HeonTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = if (!item.isRead) FontWeight.ExtraBold else FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = item.timestamp,
                        color = HeonTextMuted,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.message,
                    color = HeonTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                if (item.targetGameId != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tap to view game details →",
                        color = HeonAccentNeon,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = HeonTextMuted,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
