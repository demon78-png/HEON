package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.HeonUiState
import com.example.ui.HeonViewModel
import com.example.ui.SecondaryScreen
import com.example.ui.components.AuthDialog
import com.example.ui.components.HeonBottomNavigation
import com.example.ui.components.HeonNavDestination
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.CloudGamingScreen
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.GameDetailScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PremiumScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RewardsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SocialScreen
import com.example.ui.screens.SupportScreen
import com.example.ui.theme.HeonAccentNeon
import com.example.ui.theme.HeonBackgroundPrimary
import com.example.ui.theme.HeonBorderSubtle
import com.example.ui.theme.HeonTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HeonTheme {
                val viewModel: HeonViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                HeonAppRoot(viewModel = viewModel, uiState = uiState)
            }
        }
    }
}

@Composable
fun HeonAppRoot(
    viewModel: HeonViewModel,
    uiState: HeonUiState
) {
    // Auto-dismiss toast after 3 seconds
    LaunchedEffect(uiState.toastMessage) {
        if (uiState.toastMessage != null) {
            delay(3200)
            viewModel.clearToast()
        }
    }

    // Hardware and gesture Back navigation handling
    BackHandler(enabled = uiState.secondaryScreen != SecondaryScreen.NONE) {
        if (uiState.secondaryScreen == SecondaryScreen.CLOUD_SESSION) {
            viewModel.endCloudSession()
        } else {
            viewModel.closeSecondaryScreen()
        }
    }

    BackHandler(enabled = uiState.secondaryScreen == SecondaryScreen.NONE && uiState.currentTab != HeonNavDestination.HOME) {
        viewModel.navigateToTab(HeonNavDestination.HOME)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HeonBackgroundPrimary)
    ) {
        Scaffold(
            bottomBar = {
                // Bottom navigation only on top-level tabs
                if (uiState.secondaryScreen == SecondaryScreen.NONE) {
                    val unreadCount = uiState.notifications.count { !it.isRead }
                    HeonBottomNavigation(
                        currentDestination = uiState.currentTab,
                        onNavigate = { viewModel.navigateToTab(it) },
                        unreadNotificationCount = unreadCount
                    )
                }
            },
            containerColor = HeonBackgroundPrimary
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = if (uiState.secondaryScreen == SecondaryScreen.NONE) innerPadding.calculateBottomPadding() else 0.dp
                    )
            ) {
                // Secondary Screens Routing (Details, Premium, Settings, Admin, Support, Social)
                when (uiState.secondaryScreen) {
                    SecondaryScreen.GAME_DETAIL -> {
                        val game = uiState.selectedGame
                        if (game != null) {
                            GameDetailScreen(
                                game = game,
                                isSaved = uiState.savedGameIds.contains(game.id),
                                isWishlisted = uiState.wishlistGameIds.contains(game.id),
                                isInstalled = uiState.installedGameIds.contains(game.id),
                                installedInfo = uiState.installedGames[game.id],
                                downloadItem = uiState.activeDownloads[game.id],
                                allGames = uiState.allGames,
                                onBack = { viewModel.closeSecondaryScreen() },
                                onToggleSave = { viewModel.toggleSaveToLibrary(game.id) },
                                onToggleWishlist = { viewModel.toggleWishlist(game.id) },
                                onStartDownload = { viewModel.startDownload(game) },
                                onPauseDownload = { viewModel.pauseDownload(game.id) },
                                onResumeDownload = { viewModel.resumeDownload(game.id) },
                                onCancelDownload = { viewModel.cancelDownload(game.id) },
                                onInstallGame = { viewModel.installGame(game.id) },
                                onUninstallGame = { viewModel.uninstallGame(game.id) },
                                onLaunchGame = { viewModel.launchGame(game) },
                                onStartCloudSession = { viewModel.startCloudSession(game) },
                                onSelectGame = { viewModel.openGameDetails(it) }
                            )
                        } else {
                            viewModel.closeSecondaryScreen()
                        }
                    }

                    SecondaryScreen.PREMIUM_PASS -> {
                        PremiumScreen(
                            isPremium = uiState.isPremium,
                            transactions = uiState.transactions,
                            onBack = { viewModel.closeSecondaryScreen() },
                            onPurchasePlan = { viewModel.purchasePremiumPlan(it) },
                            onRestorePurchases = { viewModel.restorePurchases() }
                        )
                    }

                    SecondaryScreen.ADMIN_DASHBOARD -> {
                        AdminScreen(
                            isAdminLoggedIn = uiState.isAdminLoggedIn,
                            platformMetrics = uiState.platformMetrics,
                            games = uiState.allGames,
                            auditLogs = uiState.auditLogs,
                            onBack = { viewModel.closeSecondaryScreen() },
                            onAuthenticateAdmin = { viewModel.authenticateAdmin(it) },
                            onToggleFeatured = { viewModel.toggleFeaturedAdmin(it) },
                            onPublishAnnouncement = { title, msg -> viewModel.publishAnnouncementAdmin(title, msg) }
                        )
                    }

                    SecondaryScreen.SETTINGS -> {
                        SettingsScreen(
                            onBack = { viewModel.closeSecondaryScreen() },
                            onShowToast = { viewModel.showToast(it) }
                        )
                    }

                    SecondaryScreen.SUPPORT -> {
                        SupportScreen(
                            tickets = uiState.supportTickets,
                            onBack = { viewModel.closeSecondaryScreen() },
                            onSubmitTicket = { cat, sub, desc -> viewModel.submitSupportTicket(cat, sub, desc) },
                            onSubmitBugReport = { summary, steps -> viewModel.submitBugReport(summary, steps) }
                        )
                    }

                    SecondaryScreen.SOCIAL_LEADERBOARD -> {
                        SocialScreen(
                            leaderboard = uiState.leaderboard,
                            friends = uiState.friendsList,
                            onBack = { viewModel.closeSecondaryScreen() }
                        )
                    }

                    SecondaryScreen.CLOUD_SESSION -> {
                        val session = uiState.activeCloudSession
                        val game = uiState.selectedGame ?: uiState.allGames.find { it.id == session?.gameId }
                        if (session != null && game != null) {
                            CloudGamingScreen(
                                game = game,
                                session = session,
                                onBack = { viewModel.endCloudSession() },
                                onEndSession = { viewModel.endCloudSession() },
                                onChangeRegion = { viewModel.changeCloudRegion(it) },
                                onChangeQuality = { viewModel.changeStreamQuality(it) },
                                onToggleTouchControls = { viewModel.toggleTouchControls() }
                            )
                        } else {
                            viewModel.closeSecondaryScreen()
                        }
                    }

                    SecondaryScreen.REWARDS -> {
                        RewardsScreen(
                            seasonPass = uiState.seasonPassInfo,
                            challenges = uiState.challenges,
                            cosmetics = uiState.cosmetics,
                            onBack = { viewModel.closeSecondaryScreen() },
                            onClaimChallenge = { viewModel.claimChallenge(it) },
                            onEquipCosmetic = { viewModel.equipCosmetic(it) },
                            onUpgradePass = { viewModel.openSecondaryScreen(SecondaryScreen.PREMIUM_PASS) }
                        )
                    }

                    SecondaryScreen.NONE -> {
                        // Main Bottom Navigation Tabs
                        when (uiState.currentTab) {
                            HeonNavDestination.HOME -> {
                                HomeScreen(
                                    featuredGames = uiState.featuredGames,
                                    trendingGames = uiState.trendingGames,
                                    recentlyAddedGames = uiState.recentlyAddedGames,
                                    recommendedGames = uiState.recommendedGames,
                                    editorsPicks = uiState.editorsPicks,
                                    categories = uiState.categories,
                                    savedGameIds = uiState.savedGameIds,
                                    userProfile = uiState.userProfile,
                                    onGameClick = { viewModel.openGameDetails(it) },
                                    onToggleSave = { viewModel.toggleSaveToLibrary(it.id) },
                                    onSelectCategory = { catId ->
                                        viewModel.setCategoryFilter(catId)
                                        viewModel.navigateToTab(HeonNavDestination.DISCOVER)
                                    },
                                    onOpenSearch = {
                                        viewModel.navigateToTab(HeonNavDestination.DISCOVER)
                                    },
                                    onOpenNotifications = {
                                        viewModel.navigateToTab(HeonNavDestination.NOTIFICATIONS)
                                    },
                                    onOpenProfile = {
                                        viewModel.navigateToTab(HeonNavDestination.PROFILE)
                                    },
                                    onSeeAllTrending = {
                                        viewModel.setSortFilter(com.example.data.SortOption.RECOMMENDED)
                                        viewModel.navigateToTab(HeonNavDestination.DISCOVER)
                                    },
                                    onSeeAllRecent = {
                                        viewModel.setSortFilter(com.example.data.SortOption.RECENTLY_ADDED)
                                        viewModel.navigateToTab(HeonNavDestination.DISCOVER)
                                    },
                                    isLoading = uiState.isLoading
                                )
                            }

                            HeonNavDestination.DISCOVER -> {
                                DiscoverScreen(
                                    games = uiState.searchResults,
                                    categories = uiState.categories,
                                    selectedCategory = uiState.selectedCategory,
                                    searchQuery = uiState.searchQuery,
                                    selectedSort = uiState.selectedSort,
                                    selectedPlatform = uiState.selectedPlatform,
                                    savedGameIds = uiState.savedGameIds,
                                    onQueryChange = { viewModel.setSearchQuery(it) },
                                    onCategoryChange = { viewModel.setCategoryFilter(it) },
                                    onSortChange = { viewModel.setSortFilter(it) },
                                    onPlatformChange = { viewModel.setPlatformFilter(it) },
                                    onClearFilters = { viewModel.clearFilters() },
                                    onGameClick = { viewModel.openGameDetails(it) },
                                    onToggleSave = { viewModel.toggleSaveToLibrary(it.id) },
                                    isLoading = uiState.isLoading
                                )
                            }

                            HeonNavDestination.LIBRARY -> {
                                LibraryScreen(
                                    allGames = uiState.allGames,
                                    savedGameIds = uiState.savedGameIds,
                                    wishlistGameIds = uiState.wishlistGameIds,
                                    installedGameIds = uiState.installedGameIds,
                                    installedGames = uiState.installedGames,
                                    activeDownloads = uiState.activeDownloads,
                                    onGameClick = { viewModel.openGameDetails(it) },
                                    onToggleSave = { viewModel.toggleSaveToLibrary(it.id) },
                                    onToggleWishlist = { viewModel.toggleWishlist(it.id) },
                                    onPauseDownload = { viewModel.pauseDownload(it) },
                                    onResumeDownload = { viewModel.resumeDownload(it) },
                                    onCancelDownload = { viewModel.cancelDownload(it) },
                                    onInstallGame = { viewModel.installGame(it) },
                                    onUninstallGame = { viewModel.uninstallGame(it) },
                                    onLaunchGame = { viewModel.launchGame(it) },
                                    onNavigateDiscover = { viewModel.navigateToTab(HeonNavDestination.DISCOVER) }
                                )
                            }

                            HeonNavDestination.NOTIFICATIONS -> {
                                NotificationsScreen(
                                    notifications = uiState.notifications,
                                    allGames = uiState.allGames,
                                    onMarkRead = { viewModel.markNotificationRead(it) },
                                    onMarkAllRead = { viewModel.markAllNotificationsRead() },
                                    onDismiss = { viewModel.dismissNotification(it) },
                                    onSelectGame = { viewModel.openGameDetails(it) }
                                )
                            }

                            HeonNavDestination.PROFILE -> {
                                ProfileScreen(
                                    userProfile = uiState.userProfile,
                                    isPremium = uiState.isPremium,
                                    onOpenAuth = { viewModel.setShowAuthDialog(true) },
                                    onOpenPremium = { viewModel.openSecondaryScreen(SecondaryScreen.PREMIUM_PASS) },
                                    onOpenRewards = { viewModel.openSecondaryScreen(SecondaryScreen.REWARDS) },
                                    onOpenSettings = { viewModel.openSecondaryScreen(SecondaryScreen.SETTINGS) },
                                    onOpenSupport = { viewModel.openSecondaryScreen(SecondaryScreen.SUPPORT) },
                                    onOpenAdmin = { viewModel.openSecondaryScreen(SecondaryScreen.ADMIN_DASHBOARD) },
                                    onOpenSocial = { viewModel.openSecondaryScreen(SecondaryScreen.SOCIAL_LEADERBOARD) },
                                    onSignOut = { viewModel.signOutUser() },
                                    onUpdateProfile = { name, tag -> viewModel.updateProfile(name, tag) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Floating Toast Banner
        AnimatedVisibility(
            visible = uiState.toastMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 16.dp, start = 20.dp, end = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xF0182210))
                    .border(1.dp, HeonAccentNeon, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = uiState.toastMessage ?: "",
                    color = HeonAccentNeon,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Authentication Modal Dialog
        if (uiState.showAuthDialog) {
            AuthDialog(
                onDismiss = { viewModel.setShowAuthDialog(false) },
                onSignIn = { email, pass -> viewModel.signInUser(email, pass) },
                onSignUp = { user, email, pass -> viewModel.signUpUser(user, email, pass) },
                onForgotPassword = { email -> viewModel.showToast("Password reset link dispatched to $email") }
            )
        }
    }
}
