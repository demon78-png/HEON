package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BroadcastOnPersonal
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AuditLog
import com.example.model.Game
import com.example.model.PlatformMetrics
import com.example.ui.components.HeonIconButton
import com.example.ui.components.HeonPrimaryButton
import com.example.ui.components.HeonSecondaryButton
import com.example.ui.theme.HeonAccentBlue
import com.example.ui.theme.HeonAccentNeon
import com.example.ui.theme.HeonAccentPurple
import com.example.ui.theme.HeonBackgroundPrimary
import com.example.ui.theme.HeonBorderSubtle
import com.example.ui.theme.HeonCardElevated
import com.example.ui.theme.HeonError
import com.example.ui.theme.HeonSuccess
import com.example.ui.theme.HeonTextMuted
import com.example.ui.theme.HeonTextPrimary
import com.example.ui.theme.HeonTextSecondary

@Composable
fun AdminScreen(
    isAdminLoggedIn: Boolean,
    platformMetrics: PlatformMetrics,
    games: List<Game>,
    auditLogs: List<AuditLog>,
    onBack: () -> Unit,
    onAuthenticateAdmin: (String) -> Boolean,
    onToggleFeatured: (String) -> Unit,
    onPublishAnnouncement: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var passkeyInput by remember { mutableStateOf("") }
    var announcementTitle by remember { mutableStateOf("") }
    var announcementBody by remember { mutableStateOf("") }

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
                testTag = "admin_back_button"
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                imageVector = Icons.Default.AdminPanelSettings,
                contentDescription = null,
                tint = Color(0xFFEF4444),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "HEON Admin Console",
                color = HeonTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (!isAdminLoggedIn) {
            // Protected Passkey Prompt Gate
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF381B22), RoundedCornerShape(18.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2B1319)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = HeonError,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Restricted Area",
                            color = HeonTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Enter authorized staff passkey to access platform controls, live metrics, and catalog management.",
                            color = HeonTextSecondary,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = passkeyInput,
                            onValueChange = { passkeyInput = it },
                            placeholder = { Text("Enter Passkey (Default: 7788)", color = HeonTextMuted, fontSize = 12.sp) },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF141722),
                                unfocusedContainerColor = Color(0xFF141722),
                                focusedBorderColor = HeonAccentNeon,
                                unfocusedBorderColor = HeonBorderSubtle,
                                focusedTextColor = HeonTextPrimary,
                                unfocusedTextColor = HeonTextPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_passkey_input")
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        HeonPrimaryButton(
                            text = "Authenticate Staff",
                            onClick = { onAuthenticateAdmin(passkeyInput) },
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "admin_auth_button"
                        )
                    }
                }
            }
        } else {
            // Logged-in Admin Dashboard
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Platform Overview Cards
                item(key = "admin_metrics") {
                    Text(
                        text = "Platform Telemetry",
                        color = HeonTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AdminMetricCard(
                            label = "GAMES",
                            value = platformMetrics.totalGamesCatalog.toString(),
                            sub = "${platformMetrics.publishedGames} Published",
                            color = HeonAccentNeon,
                            modifier = Modifier.weight(1f)
                        )
                        AdminMetricCard(
                            label = "PLAYERS",
                            value = "${platformMetrics.registeredPlayers / 1000}k",
                            sub = "Active Users",
                            color = HeonAccentPurple,
                            modifier = Modifier.weight(1f)
                        )
                        AdminMetricCard(
                            label = "PRIME VIP",
                            value = platformMetrics.primeSubscribers.toString(),
                            sub = "Subscribers",
                            color = HeonAccentBlue,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Announcement Broadcaster
                item(key = "broadcast_card") {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Campaign,
                                    contentDescription = null,
                                    tint = HeonAccentNeon,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Dispatch Platform Announcement",
                                    color = HeonTextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = announcementTitle,
                                onValueChange = { announcementTitle = it },
                                placeholder = { Text("Announcement Title", color = HeonTextMuted, fontSize = 12.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF141722),
                                    unfocusedContainerColor = Color(0xFF141722),
                                    focusedBorderColor = HeonAccentNeon,
                                    unfocusedBorderColor = HeonBorderSubtle,
                                    focusedTextColor = HeonTextPrimary,
                                    unfocusedTextColor = HeonTextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = announcementBody,
                                onValueChange = { announcementBody = it },
                                placeholder = { Text("Announcement message details...", color = HeonTextMuted, fontSize = 12.sp) },
                                maxLines = 3,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF141722),
                                    unfocusedContainerColor = Color(0xFF141722),
                                    focusedBorderColor = HeonAccentNeon,
                                    unfocusedBorderColor = HeonBorderSubtle,
                                    focusedTextColor = HeonTextPrimary,
                                    unfocusedTextColor = HeonTextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            HeonPrimaryButton(
                                text = "Broadcast to Notification Center",
                                onClick = {
                                    if (announcementTitle.isNotBlank()) {
                                        onPublishAnnouncement(announcementTitle, announcementBody)
                                        announcementTitle = ""
                                        announcementBody = ""
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "broadcast_announcement_button"
                            )
                        }
                    }
                }

                // Catalog Management: Toggle Featured
                item(key = "catalog_management_header") {
                    Text(
                        text = "Catalog Curation (Featured Carousel)",
                        color = HeonTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(games, key = { "adm_${it.id}" }) { game ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = game.title,
                                    color = HeonTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${game.developer} • ${game.version} • ${game.availabilityStatus.label}",
                                    color = HeonTextSecondary,
                                    fontSize = 11.sp
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (game.isFeatured) "Featured" else "Standard",
                                    color = if (game.isFeatured) HeonAccentNeon else HeonTextMuted,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Switch(
                                    checked = game.isFeatured,
                                    onCheckedChange = { onToggleFeatured(game.id) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color(0xFF090D02),
                                        checkedTrackColor = HeonAccentNeon,
                                        uncheckedThumbColor = HeonTextMuted,
                                        uncheckedTrackColor = Color(0xFF1E2230)
                                    )
                                )
                            }
                        }
                    }
                }

                // Audit Logs
                item(key = "audit_logs_header") {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = HeonTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Security Audit Trail",
                            color = HeonTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                items(auditLogs, key = { it.id }) { log ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(10.dp))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = log.action,
                                    color = HeonAccentNeon,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = log.timestamp,
                                    color = HeonTextMuted,
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${log.actor} → ${log.target}: ${log.details}",
                                color = HeonTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminMetricCard(
    label: String,
    value: String,
    sub: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
        modifier = modifier.border(1.dp, HeonBorderSubtle, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = label, color = HeonTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = color, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            Text(text = sub, color = HeonTextSecondary, fontSize = 9.sp)
        }
    }
}
