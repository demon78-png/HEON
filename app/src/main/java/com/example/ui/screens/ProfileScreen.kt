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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.components.HeonPrimaryButton
import com.example.ui.components.HeonSecondaryButton
import com.example.ui.theme.HeonAccentBlue
import com.example.ui.theme.HeonAccentNeon
import com.example.ui.theme.HeonAccentPurple
import com.example.ui.theme.HeonBackgroundPrimary
import com.example.ui.theme.HeonBorderSubtle
import com.example.ui.theme.HeonCardElevated
import com.example.ui.theme.HeonTextMuted
import com.example.ui.theme.HeonTextPrimary
import com.example.ui.theme.HeonTextSecondary

import androidx.compose.material.icons.filled.EmojiEvents

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    isPremium: Boolean,
    onOpenAuth: () -> Unit,
    onOpenPremium: () -> Unit,
    onOpenRewards: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenSupport: () -> Unit,
    onOpenAdmin: () -> Unit,
    onOpenSocial: () -> Unit,
    onSignOut: () -> Unit,
    onUpdateProfile: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var editDisplayName by remember(userProfile.displayName) { mutableStateOf(userProfile.displayName) }
    var editUsername by remember(userProfile.username) { mutableStateOf(userProfile.username) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HeonBackgroundPrimary)
            .statusBarsPadding()
    ) {
        // Page Title
        Text(
            text = "Player Profile",
            color = HeonTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Gamer Card with Level and XP
            item(key = "player_card") {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, HeonBorderSubtle, RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(HeonAccentNeon, HeonAccentPurple)
                                        )
                                    )
                                    .padding(2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(Color(0xFF0F121C)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = HeonAccentNeon,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = userProfile.displayName,
                                        color = HeonTextPrimary,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (isPremium) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Default.VerifiedUser,
                                            contentDescription = "Prime Member",
                                            tint = HeonAccentNeon,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "@${userProfile.username}",
                                    color = HeonTextSecondary,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isPremium) Color(0xFF2B3D0A) else Color(0xFF22283A))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (isPremium) "HEON PRIME VIP" else userProfile.membershipTier,
                                        color = if (isPremium) HeonAccentNeon else HeonTextSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    editDisplayName = userProfile.displayName
                                    editUsername = userProfile.username
                                    showEditDialog = true
                                },
                                modifier = Modifier.testTag("edit_profile_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Profile",
                                    tint = HeonAccentNeon,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // XP Progress
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Player Level ${userProfile.level}",
                                color = HeonTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${(userProfile.xpProgress * 100).toInt()}% to Level ${userProfile.level + 1}",
                                color = HeonTextMuted,
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { userProfile.xpProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = HeonAccentNeon,
                            trackColor = Color(0xFF282D3D)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Stats
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF131620))
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            ProfileStat(label = "SAVED", value = userProfile.savedGamesCount.toString())
                            ProfileStat(label = "DOWNLOADS", value = userProfile.downloadsCount.toString())
                            ProfileStat(label = "PLAYTIME", value = "${userProfile.hoursPlayedTotal}h")
                        }
                    }
                }
            }

            // Guest Sync Banner (if guest)
            if (userProfile.isGuest) {
                item(key = "guest_banner") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF1B2412))
                            .border(1.dp, Color(0xFF3B5720), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Sync,
                                    contentDescription = null,
                                    tint = HeonAccentNeon,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Guest Mode Active",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your wishlist and downloads are currently stored locally. Upgrade to a free account to back up and sync your library across devices.",
                                color = Color(0xFFC7DEB8),
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            HeonPrimaryButton(
                                text = "Sign In / Register Account",
                                onClick = onOpenAuth,
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "profile_signin_button"
                            )
                        }
                    }
                }
            }

            // Navigation Links
            item(key = "nav_menu_items") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(HeonCardElevated)
                        .border(1.dp, HeonBorderSubtle, RoundedCornerShape(16.dp))
                ) {
                    ProfileMenuRow(
                        icon = Icons.Default.Diamond,
                        title = "HEON Prime Pass & Billing",
                        subtitle = if (isPremium) "Subscription Active • Manage Perks" else "Unlock 120Hz cloud streaming & zero ads",
                        tint = if (isPremium) HeonAccentNeon else Color(0xFFFBBF24),
                        onClick = onOpenPremium
                    )

                    ProfileMenuRow(
                        icon = Icons.Default.EmojiEvents,
                        title = "Season Horizon Pass & Bounties",
                        subtitle = "Daily challenges, cosmetic avatar frames & XP rewards",
                        tint = HeonAccentNeon,
                        onClick = onOpenRewards
                    )

                    ProfileMenuRow(
                        icon = Icons.Default.Leaderboard,
                        title = "Community & Leaderboards",
                        subtitle = "Global player ranks, friends & achievements",
                        tint = HeonAccentPurple,
                        onClick = onOpenSocial
                    )

                    ProfileMenuRow(
                        icon = Icons.Default.Settings,
                        title = "Settings & Device Preferences",
                        subtitle = "Theme, Wi-Fi downloads, accessibility & cache",
                        tint = HeonAccentBlue,
                        onClick = onOpenSettings
                    )

                    ProfileMenuRow(
                        icon = Icons.Default.HelpOutline,
                        title = "Help Center & Bug Reports",
                        subtitle = "FAQs, support tickets & diagnostic logs",
                        tint = HeonTextPrimary,
                        onClick = onOpenSupport
                    )

                    ProfileMenuRow(
                        icon = Icons.Default.AdminPanelSettings,
                        title = "Admin Management Console",
                        subtitle = "Platform metrics, catalog review & announcements",
                        tint = Color(0xFFEF4444),
                        onClick = onOpenAdmin
                    )
                }
            }

            // Account Actions
            if (!userProfile.isGuest) {
                item(key = "account_actions") {
                    HeonSecondaryButton(
                        text = "Sign Out",
                        onClick = onSignOut,
                        icon = Icons.Default.Logout,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "sign_out_button"
                    )
                }
            }
        }

        if (showEditDialog) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                containerColor = HeonCardElevated,
                title = {
                    Text(
                        text = "Edit Player Profile",
                        color = HeonTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Update your public display name and gamer tag.",
                            color = HeonTextSecondary,
                            fontSize = 12.sp
                        )
                        OutlinedTextField(
                            value = editDisplayName,
                            onValueChange = { editDisplayName = it },
                            label = { Text("Display Name") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = HeonTextPrimary,
                                unfocusedTextColor = HeonTextPrimary,
                                focusedBorderColor = HeonAccentNeon,
                                unfocusedBorderColor = HeonBorderSubtle,
                                focusedLabelColor = HeonAccentNeon,
                                unfocusedLabelColor = HeonTextSecondary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("edit_display_name_input")
                        )
                        OutlinedTextField(
                            value = editUsername,
                            onValueChange = { editUsername = it },
                            label = { Text("Gamer Tag / Username") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = HeonTextPrimary,
                                unfocusedTextColor = HeonTextPrimary,
                                focusedBorderColor = HeonAccentNeon,
                                unfocusedBorderColor = HeonBorderSubtle,
                                focusedLabelColor = HeonAccentNeon,
                                unfocusedLabelColor = HeonTextSecondary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("edit_username_input")
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onUpdateProfile(editDisplayName, editUsername)
                            showEditDialog = false
                        },
                        modifier = Modifier.testTag("save_profile_button")
                    ) {
                        Text("Save Changes", color = HeonAccentNeon, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) {
                        Text("Cancel", color = HeonTextMuted)
                    }
                }
            )
        }
    }
}

@Composable
private fun ProfileStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = HeonAccentNeon,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = label,
            color = HeonTextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ProfileMenuRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF141724)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = HeonTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = HeonTextSecondary,
                fontSize = 11.sp,
                maxLines = 1
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = HeonTextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}
