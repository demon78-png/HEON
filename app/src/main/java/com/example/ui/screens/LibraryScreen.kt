package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DownloadItem
import com.example.model.DownloadState
import com.example.model.Game
import com.example.model.InstalledGameInfo
import com.example.ui.components.CompactGameRow
import com.example.ui.components.EmptyStateView
import com.example.ui.components.HeonPrimaryButton
import com.example.ui.components.HeonSecondaryButton
import com.example.ui.components.RatingPill
import com.example.ui.theme.HeonAccentBlue
import com.example.ui.theme.HeonAccentNeon
import com.example.ui.theme.HeonBackgroundPrimary
import com.example.ui.theme.HeonBorderSubtle
import com.example.ui.theme.HeonCardElevated
import com.example.ui.theme.HeonSuccess
import com.example.ui.theme.HeonTextMuted
import com.example.ui.theme.HeonTextPrimary
import com.example.ui.theme.HeonTextSecondary

@Composable
fun LibraryScreen(
    allGames: List<Game>,
    savedGameIds: Set<String>,
    wishlistGameIds: Set<String>,
    installedGameIds: Set<String>,
    installedGames: Map<String, InstalledGameInfo>,
    activeDownloads: Map<String, DownloadItem>,
    onGameClick: (Game) -> Unit,
    onToggleSave: (Game) -> Unit,
    onToggleWishlist: (Game) -> Unit,
    onPauseDownload: (String) -> Unit,
    onResumeDownload: (String) -> Unit,
    onCancelDownload: (String) -> Unit,
    onInstallGame: (String) -> Unit,
    onUninstallGame: (String) -> Unit,
    onLaunchGame: (Game) -> Unit,
    onNavigateDiscover: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("My Games") }
    var filterQuery by remember { mutableStateOf("") }

    val myGames = allGames.filter { savedGameIds.contains(it.id) || installedGameIds.contains(it.id) }
    val wishlistGames = allGames.filter { wishlistGameIds.contains(it.id) }
    val installedGamesList = allGames.filter { installedGameIds.contains(it.id) }
    val downloadsList = activeDownloads.values.toList()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HeonBackgroundPrimary)
            .statusBarsPadding()
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Player Library",
                color = HeonTextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Manage your game collection, active download queues, and local installs",
                color = HeonTextSecondary,
                fontSize = 12.sp
            )
        }

        // Storage Analyzer Bar
        val gamesStorageBytes = installedGames.values.sumOf { it.installedSizeBytes }
        val systemAndOtherBytes = 28L * 1024L * 1024L * 1024L // 28 GB OS & other apps
        val totalCapacityBytes = 128L * 1024L * 1024L * 1024L // 128 GB
        val totalUsedBytes = systemAndOtherBytes + gamesStorageBytes
        val storageProgress = (totalUsedBytes.toFloat() / totalCapacityBytes.toFloat()).coerceIn(0.1f, 1f)
        val gamesFormatted = DownloadItem.formatBytes(gamesStorageBytes)
        val totalUsedFormatted = DownloadItem.formatBytes(totalUsedBytes)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF131722))
                .border(1.dp, HeonBorderSubtle, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            tint = HeonAccentNeon,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Storage ($gamesFormatted in games)",
                            color = HeonTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "$totalUsedFormatted / 128 GB Used",
                        color = HeonAccentNeon,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { storageProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = HeonAccentNeon,
                    trackColor = Color(0xFF242B3D)
                )
            }
        }

        // Search Bar in Library
        OutlinedTextField(
            value = filterQuery,
            onValueChange = { filterQuery = it },
            placeholder = { Text("Filter your collection...", color = HeonTextMuted, fontSize = 12.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = HeonTextMuted,
                    modifier = Modifier.size(18.dp)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = HeonCardElevated,
                unfocusedContainerColor = HeonCardElevated,
                focusedBorderColor = HeonAccentNeon,
                unfocusedBorderColor = HeonBorderSubtle,
                focusedTextColor = HeonTextPrimary,
                unfocusedTextColor = HeonTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .testTag("library_filter_input")
        )

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(HeonCardElevated)
                .padding(4.dp)
        ) {
            listOf(
                "My Games (${myGames.size})" to "My Games",
                "Wishlist (${wishlistGames.size})" to "Wishlist",
                "Installed (${installedGamesList.size})" to "Installed",
                "Downloads (${downloadsList.size})" to "Downloads"
            ).forEach { (label, key) ->
                val isSelected = selectedTab == key
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color(0xFF263D08) else Color.Transparent)
                        .clickable { selectedTab = key }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) HeonAccentNeon else HeonTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Tab Content
        when (selectedTab) {
            "My Games" -> {
                val filtered = myGames.filter {
                    filterQuery.isBlank() || it.title.contains(filterQuery, ignoreCase = true)
                }
                if (filtered.isEmpty()) {
                    EmptyStateView(
                        title = "Your game library is empty",
                        message = "Explore the HEON discovery catalog and bookmark your favorite titles.",
                        actionButtonText = "Discover Games",
                        onActionClick = onNavigateDiscover,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filtered, key = { it.id }) { game ->
                            CompactGameRow(
                                game = game,
                                isSaved = true,
                                onGameClick = onGameClick,
                                onToggleSave = onToggleSave
                            )
                        }
                    }
                }
            }

            "Wishlist" -> {
                val filtered = wishlistGames.filter {
                    filterQuery.isBlank() || it.title.contains(filterQuery, ignoreCase = true)
                }
                if (filtered.isEmpty()) {
                    EmptyStateView(
                        title = "No games on your wishlist",
                        message = "Save games you're anticipating to receive notification updates when released or on sale.",
                        actionButtonText = "Browse Discover",
                        onActionClick = onNavigateDiscover,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filtered, key = { it.id }) { game ->
                            CompactGameRow(
                                game = game,
                                isSaved = savedGameIds.contains(game.id),
                                onGameClick = onGameClick,
                                onToggleSave = onToggleWishlist
                            )
                        }
                    }
                }
            }

            "Installed" -> {
                val filtered = installedGamesList.filter {
                    filterQuery.isBlank() || it.title.contains(filterQuery, ignoreCase = true)
                }
                if (filtered.isEmpty()) {
                    EmptyStateView(
                        title = "No games currently installed",
                        message = "Download any available title from the catalog to launch directly.",
                        actionButtonText = "Find Games to Install",
                        onActionClick = onNavigateDiscover,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filtered, key = { it.id }) { game ->
                            InstalledGameCard(
                                game = game,
                                info = installedGames[game.id],
                                onLaunch = { onLaunchGame(game) },
                                onUninstall = { onUninstallGame(game.id) },
                                onClick = { onGameClick(game) }
                            )
                        }
                    }
                }
            }

            "Downloads" -> {
                if (downloadsList.isEmpty()) {
                    EmptyStateView(
                        title = "No active downloads in queue",
                        message = "When you download game packages or updates, their live progress and verification will show here.",
                        actionButtonText = "Explore Catalog",
                        onActionClick = onNavigateDiscover,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(downloadsList, key = { it.gameId }) { item ->
                            DownloadQueueCard(
                                item = item,
                                onPause = { onPauseDownload(item.gameId) },
                                onResume = { onResumeDownload(item.gameId) },
                                onCancel = { onCancelDownload(item.gameId) },
                                onInstall = { onInstallGame(item.gameId) },
                                onClick = {
                                    val g = allGames.find { it.id == item.gameId }
                                    if (g != null) onGameClick(g)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InstalledGameCard(
    game: Game,
    info: InstalledGameInfo?,
    onLaunch: () -> Unit,
    onUninstall: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val img = game.coverImageRes ?: game.bannerImageRes
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                if (img != null) {
                    Image(
                        painter = painterResource(id = img),
                        contentDescription = game.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = game.title,
                    color = HeonTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(HeonSuccess, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "v${info?.installedVersion ?: game.version} • ${game.sizeDisplay}",
                        color = HeonTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                HeonPrimaryButton(
                    text = "Play",
                    onClick = onLaunch,
                    icon = Icons.Default.PlayArrow,
                    modifier = Modifier.height(38.dp)
                )

                IconButton(onClick = onUninstall) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Uninstall",
                        tint = HeonTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DownloadQueueCard(
    item: DownloadItem,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onCancel: () -> Unit,
    onInstall: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.gameTitle,
                        color = HeonTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = "${item.state.label} • v${item.version}",
                        color = if (item.state == DownloadState.COMPLETED) HeonSuccess else HeonAccentNeon,
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = "${(item.progressFloat * 100).toInt()}%",
                    color = HeonAccentNeon,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { item.progressFloat },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (item.state == DownloadState.COMPLETED) HeonSuccess else HeonAccentNeon,
                trackColor = Color(0xFF262B3D)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${item.formattedDownloadedSize} / ${item.formattedTotalSize}",
                    color = HeonTextSecondary,
                    fontSize = 11.sp
                )
                Text(
                    text = if (item.state == DownloadState.DOWNLOADING) item.formattedSpeed else "Idle",
                    color = HeonTextMuted,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.state == DownloadState.COMPLETED) {
                    HeonPrimaryButton(
                        text = "Install APK",
                        onClick = onInstall,
                        icon = Icons.Default.CloudDownload,
                        modifier = Modifier.height(38.dp)
                    )
                } else {
                    if (item.state == DownloadState.DOWNLOADING) {
                        IconButton(onClick = onPause) {
                            Icon(Icons.Default.Pause, contentDescription = "Pause", tint = HeonTextPrimary)
                        }
                    } else if (item.state == DownloadState.PAUSED) {
                        IconButton(onClick = onResume) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Resume", tint = HeonAccentNeon)
                        }
                    }
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.Cancel, contentDescription = "Cancel", tint = HeonTextMuted)
                    }
                }
            }
        }
    }
}
