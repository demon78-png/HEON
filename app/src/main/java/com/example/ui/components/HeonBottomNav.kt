package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HeonAccentNeon
import com.example.ui.theme.HeonBackgroundSecondary
import com.example.ui.theme.HeonBorderSubtle
import com.example.ui.theme.HeonTextMuted
import com.example.ui.theme.HeonTextPrimary

enum class HeonNavDestination(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("home", "Home", Icons.Filled.Home, Icons.Outlined.Home),
    DISCOVER("discover", "Discover", Icons.Filled.Explore, Icons.Outlined.Explore),
    LIBRARY("library", "Library", Icons.Filled.Bookmarks, Icons.Outlined.Bookmarks),
    NOTIFICATIONS("notifications", "Alerts", Icons.Filled.Notifications, Icons.Outlined.Notifications),
    PROFILE("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun HeonBottomNavigation(
    currentDestination: HeonNavDestination,
    onNavigate: (HeonNavDestination) -> Unit,
    modifier: Modifier = Modifier,
    unreadNotificationCount: Int = 2
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(HeonBackgroundSecondary)
            .border(width = 1.dp, color = HeonBorderSubtle, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeonNavDestination.entries.forEach { destination ->
                val isSelected = destination == currentDestination
                val icon = if (isSelected) destination.selectedIcon else destination.unselectedIcon
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) HeonAccentNeon else HeonTextMuted,
                    label = "navColor"
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = HeonAccentNeon, bounded = true),
                            onClick = { onNavigate(destination) }
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .testTag("nav_item_${destination.route}")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        // Active pill indicator behind icon
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(width = 44.dp, height = 26.dp)
                                    .clip(RoundedCornerShape(13.dp))
                                    .background(Color(0xFF233608))
                            )
                        }

                        Icon(
                            imageVector = icon,
                            contentDescription = destination.title,
                            tint = contentColor,
                            modifier = Modifier.size(22.dp)
                        )

                        if (destination == HeonNavDestination.NOTIFICATIONS && unreadNotificationCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(HeonAccentNeon, CircleShape)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = destination.title,
                        color = contentColor,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}
