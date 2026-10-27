package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Achievement
import com.example.model.FriendPlayer
import com.example.model.FriendStatus
import com.example.model.LeaderboardEntry
import com.example.ui.components.HeonIconButton
import com.example.ui.components.HeonPrimaryButton
import com.example.ui.components.RatingPill
import com.example.ui.theme.HeonAccentBlue
import com.example.ui.theme.HeonAccentNeon
import com.example.ui.theme.HeonAccentPurple
import com.example.ui.theme.HeonBackgroundPrimary
import com.example.ui.theme.HeonBorderSubtle
import com.example.ui.theme.HeonCardElevated
import com.example.ui.theme.HeonRatingStar
import com.example.ui.theme.HeonSuccess
import com.example.ui.theme.HeonTextMuted
import com.example.ui.theme.HeonTextPrimary
import com.example.ui.theme.HeonTextSecondary

@Composable
fun SocialScreen(
    leaderboard: List<LeaderboardEntry>,
    friends: List<FriendPlayer>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var selectedTab by remember { mutableStateOf("Leaderboard") }

    val sampleAchievements = listOf(
        Achievement("ach_01", "game_01", "CyberBlade Maestro", "Complete 50 consecutive parries without taking damage", "⚔️", 500, isUnlocked = true, unlockedDate = "Yesterday"),
        Achievement("ach_02", "game_02", "Apex Speed Demon", "Exceed 320 km/h on Neo-City GP without bumping walls", "🏎️", 750, isUnlocked = true, unlockedDate = "3 days ago"),
        Achievement("ach_03", "game_03", "Event Horizon Traveler", "Discover all 12 quantum wormholes in the Perseus arm", "🌌", 1000, isUnlocked = false),
        Achievement("ach_04", "game_01", "Silent Infiltration", "Clear arcology sector 4 without raising security alert", "🥷", 400, isUnlocked = false)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HeonBackgroundPrimary)
            .statusBarsPadding()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeonIconButton(
                icon = Icons.Default.ArrowBack,
                contentDescription = "Back",
                onClick = onBack
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Community & Leaderboards",
                color = HeonTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(HeonCardElevated)
                .padding(4.dp)
        ) {
            listOf("Leaderboard", "Friends (${friends.size})", "Achievements").forEach { tab ->
                val isSelected = selectedTab == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color(0xFF263D08) else Color.Transparent)
                        .clickable { selectedTab = tab }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        color = if (isSelected) HeonAccentNeon else HeonTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            when (selectedTab) {
                "Leaderboard" -> {
                    items(leaderboard, key = { it.rank }) { entry ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (entry.isCurrentPlayer) Color(0xFF1E2B12) else HeonCardElevated
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    if (entry.isCurrentPlayer) HeonAccentNeon else HeonBorderSubtle,
                                    RoundedCornerShape(12.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (entry.rank) {
                                                1 -> HeonRatingStar
                                                2 -> Color(0xFFCBD5E1)
                                                3 -> Color(0xFFD97706)
                                                else -> Color(0xFF242838)
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "#${entry.rank}",
                                        color = if (entry.rank <= 3) Color(0xFF090D02) else HeonTextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = entry.username,
                                            color = HeonTextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (entry.isCurrentPlayer) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "(You)",
                                                color = HeonAccentNeon,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Text(
                                        text = entry.tierBadge,
                                        color = HeonTextSecondary,
                                        fontSize = 10.sp
                                    )
                                }

                                Text(
                                    text = entry.scoreDisplay,
                                    color = HeonAccentNeon,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }

                "Friends" -> {
                    items(friends, key = { it.id }) { friend ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, HeonBorderSubtle, RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(HeonAccentPurple),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = friend.avatarTag,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = friend.username,
                                        color = HeonTextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(
                                                    if (friend.status == FriendStatus.OFFLINE) Color(0xFF6B7280) else HeonSuccess,
                                                    CircleShape
                                                )
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (friend.currentGame != null) "Playing ${friend.currentGame}" else friend.status.label,
                                            color = HeonTextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                if (friend.status == FriendStatus.ONLINE || friend.status == FriendStatus.IN_GAME) {
                                    HeonPrimaryButton(
                                        text = "Join",
                                        onClick = { },
                                        modifier = Modifier.height(34.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                "Achievements" -> {
                    items(sampleAchievements, key = { it.id }) { ach ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    if (ach.isUnlocked) Color(0x66B9FF39) else HeonBorderSubtle,
                                    RoundedCornerShape(12.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(if (ach.isUnlocked) Color(0xFF1E2E10) else Color(0xFF151822)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = ach.iconEmoji, fontSize = 20.sp)
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = ach.title,
                                            color = HeonTextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (ach.isUnlocked) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Unlocked",
                                                tint = HeonSuccess,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = ach.description,
                                        color = HeonTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }

                                Text(
                                    text = "+${ach.xpValue} XP",
                                    color = if (ach.isUnlocked) HeonAccentNeon else HeonTextMuted,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
