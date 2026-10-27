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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.DownloadItem
import com.example.model.DownloadState
import com.example.model.Game
import com.example.model.InstalledGameInfo
import com.example.ui.components.HeonIconButton
import com.example.ui.components.HeonPrimaryButton
import com.example.ui.components.HeonSecondaryButton
import com.example.ui.components.RatingPill
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameDetailScreen(
    game: Game,
    isSaved: Boolean,
    isWishlisted: Boolean,
    isInstalled: Boolean,
    installedInfo: InstalledGameInfo?,
    downloadItem: DownloadItem?,
    allGames: List<Game>,
    onBack: () -> Unit,
    onToggleSave: () -> Unit,
    onToggleWishlist: () -> Unit,
    onStartDownload: () -> Unit,
    onPauseDownload: () -> Unit,
    onResumeDownload: () -> Unit,
    onCancelDownload: () -> Unit,
    onInstallGame: () -> Unit,
    onUninstallGame: () -> Unit,
    onLaunchGame: () -> Unit,
    onStartCloudSession: () -> Unit,
    onSelectGame: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var selectedTab by remember { mutableStateOf("Overview") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HeonBackgroundPrimary)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Hero Banner Image with Back & Action Overlays
            item(key = "banner_header") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    val bannerRes = game.bannerImageRes ?: game.coverImageRes
                    if (bannerRes != null) {
                        Image(
                            painter = painterResource(id = bannerRes),
                            contentDescription = game.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (game.bannerImageUrl.isNotBlank()) {
                        AsyncImage(
                            model = game.bannerImageUrl,
                            contentDescription = game.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF141926))
                        )
                    }

                    // Gradients
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0x9908090D),
                                        Color(0x2208090D),
                                        Color(0xEE08090D),
                                        HeonBackgroundPrimary
                                    )
                                )
                            )
                    )

                    // Top Bar controls
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HeonIconButton(
                            icon = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            onClick = onBack,
                            testTag = "detail_back_button"
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            HeonIconButton(
                                icon = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Wishlist",
                                onClick = onToggleWishlist,
                                tint = if (isWishlisted) Color(0xFFF43F5E) else HeonTextPrimary,
                                testTag = "detail_wishlist_button"
                            )
                            HeonIconButton(
                                icon = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Save to Library",
                                onClick = onToggleSave,
                                tint = if (isSaved) HeonAccentNeon else HeonTextPrimary,
                                testTag = "detail_library_button"
                            )
                        }
                    }

                    // Badge and title preview at bottom of hero
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StatusBadge(status = game.availabilityStatus)
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF1E2436))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = game.ageRating,
                                    color = HeonTextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = game.title,
                            color = HeonTextPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp
                        )
                    }
                }
            }

            // Developer, publisher, rating & specs bar
            item(key = "game_meta_bar") {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "${game.developer} • Published by ${game.publisher}",
                        color = HeonTextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Meta cards row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetaMetricBox(
                            label = "RATING",
                            value = "${game.rating} ★",
                            subtext = "${game.reviewCount} reviews",
                            valueColor = HeonRatingStar
                        )
                        MetaMetricBox(
                            label = "SIZE",
                            value = game.sizeDisplay,
                            subtext = "Full High-Res APK",
                            valueColor = HeonTextPrimary
                        )
                        MetaMetricBox(
                            label = "VERSION",
                            value = "v${game.version}",
                            subtext = game.releaseDate,
                            valueColor = HeonAccentNeon
                        )
                        MetaMetricBox(
                            label = "HARDWARE",
                            value = "120 FPS",
                            subtext = "Gamepad Ready",
                            valueColor = HeonAccentBlue
                        )
                    }
                }
            }

            // Primary Interactive Download / Launch / Install Panel
            item(key = "action_panel") {
                Spacer(modifier = Modifier.height(16.dp))
                ActionControlPanel(
                    game = game,
                    isInstalled = isInstalled,
                    installedInfo = installedInfo,
                    downloadItem = downloadItem,
                    onStartDownload = onStartDownload,
                    onPauseDownload = onPauseDownload,
                    onResumeDownload = onResumeDownload,
                    onCancelDownload = onCancelDownload,
                    onInstallGame = onInstallGame,
                    onUninstallGame = onUninstallGame,
                    onLaunchGame = onLaunchGame,
                    onStartCloudSession = onStartCloudSession
                )
            }

            // Tab switchers (Overview, Requirements, Reviews)
            item(key = "tabs_row") {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(HeonCardElevated)
                        .padding(4.dp)
                ) {
                    listOf("Overview", "System Specs", "Reviews (${game.reviewCount})").forEach { tab ->
                        val isSelected = selectedTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFF263D08) else Color.Transparent)
                                .clickable { selectedTab = tab }
                                .padding(vertical = 10.dp),
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
            }

            // Tab Content
            when (selectedTab) {
                "Overview" -> {
                    item(key = "tab_overview") {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "About This Game",
                                color = HeonTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = game.description,
                                color = HeonTextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Key Features",
                                color = HeonTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            game.keyFeatures.forEach { feature ->
                                Row(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(HeonAccentNeon, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = feature,
                                        color = HeonTextPrimary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Genres & Tags",
                                color = HeonTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                game.genres.forEach { genre ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(HeonCardElevated)
                                            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = genre,
                                            color = HeonAccentNeon,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                "System Specs" -> {
                    item(key = "tab_specs") {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Hardware & OS Compatibility",
                                color = HeonTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF162B12))
                                    .border(1.dp, Color(0xFF2E6325), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = HeonSuccess,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = game.compatibility.label,
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Optimized for high-performance touch and Bluetooth gamepad input.",
                                            color = Color(0xFFB5E2AE),
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            SpecRow(label = "Minimum OS", value = game.systemRequirements.minAndroidVersion)
                            SpecRow(label = "Recommended RAM", value = game.systemRequirements.recommendedRam)
                            SpecRow(label = "Free Storage Needed", value = game.systemRequirements.storageRequired)
                            SpecRow(label = "Chipset Architectures", value = game.systemRequirements.chipsetSupport)
                            SpecRow(label = "Gamepad Controller", value = if (game.systemRequirements.gamepadSupport) "Supported (XInput / HID)" else "Touch Only")
                            SpecRow(label = "Max Display Refresh", value = "${game.systemRequirements.refreshRateHz} Hz High-FPS Mode")
                        }
                    }
                }

                else -> {
                    item(key = "tab_reviews") {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Community Consensus",
                                color = HeonTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            game.sampleReviews.forEach { review ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .border(1.dp, HeonBorderSubtle, RoundedCornerShape(12.dp))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(28.dp)
                                                        .clip(CircleShape)
                                                        .background(HeonAccentPurple),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = review.avatarTag,
                                                        color = Color.White,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = review.reviewerName,
                                                    color = HeonTextPrimary,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            RatingPill(rating = review.rating)
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = review.comment,
                                            color = HeonTextSecondary,
                                            fontSize = 12.sp,
                                            lineHeight = 18.sp
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "${review.hoursPlayed} hrs on record • Verified Player",
                                            color = HeonTextMuted,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Related Games
            item(key = "related_games_header") {
                SectionHeader(
                    title = "Similar Titles",
                    subtitle = "Other games in ${game.genres.firstOrNull() ?: "the catalog"}"
                )
            }

            item(key = "related_games_row") {
                val related = allGames.filter { it.id != game.id }.take(4)
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(related, key = { "rel_${it.id}" }) { otherGame ->
                        Box(
                            modifier = Modifier
                                .width(140.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(HeonCardElevated)
                                .border(1.dp, HeonBorderSubtle, RoundedCornerShape(12.dp))
                                .clickable { onSelectGame(otherGame) }
                                .padding(8.dp)
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                ) {
                                    val img = otherGame.bannerImageRes ?: otherGame.coverImageRes
                                    if (img != null) {
                                        Image(
                                            painter = painterResource(id = img),
                                            contentDescription = otherGame.title,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = otherGame.title,
                                    color = HeonTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${otherGame.rating} ★",
                                    color = HeonRatingStar,
                                    fontSize = 10.sp,
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

@Composable
private fun ActionControlPanel(
    game: Game,
    isInstalled: Boolean,
    installedInfo: InstalledGameInfo?,
    downloadItem: DownloadItem?,
    onStartDownload: () -> Unit,
    onPauseDownload: () -> Unit,
    onResumeDownload: () -> Unit,
    onCancelDownload: () -> Unit,
    onInstallGame: () -> Unit,
    onUninstallGame: () -> Unit,
    onLaunchGame: () -> Unit,
    onStartCloudSession: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            when {
                // Currently downloading or paused
                downloadItem != null && (downloadItem.state == DownloadState.DOWNLOADING || downloadItem.state == DownloadState.PAUSED) -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (downloadItem.state == DownloadState.PAUSED) "Download Paused" else "Downloading Package...",
                            color = HeonTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${(downloadItem.progressFloat * 100).toInt()}%",
                            color = HeonAccentNeon,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { downloadItem.progressFloat },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = HeonAccentNeon,
                        trackColor = Color(0xFF282D3D)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${downloadItem.formattedDownloadedSize} / ${downloadItem.formattedTotalSize}",
                            color = HeonTextSecondary,
                            fontSize = 11.sp
                        )
                        Text(
                            text = if (downloadItem.state == DownloadState.DOWNLOADING) downloadItem.formattedSpeed else "0 KB/s",
                            color = HeonTextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (downloadItem.state == DownloadState.DOWNLOADING) {
                            HeonSecondaryButton(
                                text = "Pause",
                                onClick = onPauseDownload,
                                icon = Icons.Default.Pause,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            HeonPrimaryButton(
                                text = "Resume",
                                onClick = onResumeDownload,
                                icon = Icons.Default.PlayArrow,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        HeonSecondaryButton(
                            text = "Cancel",
                            onClick = onCancelDownload,
                            icon = Icons.Default.Cancel,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Download completed, ready to install
                downloadItem != null && downloadItem.state == DownloadState.COMPLETED -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = HeonSuccess,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Download Complete • SHA-256 Verified",
                            color = HeonTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ready to install securely via Android Package Installer.",
                        color = HeonTextSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HeonPrimaryButton(
                        text = "Install Game (APK)",
                        onClick = onInstallGame,
                        icon = Icons.Default.CloudDownload,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Installing
                downloadItem != null && downloadItem.state == DownloadState.INSTALLING -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = HeonAccentNeon,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Installing Game Package...",
                            color = HeonTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = HeonAccentNeon,
                        trackColor = Color(0xFF282D3D)
                    )
                }

                // Installed & Ready to Launch
                isInstalled -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(HeonSuccess, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Installed (v${installedInfo?.installedVersion ?: game.version})",
                                color = HeonTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = onUninstallGame,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Uninstall",
                                tint = HeonTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HeonPrimaryButton(
                        text = "Launch Game",
                        onClick = onLaunchGame,
                        icon = Icons.Default.PlayArrow,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "launch_game_primary_button"
                    )
                }

                // Not installed, not downloading
                else -> {
                    Text(
                        text = "Official Authorized Distribution",
                        color = HeonTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Full Android game package signed by developer. Verified virus-free.",
                        color = HeonTextSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HeonPrimaryButton(
                            text = "Download (${game.sizeDisplay})",
                            onClick = onStartDownload,
                            icon = Icons.Default.Download,
                            modifier = Modifier.weight(1.3f),
                            testTag = "start_download_button"
                        )
                        HeonSecondaryButton(
                            text = "Cloud Stream",
                            onClick = onStartCloudSession,
                            icon = Icons.Default.PlayArrow,
                            modifier = Modifier.weight(1f),
                            testTag = "cloud_stream_button"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetaMetricBox(
    label: String,
    value: String,
    subtext: String,
    valueColor: Color
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(HeonCardElevated)
            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = HeonTextMuted,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            color = valueColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = subtext,
            color = HeonTextSecondary,
            fontSize = 9.sp
        )
    }
}

@Composable
private fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = HeonTextMuted,
            fontSize = 12.sp
        )
        Text(
            text = value,
            color = HeonTextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
    HorizontalDivider(color = Color(0xFF1E2230), thickness = 0.8.dp)
}
