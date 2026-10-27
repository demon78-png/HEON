package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Wifi
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CloudServerRegion
import com.example.model.CloudSession
import com.example.model.CloudSessionState
import com.example.model.DefaultCloudRegions
import com.example.model.Game
import com.example.model.StreamQualityPreset
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
fun CloudGamingScreen(
    game: Game,
    session: CloudSession,
    onBack: () -> Unit,
    onEndSession: () -> Unit,
    onChangeRegion: (CloudServerRegion) -> Unit,
    onChangeQuality: (StreamQualityPreset) -> Unit,
    onToggleTouchControls: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var showSettingsDrawer by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Simulated Interactive Game Stream Viewport
        val bannerRes = game.bannerImageRes ?: game.coverImageRes
        if (bannerRes != null) {
            Image(
                painter = painterResource(id = bannerRes),
                contentDescription = game.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Dark Vignette / Game Stream HUD overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xCC08090D),
                            Color(0x2208090D),
                            Color(0x4408090D),
                            Color(0xCC08090D)
                        )
                    )
                )
        )

        // Top Telemetry Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                HeonIconButton(
                    icon = Icons.Default.Close,
                    contentDescription = "Close Stream",
                    onClick = onEndSession,
                    testTag = "cloud_exit_button"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = game.title,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(HeonSuccess, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${session.state.label} • ${session.region.name}",
                            color = HeonAccentNeon,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Real-time Network Telemetry Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xDD11141F))
                    .border(1.dp, Color(0xFF242C3D), RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Wifi,
                    contentDescription = null,
                    tint = HeonSuccess,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "${session.latencyMs}ms",
                    color = HeonAccentNeon,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${session.currentFps} FPS",
                    color = Color.White,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${session.currentBitrateMbps} Mbps",
                    color = HeonTextMuted,
                    fontSize = 10.sp
                )

                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Stream Settings",
                    tint = Color.White,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { showSettingsDrawer = !showSettingsDrawer }
                )
            }
        }

        // Virtual Touch Gamepad Overlay (if enabled)
        if (session.touchControlsEnabled) {
            VirtualGamepadOverlay(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            )
        }

        // Quick Bottom Controls
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 12.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xCC0E111A))
                .border(1.dp, HeonBorderSubtle, RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.clickable(onClick = onToggleTouchControls),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Gamepad,
                    contentDescription = null,
                    tint = if (session.touchControlsEnabled) HeonAccentNeon else HeonTextMuted,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (session.touchControlsEnabled) "Touch Controls: ON" else "Gamepad: OFF",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(text = "•", color = HeonTextMuted)

            Text(
                text = "Disconnect Session",
                color = Color(0xFFF87171),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(onClick = onEndSession)
            )
        }

        // Stream Settings Drawer / Card
        AnimatedVisibility(
            visible = showSettingsDrawer,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 56.dp, end = 16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF2121520)),
                modifier = Modifier
                    .width(280.dp)
                    .border(1.dp, HeonBorderSubtle, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Stream Configuration",
                        color = HeonTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "EDGE SERVER REGION", color = HeonTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    DefaultCloudRegions.forEach { reg ->
                        val isSelected = session.region.id == reg.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) Color(0xFF1F2F11) else Color.Transparent)
                                .clickable { onChangeRegion(reg) }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = reg.name,
                                color = if (isSelected) HeonAccentNeon else HeonTextSecondary,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${reg.pingMs}ms",
                                color = if (isSelected) HeonAccentNeon else HeonTextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "QUALITY PRESET", color = HeonTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    StreamQualityPreset.entries.forEach { preset ->
                        val isSelected = session.quality == preset
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) Color(0xFF1F2F11) else Color.Transparent)
                                .clickable { onChangeQuality(preset) }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${preset.label} (${preset.resolution})",
                                color = if (isSelected) HeonAccentNeon else HeonTextSecondary,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${preset.frameRate}fps",
                                color = if (isSelected) HeonAccentNeon else HeonTextMuted,
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

/**
 * Responsive Virtual Touch Gamepad Overlay
 */
@Composable
private fun VirtualGamepadOverlay(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        // Left D-Pad
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(130.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
                    .border(1.5.dp, Color(0x66FFFFFF), CircleShape)
            )

            // Crosshair icons
            Text(text = "▲", color = Color.White, fontSize = 14.sp, modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp))
            Text(text = "▼", color = Color.White, fontSize = 14.sp, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp))
            Text(text = "◀", color = Color.White, fontSize = 14.sp, modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp))
            Text(text = "▶", color = Color.White, fontSize = 14.sp, modifier = Modifier.align(Alignment.CenterEnd).padding(end = 8.dp))

            // Center Stick Knob
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0x88B9FF39))
                    .border(1.5.dp, HeonAccentNeon, CircleShape)
            )
        }

        // Right Action Cluster (A, B, X, Y)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(130.dp)
        ) {
            // Y (Top)
            GamepadButton(label = "Y", color = Color(0xFFFBBF24), modifier = Modifier.align(Alignment.TopCenter))
            // A (Bottom)
            GamepadButton(label = "A", color = HeonAccentNeon, modifier = Modifier.align(Alignment.BottomCenter))
            // X (Left)
            GamepadButton(label = "X", color = HeonAccentBlue, modifier = Modifier.align(Alignment.CenterStart))
            // B (Right)
            GamepadButton(label = "B", color = Color(0xFFEF4444), modifier = Modifier.align(Alignment.CenterEnd))
        }
    }
}

@Composable
private fun GamepadButton(
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color(0x6611131A))
            .border(1.5.dp, color, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black
        )
    }
}
