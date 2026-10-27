package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SortOption
import com.example.model.Game
import com.example.model.GameCategory
import com.example.ui.components.EmptyStateView
import com.example.ui.components.PortraitGameCard
import com.example.ui.theme.HeonAccentNeon
import com.example.ui.theme.HeonBackgroundPrimary
import com.example.ui.theme.HeonBackgroundSecondary
import com.example.ui.theme.HeonBorderSubtle
import com.example.ui.theme.HeonCardElevated
import com.example.ui.theme.HeonTextMuted
import com.example.ui.theme.HeonTextPrimary
import com.example.ui.theme.HeonTextSecondary

@Composable
fun DiscoverScreen(
    games: List<Game>,
    categories: List<GameCategory>,
    selectedCategory: String?,
    searchQuery: String,
    selectedSort: SortOption,
    selectedPlatform: String?,
    savedGameIds: Set<String>,
    onQueryChange: (String) -> Unit,
    onCategoryChange: (String?) -> Unit,
    onSortChange: (SortOption) -> Unit,
    onPlatformChange: (String?) -> Unit,
    onClearFilters: () -> Unit,
    onGameClick: (Game) -> Unit,
    onToggleSave: (Game) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    var showSortMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HeonBackgroundPrimary)
            .statusBarsPadding()
    ) {
        // Page Title & Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Discover Games",
                color = HeonTextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Explore the complete HEON catalog, curated genres, and releases",
                color = HeonTextSecondary,
                fontSize = 12.sp
            )
        }

        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                placeholder = {
                    Text(
                        text = "Search by title, genre, developer...",
                        color = HeonTextMuted,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = HeonAccentNeon,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                tint = HeonTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = HeonCardElevated,
                    unfocusedContainerColor = HeonCardElevated,
                    focusedBorderColor = HeonAccentNeon,
                    unfocusedBorderColor = HeonBorderSubtle,
                    focusedTextColor = HeonTextPrimary,
                    unfocusedTextColor = HeonTextPrimary
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("discover_search_input")
            )
        }

        // Category Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                DiscoverFilterChip(
                    text = "All Genres",
                    isSelected = selectedCategory == null || selectedCategory == "all",
                    onClick = { onCategoryChange(null) }
                )
            }
            items(categories, key = { it.id }) { cat ->
                DiscoverFilterChip(
                    text = "${cat.icon} ${cat.name}",
                    isSelected = selectedCategory.equals(cat.id, ignoreCase = true) ||
                        selectedCategory.equals(cat.name, ignoreCase = true),
                    onClick = {
                        if (selectedCategory.equals(cat.id, ignoreCase = true)) {
                            onCategoryChange(null)
                        } else {
                            onCategoryChange(cat.id)
                        }
                    }
                )
            }
        }

        // Sub-filters row: Platform + Sort dropdown + Result Count
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Result count
            Text(
                text = "${games.size} ${if (games.size == 1) "game" else "games"} found",
                color = HeonTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Platform Chip Toggles
                listOf("All", "Android", "Cloud").forEach { platform ->
                    val isPlatformSelected = (selectedPlatform == null && platform == "All") ||
                        selectedPlatform.equals(platform, ignoreCase = true)
                    Text(
                        text = platform,
                        color = if (isPlatformSelected) HeonAccentNeon else HeonTextMuted,
                        fontSize = 11.sp,
                        fontWeight = if (isPlatformSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                onPlatformChange(if (platform == "All") null else platform)
                            }
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Sort Dropdown Button
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(HeonCardElevated)
                            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(8.dp))
                            .clickable { showSortMenu = true }
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                            .testTag("sort_menu_trigger"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort",
                            tint = HeonAccentNeon,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = selectedSort.displayName,
                            color = HeonTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false },
                        modifier = Modifier
                            .background(HeonBackgroundSecondary)
                            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(12.dp))
                    ) {
                        SortOption.entries.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = option.displayName,
                                        color = if (option == selectedSort) HeonAccentNeon else HeonTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = if (option == selectedSort) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    onSortChange(option)
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Active filter indicator / reset option
        val hasActiveFilter = searchQuery.isNotBlank() || selectedCategory != null || selectedPlatform != null
        if (hasActiveFilter) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filtering active",
                    color = HeonAccentNeon,
                    fontSize = 11.sp
                )
                Text(
                    text = "Clear all filters",
                    color = HeonTextMuted,
                    fontSize = 11.sp,
                    modifier = Modifier
                        .clickable(onClick = onClearFilters)
                        .padding(4.dp)
                        .testTag("clear_all_filters")
                )
            }
        }

        // Games Grid (2-column on phone, responsive)
        if (games.isEmpty()) {
            EmptyStateView(
                title = "No games matched your search",
                message = "Try searching for a different keyword or clear your category/platform filters.",
                actionButtonText = "Reset Filters",
                onActionClick = onClearFilters,
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(games, key = { it.id }) { game ->
                    PortraitGameCard(
                        game = game,
                        isSaved = savedGameIds.contains(game.id),
                        onGameClick = onGameClick,
                        onToggleSave = onToggleSave
                    )
                }
            }
        }
    }
}

@Composable
private fun DiscoverFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0xFF263D08) else HeonCardElevated)
            .border(
                width = 1.dp,
                color = if (isSelected) HeonAccentNeon else HeonBorderSubtle,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) HeonAccentNeon else HeonTextSecondary,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
