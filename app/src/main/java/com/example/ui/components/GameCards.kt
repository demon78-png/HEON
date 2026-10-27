package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Game
import com.example.ui.theme.HeonAccentBlue
import com.example.ui.theme.HeonAccentNeon
import com.example.ui.theme.HeonAccentPurple
import com.example.ui.theme.HeonBorderSubtle
import com.example.ui.theme.HeonCardElevated
import com.example.ui.theme.HeonTextMuted
import com.example.ui.theme.HeonTextPrimary
import com.example.ui.theme.HeonTextSecondary

/**
 * 2.2 Featured Game Hero Card
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HeroFeaturedCard(
    game: Game,
    isSaved: Boolean,
    onGameClick: (Game) -> Unit,
    onToggleSave: (Game) -> Unit,
    modifier: Modifier = Modifier,
    carouselIndex: Int = 0,
    totalCount: Int = 1,
    onSelectIndex: ((Int) -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = HeonAccentNeon),
                onClick = { onGameClick(game) }
            )
            .testTag("featured_hero_${game.id}")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        ) {
            // Artwork
            if (game.bannerImageRes != null) {
                Image(
                    painter = painterResource(id = game.bannerImageRes),
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
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF1E2436), Color(0xFF0F121C))
                            )
                        )
                )
            }

            // Cinematic Gradient Overlay (dark at bottom and subtle at top)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x3308090D),
                                Color(0x7708090D),
                                Color(0xFA08090D)
                            )
                        )
                    )
            )

            // Top Badges
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xCC090D02))
                            .border(1.dp, HeonAccentNeon, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "FEATURED TITLE",
                            color = HeonAccentNeon,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    StatusBadge(status = game.availabilityStatus)
                }

                // Bookmark / Save Action
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xCC11131A))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = HeonAccentNeon),
                            onClick = { onToggleSave(game) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save Game",
                        tint = if (isSaved) HeonAccentNeon else HeonTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Bottom Information Overlay
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = game.title,
                        color = HeonTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    RatingPill(rating = game.rating, reviewCount = game.reviewCount)
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = game.tagline,
                    color = HeonTextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        game.genres.take(3).forEach { genre ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0x55282E40))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = genre,
                                    color = HeonTextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    HeonPrimaryButton(
                        text = "View Game",
                        onClick = { onGameClick(game) },
                        icon = Icons.Default.Visibility,
                        testTag = "hero_view_game_button"
                    )
                }
            }

            // Carousel Dots if multiple
            if (totalCount > 1 && onSelectIndex != null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (i in 0 until totalCount) {
                        Box(
                            modifier = Modifier
                                .size(if (i == carouselIndex) 16.dp else 6.dp, 6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (i == carouselIndex) HeonAccentNeon else Color(0x66FFFFFF))
                                .clickable { onSelectIndex(i) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Standard Landscape Card (Used for Trending, Recently Added, Recommendations)
 */
@Composable
fun LandscapeGameCard(
    game: Game,
    isSaved: Boolean,
    onGameClick: (Game) -> Unit,
    onToggleSave: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
        modifier = modifier
            .width(240.dp)
            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = HeonAccentNeon),
                onClick = { onGameClick(game) }
            )
            .testTag("game_card_${game.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                if (game.bannerImageRes != null) {
                    Image(
                        painter = painterResource(id = game.bannerImageRes),
                        contentDescription = game.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF1E2230))
                    )
                }

                // Bookmark Icon
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xCC11131A))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = HeonAccentNeon),
                            onClick = { onToggleSave(game) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save Game",
                        tint = if (isSaved) HeonAccentNeon else HeonTextPrimary,
                        modifier = Modifier.size(17.dp)
                    )
                }

                // Bottom gradient for card image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color(0xCC191C26))
                            )
                        )
                )

                // Rating pill
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    RatingPill(rating = game.rating)
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = game.title,
                    color = HeonTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = game.genres.firstOrNull() ?: "Game",
                        color = HeonAccentNeon,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = game.sizeDisplay,
                        color = HeonTextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

/**
 * Portrait Cover Card (Used for Discover Grid)
 */
@Composable
fun PortraitGameCard(
    game: Game,
    isSaved: Boolean,
    onGameClick: (Game) -> Unit,
    onToggleSave: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = HeonAccentNeon),
                onClick = { onGameClick(game) }
            )
            .testTag("portrait_card_${game.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.85f)
            ) {
                val imageRes = game.coverImageRes ?: game.bannerImageRes
                if (imageRes != null) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = game.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF1E2230))
                    )
                }

                // Bookmark button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color(0xCC11131A))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = HeonAccentNeon),
                            onClick = { onToggleSave(game) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save Game",
                        tint = if (isSaved) HeonAccentNeon else HeonTextPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Rating overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                ) {
                    RatingPill(rating = game.rating)
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = game.title,
                    color = HeonTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = game.genres.firstOrNull() ?: "Gaming",
                        color = HeonAccentNeon,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = game.platforms.firstOrNull() ?: "Android",
                        color = HeonTextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

/**
 * Compact Game Row for Recent History and Search Results
 */
@Composable
fun CompactGameRow(
    game: Game,
    isSaved: Boolean,
    onGameClick: (Game) -> Unit,
    onToggleSave: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(HeonCardElevated)
            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = HeonAccentNeon),
                onClick = { onGameClick(game) }
            )
            .padding(10.dp)
            .testTag("compact_game_${game.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageRes = game.coverImageRes ?: game.bannerImageRes
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = game.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(modifier = Modifier.fillMaxSize().background(Color(0xFF22283A)))
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
            Text(
                text = "${game.developer} • ${game.genres.take(2).joinToString(", ")}",
                color = HeonTextSecondary,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RatingPill(rating = game.rating)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = game.sizeDisplay,
                    color = HeonTextMuted,
                    fontSize = 10.sp
                )
            }
        }

        IconButton(
            onClick = { onToggleSave(game) },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = "Save",
                tint = if (isSaved) HeonAccentNeon else HeonTextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
