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
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CosmeticReward
import com.example.model.PlayerChallenge
import com.example.model.SeasonPassInfo
import com.example.ui.components.HeonIconButton
import com.example.ui.components.HeonPrimaryButton
import com.example.ui.components.HeonSecondaryButton
import com.example.ui.theme.HeonAccentBlue
import com.example.ui.theme.HeonAccentNeon
import com.example.ui.theme.HeonAccentPurple
import com.example.ui.theme.HeonBackgroundPrimary
import com.example.ui.theme.HeonBorderSubtle
import com.example.ui.theme.HeonCardElevated
import com.example.ui.theme.HeonSuccess
import com.example.ui.theme.HeonTextMuted
import com.example.ui.theme.HeonTextPrimary
import com.example.ui.theme.HeonTextSecondary

@Composable
fun RewardsScreen(
    seasonPass: SeasonPassInfo,
    challenges: List<PlayerChallenge>,
    cosmetics: List<CosmeticReward>,
    onBack: () -> Unit,
    onClaimChallenge: (String) -> Unit,
    onEquipCosmetic: (String) -> Unit,
    onUpgradePass: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var selectedTab by remember { mutableStateOf("Challenges") }

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
                onClick = onBack,
                testTag = "rewards_back_button"
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = HeonAccentNeon,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Gamification & Season Horizon",
                color = HeonTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Season Horizon Pass Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF26123D), Color(0xFF14240C), Color(0xFF0F1524))
                    )
                )
                .border(1.dp, Color(0x66B9FF39), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SEASON ${seasonPass.seasonNumber} • ${seasonPass.seasonName.uppercase()}",
                            color = HeonAccentNeon,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Tier ${seasonPass.currentTier} of ${seasonPass.maxTier}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = HeonTextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${seasonPass.daysRemaining} days left",
                            color = HeonTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { seasonPass.currentTierXp.toFloat() / seasonPass.maxTierXp.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = HeonAccentNeon,
                    trackColor = Color(0xFF202638)
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${seasonPass.currentTierXp} / ${seasonPass.maxTierXp} XP to Tier ${seasonPass.currentTier + 1}",
                        color = HeonTextMuted,
                        fontSize = 10.sp
                    )

                    if (!seasonPass.isPremiumUnlocked) {
                        Text(
                            text = "Unlock Prime Pass →",
                            color = HeonAccentNeon,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable(onClick = onUpgradePass)
                        )
                    }
                }
            }
        }

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(HeonCardElevated)
                .padding(4.dp)
        ) {
            listOf("Challenges", "Rewards & Cosmetics").forEach { tab ->
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
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            when (selectedTab) {
                "Challenges" -> {
                    items(challenges, key = { it.id }) { ch ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    if (ch.isCompleted && !ch.isClaimed) HeonAccentNeon else HeonBorderSubtle,
                                    RoundedCornerShape(14.dp)
                                )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFF1B2433))
                                            .padding(horizontal = 7.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = ch.type.label,
                                            color = HeonAccentNeon,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Text(
                                        text = "+${ch.xpReward} XP",
                                        color = HeonAccentNeon,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = ch.title,
                                    color = HeonTextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = ch.description,
                                    color = HeonTextSecondary,
                                    fontSize = 11.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = { ch.progressFloat },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = if (ch.isCompleted) HeonSuccess else HeonAccentNeon,
                                    trackColor = Color(0xFF222838)
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${ch.currentProgress} / ${ch.targetCount} completed",
                                        color = HeonTextMuted,
                                        fontSize = 10.sp
                                    )

                                    when {
                                        ch.isClaimed -> {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = HeonSuccess,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(text = "Claimed", color = HeonSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        ch.isCompleted -> {
                                            HeonPrimaryButton(
                                                text = "Claim +${ch.xpReward} XP",
                                                onClick = { onClaimChallenge(ch.id) },
                                                modifier = Modifier.height(34.dp)
                                            )
                                        }
                                        else -> {
                                            Text(text = ch.expiresIn, color = HeonTextMuted, fontSize = 10.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "Rewards & Cosmetics" -> {
                    items(cosmetics, key = { it.id }) { item ->
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
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF161926))
                                        .border(1.5.dp, if (item.isEquipped) HeonAccentNeon else HeonBorderSubtle, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = item.iconEmoji, fontSize = 22.sp)
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = item.name,
                                            color = HeonTextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = item.rarity,
                                            color = if (item.rarity == "Legendary") Color(0xFFFBBF24) else HeonAccentPurple,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = "${item.type.label} • Req. Lvl ${item.requiredLevel}",
                                        color = HeonTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }

                                if (item.isEquipped) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFF1E310E))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(text = "EQUIPPED", color = HeonAccentNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    HeonSecondaryButton(
                                        text = "Equip",
                                        onClick = { onEquipCosmetic(item.id) },
                                        modifier = Modifier.height(34.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
