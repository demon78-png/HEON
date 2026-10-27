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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.HeonIconButton
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

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var wifiOnly by remember { mutableStateOf(true) }
    var autoUpdate by remember { mutableStateOf(true) }
    var highContrast by remember { mutableStateOf(false) }
    var reducedMotion by remember { mutableStateOf(false) }
    var hapticFeedback by remember { mutableStateOf(true) }
    var deleteApkAfterInstall by remember { mutableStateOf(true) }
    var cacheSizeMb by remember { mutableStateOf(240) }

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
                testTag = "settings_back_button"
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Settings & Preferences",
                color = HeonTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Downloads & Network
            item(key = "section_network") {
                SettingsSectionContainer(title = "Downloads & Network", icon = Icons.Default.Wifi) {
                    SettingsSwitchRow(
                        title = "Download over Wi-Fi only",
                        subtitle = "Prevents heavy game package downloads over cellular data",
                        checked = wifiOnly,
                        onCheckedChange = { wifiOnly = it }
                    )
                    SettingsDivider()
                    SettingsSwitchRow(
                        title = "Auto-update games on Wi-Fi",
                        subtitle = "Pre-downloads patches when plugged in and connected to Wi-Fi",
                        checked = autoUpdate,
                        onCheckedChange = { autoUpdate = it }
                    )
                    SettingsDivider()
                    SettingsSwitchRow(
                        title = "Delete APK package after install",
                        subtitle = "Frees up storage immediately after game verifies and installs",
                        checked = deleteApkAfterInstall,
                        onCheckedChange = { deleteApkAfterInstall = it }
                    )
                }
            }

            // Accessibility & Controls
            item(key = "section_accessibility") {
                SettingsSectionContainer(title = "Accessibility & Display", icon = Icons.Default.Accessibility) {
                    SettingsSwitchRow(
                        title = "High contrast text",
                        subtitle = "Increases legibility of labels and metadata",
                        checked = highContrast,
                        onCheckedChange = { highContrast = it }
                    )
                    SettingsDivider()
                    SettingsSwitchRow(
                        title = "Reduced motion & animations",
                        subtitle = "Minimizes parallax transitions and card shimmers",
                        checked = reducedMotion,
                        onCheckedChange = { reducedMotion = it }
                    )
                    SettingsDivider()
                    SettingsSwitchRow(
                        title = "Haptic feedback",
                        subtitle = "Subtle tactile vibrations on button taps and completions",
                        checked = hapticFeedback,
                        onCheckedChange = { hapticFeedback = it }
                    )
                }
            }

            // Storage & Cache
            item(key = "section_storage") {
                SettingsSectionContainer(title = "Storage & Cache Management", icon = Icons.Default.Storage) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Temporary Image & Catalog Cache",
                                color = HeonTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Currently occupying $cacheSizeMb MB of app cache",
                                color = HeonTextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        HeonSecondaryButton(
                            text = if (cacheSizeMb == 0) "Cleared" else "Clear Cache",
                            onClick = {
                                cacheSizeMb = 0
                                onShowToast("Cache wiped clean. 240 MB freed!")
                            },
                            icon = Icons.Default.CleaningServices,
                            modifier = Modifier.height(38.dp)
                        )
                    }
                }
            }

            // About HEON
            item(key = "section_about") {
                SettingsSectionContainer(title = "About HEON", icon = Icons.Default.Info) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "App Version", color = HeonTextSecondary, fontSize = 12.sp)
                            Text(text = "1.0.4 (Build 104)", color = HeonAccentNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        SettingsDivider()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Platform Architecture", color = HeonTextSecondary, fontSize = 12.sp)
                            Text(text = "Android Native (Jetpack Compose)", color = HeonTextPrimary, fontSize = 12.sp)
                        }
                        SettingsDivider()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Privacy Policy & Licenses", color = HeonTextSecondary, fontSize = 12.sp)
                            Text(
                                text = "View Legal Docs",
                                color = HeonAccentNeon,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onShowToast("Opening legal documents...") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionContainer(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(16.dp))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = HeonAccentNeon, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, color = HeonTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            HorizontalDivider(color = Color(0xFF222736), thickness = 0.8.dp)
            content()
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = HeonTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, color = HeonTextSecondary, fontSize = 11.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF090D02),
                checkedTrackColor = HeonAccentNeon,
                uncheckedThumbColor = HeonTextMuted,
                uncheckedTrackColor = Color(0xFF1E2230)
            )
        )
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(color = Color(0xFF1D2230), thickness = 0.8.dp)
}
