package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.model.Game
import com.example.model.GameCategory
import com.example.model.UserProfile
import com.example.ui.components.CategoryChip
import com.example.ui.components.CompactGameRow
import com.example.ui.components.HeonIconButton
import com.example.ui.components.HeonWordmark
import com.example.ui.components.HeroFeaturedCard
import com.example.ui.components.LandscapeGameCard
import com.example.ui.components.PortraitGameCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.SkeletonCard
import com.example.ui.theme.HeonAccentNeon
import com.example.ui.theme.HeonAccentPurple
import com.example.ui.theme.HeonBackgroundPrimary
import com.example.ui.theme.HeonBorderSubtle
import com.example.ui.theme.HeonCardElevated
import com.example.ui.theme.HeonTextMuted
import com.example.ui.theme.HeonTextPrimary
import com.example.ui.theme.HeonTextSecondary
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    featuredGames: List<Game>,
    trendingGames: List<Game>,
    recentlyAddedGames: List<Game>,
    recommendedGames: List<Game>,
    editorsPicks: List<Game>,
    categories: List<GameCategory>,
    savedGameIds: Set<String>,
    userProfile: UserProfile,
    onGameClick: (Game) -> Unit,
    onToggleSave: (Game) -> Unit,
    onSelectCategory: (String) -> Unit,
    onOpenSearch: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenProfile: () -> Unit,
    onSeeAllTrending: () -> Unit,
    onSeeAllRecent: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var heroCarouselIndex by remember { mutableIntStateOf(0) }

    val safeFeatured = featuredGames.ifEmpty { trendingGames }
    val currentFeatured = if (safeFeatured.isNotEmpty()) {
        safeFeatured[heroCarouselIndex % safeFeatured.size]
    } else null

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HeonBackgroundPrimary)
    ) {
        // 2.1 Top Header
        HomeTopHeader(
            userProfile = userProfile,
            onLogoClick = {
                coroutineScope.launch { listState.animateScrollToItem(0) }
            },
            onSearchClick = onOpenSearch,
            onNotificationsClick = onOpenNotifications,
            onProfileClick = onOpenProfile
        )

        if (isLoading) {
            Column(modifier = Modifier.padding(16.dp)) {
                SkeletonCard(height = 200.dp)
                Spacer(modifier = Modifier.height(16.dp))
                SkeletonCard(height = 120.dp)
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // 2.2 Featured Game Hero
                if (currentFeatured != null) {
                    item(key = "hero_featured") {
                        HeroFeaturedCard(
                            game = currentFeatured,
                            isSaved = savedGameIds.contains(currentFeatured.id),
                            onGameClick = onGameClick,
                            onToggleSave = onToggleSave,
                            carouselIndex = heroCarouselIndex % safeFeatured.size,
                            totalCount = safeFeatured.size,
                            onSelectIndex = { heroCarouselIndex = it }
                        )
                    }
                }

                // 3.5 Browse by Category (Horizontal quick chips)
                item(key = "categories_row") {
                    Spacer(modifier = Modifier.height(12.dp))
                    SectionHeader(
                        title = "Browse by Category",
                        subtitle = "Find your next playstyle"
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories, key = { it.id }) { cat ->
                            CategoryChip(
                                name = cat.name,
                                icon = cat.icon,
                                isSelected = false,
                                onClick = { onSelectCategory(cat.id) }
                            )
                        }
                    }
                }

                // 3.2 Trending Games
                if (trendingGames.isNotEmpty()) {
                    item(key = "trending_section") {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader(
                            title = "Trending Now",
                            subtitle = "Most active titles in the community",
                            seeAllAction = onSeeAllTrending
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(trendingGames, key = { it.id }) { game ->
                                LandscapeGameCard(
                                    game = game,
                                    isSaved = savedGameIds.contains(game.id),
                                    onGameClick = onGameClick,
                                    onToggleSave = onToggleSave
                                )
                            }
                        }
                    }
                }

                // 3.6 Editor's Picks (Curated titles)
                if (editorsPicks.isNotEmpty()) {
                    item(key = "editors_picks_section") {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader(
                            title = "Editor's Picks",
                            subtitle = "Handcrafted recommendations from HEON staff"
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(editorsPicks, key = { it.id }) { game ->
                                PortraitGameCard(
                                    game = game,
                                    isSaved = savedGameIds.contains(game.id),
                                    onGameClick = onGameClick,
                                    onToggleSave = onToggleSave,
                                    modifier = Modifier.width(160.dp)
                                )
                            }
                        }
                    }
                }

                // 3.3 Recently Added
                if (recentlyAddedGames.isNotEmpty()) {
                    item(key = "recent_added_section") {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader(
                            title = "Recently Added",
                            subtitle = "Fresh arrivals to the catalog",
                            seeAllAction = onSeeAllRecent
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(recentlyAddedGames, key = { it.id }) { game ->
                                LandscapeGameCard(
                                    game = game,
                                    isSaved = savedGameIds.contains(game.id),
                                    onGameClick = onGameClick,
                                    onToggleSave = onToggleSave
                                )
                            }
                        }
                    }
                }

                // 3.4 Recommended for You (Compact vertical list)
                if (recommendedGames.isNotEmpty()) {
                    item(key = "recommended_header") {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader(
                            title = "Recommended For You",
                            subtitle = "Based on top ratings and player consensus"
                        )
                    }

                    items(recommendedGames.take(3), key = { "rec_${it.id}" }) { game ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)) {
                            CompactGameRow(
                                game = game,
                                isSaved = savedGameIds.contains(game.id),
                                onGameClick = onGameClick,
                                onToggleSave = onToggleSave
                            )
                        }
                    }
                }

                // Demo catalog disclaimer footer
                item(key = "footer_notice") {
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF10131C))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(HeonAccentNeon, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "HEON CORE SYSTEM v1.0",
                                    color = HeonTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Running demonstration catalog with sample titles and cloud data services architecture.",
                                color = HeonTextMuted,
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
private fun HomeTopHeader(
    userProfile: UserProfile,
    onLogoClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onLogoClick)
                .testTag("home_logo_header")
        ) {
            HeonWordmark(fontSize = 20f)
            Text(
                text = "Ready for your next adventure?",
                color = HeonTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeonIconButton(
                icon = Icons.Default.Search,
                contentDescription = "Search Catalog",
                onClick = onSearchClick,
                testTag = "header_search_button"
            )

            HeonIconButton(
                icon = Icons.Default.Notifications,
                contentDescription = "Notifications",
                onClick = onNotificationsClick,
                badgeCount = 2,
                testTag = "header_notifications_button"
            )

            HeonIconButton(
                icon = Icons.Default.Person,
                contentDescription = "Player Profile",
                onClick = onProfileClick,
                tint = HeonAccentNeon,
                testTag = "header_profile_button"
            )
        }
    }
}
