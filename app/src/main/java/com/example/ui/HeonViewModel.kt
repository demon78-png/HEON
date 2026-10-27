package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GameCatalogRepository
import com.example.data.LocalGameCatalogRepository
import com.example.data.SortOption
import com.example.model.AuditLog
import com.example.model.AvailabilityStatus
import com.example.model.DownloadItem
import com.example.model.DownloadState
import com.example.model.FaqItem
import com.example.model.FriendPlayer
import com.example.model.FriendStatus
import com.example.model.Game
import com.example.model.GameCategory
import com.example.model.InstalledGameInfo
import com.example.model.LeaderboardEntry
import com.example.model.NotificationItem
import com.example.model.NotificationType
import com.example.model.PlatformMetrics
import com.example.model.PremiumPlan
import com.example.model.SupportTicket
import com.example.model.TicketStatus
import com.example.model.TransactionRecord
import com.example.model.TransactionStatus
import com.example.model.UserProfile
import com.example.ui.components.HeonNavDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import com.example.model.ChallengeType
import com.example.model.CloudServerRegion
import com.example.model.CloudSession
import com.example.model.CloudSessionState
import com.example.model.CosmeticReward
import com.example.model.DefaultCloudRegions
import com.example.model.NetworkStatus
import com.example.model.PlayerChallenge
import com.example.model.RewardType
import com.example.model.SeasonPassInfo
import com.example.model.StreamQualityPreset
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class SecondaryScreen {
    NONE,
    GAME_DETAIL,
    PREMIUM_PASS,
    SETTINGS,
    SUPPORT,
    ADMIN_DASHBOARD,
    SOCIAL_LEADERBOARD,
    CLOUD_SESSION,
    REWARDS
}

data class HeonUiState(
    val currentTab: HeonNavDestination = HeonNavDestination.HOME,
    val secondaryScreen: SecondaryScreen = SecondaryScreen.NONE,
    val selectedGame: Game? = null,
    val showAuthDialog: Boolean = false,
    val toastMessage: String? = null,
    // Catalog
    val featuredGames: List<Game> = emptyList(),
    val trendingGames: List<Game> = emptyList(),
    val recentlyAddedGames: List<Game> = emptyList(),
    val recommendedGames: List<Game> = emptyList(),
    val editorsPicks: List<Game> = emptyList(),
    val categories: List<GameCategory> = emptyList(),
    val allGames: List<Game> = emptyList(),
    val searchResults: List<Game> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val selectedSort: SortOption = SortOption.RECOMMENDED,
    val selectedPlatform: String? = null,
    val isLoading: Boolean = false,
    // User & Session
    val userProfile: UserProfile = UserProfile(),
    val isPremium: Boolean = false,
    val isAdminLoggedIn: Boolean = false,
    // Library & Storage
    val savedGameIds: Set<String> = setOf("game_01", "game_03"),
    val wishlistGameIds: Set<String> = setOf("game_02", "game_06"),
    val installedGameIds: Set<String> = setOf("game_01"),
    val activeDownloads: Map<String, DownloadItem> = emptyMap(),
    val installedGames: Map<String, InstalledGameInfo> = mapOf(
        "game_01" to InstalledGameInfo(
            gameId = "game_01",
            installedVersion = "2.1.0",
            installedSizeBytes = 2_400_000_000L,
            lastPlayedAt = System.currentTimeMillis() - 86400000L
        )
    ),
    // Notifications
    val notifications: List<NotificationItem> = listOf(
        NotificationItem(
            id = "notif_01",
            title = "CyberBlade v2.1 Update Available",
            message = "New ray-tracing performance profile for Android 15 & expanded chapter 4 storyline.",
            timestamp = "10m ago",
            isRead = false,
            type = NotificationType.UPDATE,
            targetGameId = "game_01"
        ),
        NotificationItem(
            id = "notif_02",
            title = "Welcome to HEON Gaming Hub",
            message = "Explore verified indie and AAA Android titles, zero intrusive ads, and cloud save sync.",
            timestamp = "2h ago",
            isRead = false,
            type = NotificationType.SYSTEM
        ),
        NotificationItem(
            id = "notif_03",
            title = "Velocity: Neo-City Drift Ranked Season",
            message = "Season 3 grand prix has kicked off with exclusive neon hovercraft chassis rewards.",
            timestamp = "1d ago",
            isRead = true,
            type = NotificationType.COMMUNITY,
            targetGameId = "game_02"
        )
    ),
    // Transactions
    val transactions: List<TransactionRecord> = listOf(
        TransactionRecord(
            id = "tx_01",
            orderId = "GPA.3392-8819-2019",
            planName = "HEON Prime Monthly Pass",
            amountDisplay = "$4.99",
            dateDisplay = "Oct 12, 2026",
            status = TransactionStatus.COMPLETED
        )
    ),
    // Support & Tickets
    val supportTickets: List<SupportTicket> = listOf(
        SupportTicket(
            id = "TK-4819",
            category = "Downloads",
            subject = "Storage allocation query on SD Card",
            description = "Can game cache packages be moved directly to adoptable external storage?",
            dateCreated = "Oct 22, 2026",
            status = TicketStatus.RESOLVED,
            staffReply = "Android Scoped Storage manages external volumes automatically when selected in Settings > Storage."
        )
    ),
    // Friends & Social
    val friendsList: List<FriendPlayer> = listOf(
        FriendPlayer("f_01", "AuraStriker", "AS", FriendStatus.ONLINE, "Velocity: Neo-City Drift"),
        FriendPlayer("f_02", "ShadowKage", "SK", FriendStatus.IN_GAME, "CyberBlade: Resurgence"),
        FriendPlayer("f_03", "PixelKnight", "PK", FriendStatus.OFFLINE)
    ),
    val leaderboard: List<LeaderboardEntry> = listOf(
        LeaderboardEntry(1, "NeonOverlord", "NO", 948200L, "948,200 pts", "Apex Grandmaster"),
        LeaderboardEntry(2, "QuantumPilot", "QP", 891400L, "891,400 pts", "Grandmaster"),
        LeaderboardEntry(3, "ValkyriePrime", "VP", 823100L, "823,100 pts", "Master Elite"),
        LeaderboardEntry(14, "CyberPilot_99", "CP", 412500L, "412,500 pts", "Diamond Veteran", isCurrentPlayer = true)
    ),
    // Admin
    val auditLogs: List<AuditLog> = listOf(
        AuditLog("log_01", "admin@heon.gg", "PUBLISH_CATALOG", "CyberBlade v2.1.0", "Today 10:14", "SUCCESS", "SHA-256 binary validation passed"),
        AuditLog("log_02", "admin@heon.gg", "FEATURED_TOGGLE", "Galactic Odyssey", "Yesterday 18:30", "SUCCESS", "Promoted to Top Hero Carousel")
    ),
    val platformMetrics: PlatformMetrics = PlatformMetrics(),
    // Cloud Gaming
    val activeCloudSession: CloudSession? = null,
    // Gamification & Challenges
    val seasonPassInfo: SeasonPassInfo = SeasonPassInfo(),
    val challenges: List<PlayerChallenge> = listOf(
        PlayerChallenge("ch_01", "Daily Login Bounty", "Log into HEON and explore 3 catalog titles", ChallengeType.DAILY, 150, 3, 3, isClaimed = false),
        PlayerChallenge("ch_02", "Cyber Blade Master", "Engage in cloud stream combat for 10 minutes", ChallengeType.DAILY, 250, 10, 6, isClaimed = false),
        PlayerChallenge("ch_03", "Speed Syndicate", "Complete 5 laps in Velocity Drift", ChallengeType.WEEKLY, 600, 5, 2, isClaimed = false),
        PlayerChallenge("ch_04", "Cosmic Voyager", "Bookmark 5 Sci-Fi titles to your personal library", ChallengeType.WEEKLY, 500, 5, 5, isClaimed = true)
    ),
    val cosmetics: List<CosmeticReward> = listOf(
        CosmeticReward("cos_01", "Neon Lime Grid", RewardType.AVATAR_FRAME, "🟩", "Epic", 5, isOwned = true, isEquipped = true),
        CosmeticReward("cos_02", "Cyberpunk Pulse", RewardType.PROFILE_GLOW, "🟣", "Legendary", 12, isOwned = true, isEquipped = false),
        CosmeticReward("cos_03", "Apex Pilot", RewardType.TITLE_BADGE, "⚡", "Rare", 8, isOwned = true, isEquipped = true),
        CosmeticReward("cos_04", "Galactic Starlight", RewardType.AVATAR_FRAME, "✨", "Legendary", 20, isOwned = false, isEquipped = false)
    ),
    val networkStatus: NetworkStatus = NetworkStatus.ONLINE_WIFI,
    val isOfflineMode: Boolean = false
)

class HeonViewModel(
    private val repository: GameCatalogRepository = LocalGameCatalogRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HeonUiState())
    val uiState: StateFlow<HeonUiState> = _uiState.asStateFlow()

    private var downloadSimulatorJob: Job? = null

    init {
        loadCatalogData()
        startDownloadSimulationLoop()
    }

    private fun loadCatalogData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val featured = repository.getFeaturedGames()
            val trending = repository.getTrendingGames()
            val recent = repository.getRecentlyAddedGames()
            val recommended = repository.getRecommendedGames()
            val editors = repository.getEditorsPicks()
            val categories = repository.getCategories()
            val initialResults = repository.searchAndFilterGames()

            _uiState.update { state ->
                state.copy(
                    featuredGames = featured,
                    trendingGames = trending,
                    recentlyAddedGames = recent,
                    recommendedGames = recommended,
                    editorsPicks = editors,
                    categories = categories,
                    allGames = initialResults,
                    searchResults = initialResults,
                    isLoading = false
                )
            }
        }
    }

    // Navigation
    fun navigateToTab(tab: HeonNavDestination) {
        _uiState.update {
            it.copy(
                currentTab = tab,
                secondaryScreen = SecondaryScreen.NONE,
                selectedGame = null
            )
        }
    }

    fun openGameDetails(game: Game) {
        _uiState.update {
            it.copy(
                selectedGame = game,
                secondaryScreen = SecondaryScreen.GAME_DETAIL
            )
        }
    }

    fun openSecondaryScreen(screen: SecondaryScreen) {
        _uiState.update { it.copy(secondaryScreen = screen) }
    }

    fun closeSecondaryScreen() {
        _uiState.update {
            it.copy(
                secondaryScreen = SecondaryScreen.NONE,
                selectedGame = if (it.secondaryScreen == SecondaryScreen.GAME_DETAIL) null else it.selectedGame
            )
        }
    }

    fun setShowAuthDialog(show: Boolean) {
        _uiState.update { it.copy(showAuthDialog = show) }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun showToast(msg: String) {
        _uiState.update { it.copy(toastMessage = msg) }
    }

    // Search and Filters
    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        executeSearchAndFilter()
    }

    fun setCategoryFilter(categoryId: String?) {
        _uiState.update { it.copy(selectedCategory = categoryId) }
        executeSearchAndFilter()
    }

    fun setSortFilter(sort: SortOption) {
        _uiState.update { it.copy(selectedSort = sort) }
        executeSearchAndFilter()
    }

    fun setPlatformFilter(platform: String?) {
        _uiState.update { it.copy(selectedPlatform = platform) }
        executeSearchAndFilter()
    }

    fun clearFilters() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                selectedCategory = null,
                selectedPlatform = null,
                selectedSort = SortOption.RECOMMENDED
            )
        }
        executeSearchAndFilter()
    }

    private fun executeSearchAndFilter() {
        viewModelScope.launch {
            val s = _uiState.value
            val results = repository.searchAndFilterGames(
                query = s.searchQuery,
                categoryId = s.selectedCategory,
                platform = s.selectedPlatform,
                sortBy = s.selectedSort
            )
            _uiState.update { it.copy(searchResults = results) }
        }
    }

    // Library & Wishlist Toggles
    fun toggleSaveToLibrary(gameId: String) {
        _uiState.update { state ->
            val isSaved = state.savedGameIds.contains(gameId)
            val updated = if (isSaved) state.savedGameIds - gameId else state.savedGameIds + gameId
            val message = if (isSaved) "Removed from My Games" else "Added to My Games"
            state.copy(savedGameIds = updated, toastMessage = message)
        }
    }

    fun toggleWishlist(gameId: String) {
        _uiState.update { state ->
            val isWishlisted = state.wishlistGameIds.contains(gameId)
            val updated = if (isWishlisted) state.wishlistGameIds - gameId else state.wishlistGameIds + gameId
            val message = if (isWishlisted) "Removed from Wishlist" else "Added to Wishlist"
            state.copy(wishlistGameIds = updated, toastMessage = message)
        }
    }

    // Download & Install Lifecycle
    fun startDownload(game: Game) {
        _uiState.update { state ->
            val downloads = state.activeDownloads.toMutableMap()
            val newDownload = DownloadItem(
                gameId = game.id,
                gameTitle = game.title,
                version = game.version,
                totalBytes = game.sizeBytes,
                downloadedBytes = 0L,
                state = DownloadState.DOWNLOADING
            )
            downloads[game.id] = newDownload
            state.copy(
                activeDownloads = downloads,
                toastMessage = "Starting download for ${game.title} (${game.sizeDisplay})"
            )
        }
    }

    fun pauseDownload(gameId: String) {
        _uiState.update { state ->
            val downloads = state.activeDownloads.toMutableMap()
            val existing = downloads[gameId]
            if (existing != null) {
                downloads[gameId] = existing.copy(state = DownloadState.PAUSED)
            }
            state.copy(activeDownloads = downloads, toastMessage = "Download paused")
        }
    }

    fun resumeDownload(gameId: String) {
        _uiState.update { state ->
            val downloads = state.activeDownloads.toMutableMap()
            val existing = downloads[gameId]
            if (existing != null) {
                downloads[gameId] = existing.copy(state = DownloadState.DOWNLOADING)
            }
            state.copy(activeDownloads = downloads, toastMessage = "Download resumed")
        }
    }

    fun cancelDownload(gameId: String) {
        _uiState.update { state ->
            val downloads = state.activeDownloads.toMutableMap()
            downloads.remove(gameId)
            state.copy(activeDownloads = downloads, toastMessage = "Download cancelled")
        }
    }

    fun installGame(gameId: String) {
        viewModelScope.launch {
            _uiState.update { state ->
                val downloads = state.activeDownloads.toMutableMap()
                val existing = downloads[gameId]
                if (existing != null) {
                    downloads[gameId] = existing.copy(state = DownloadState.INSTALLING)
                }
                state.copy(activeDownloads = downloads, toastMessage = "Verifying package & installing...")
            }
            delay(1200) // Brief validation simulation
            _uiState.update { state ->
                val downloads = state.activeDownloads.toMutableMap()
                downloads.remove(gameId)
                val installed = state.installedGameIds + gameId
                val game = state.allGames.find { it.id == gameId }
                val installedMap = state.installedGames.toMutableMap()
                installedMap[gameId] = InstalledGameInfo(
                    gameId = gameId,
                    installedVersion = game?.version ?: "1.0",
                    installedSizeBytes = game?.sizeBytes ?: 1_500_000_000L,
                    installedAt = System.currentTimeMillis()
                )
                state.copy(
                    activeDownloads = downloads,
                    installedGameIds = installed,
                    installedGames = installedMap,
                    savedGameIds = state.savedGameIds + gameId,
                    toastMessage = "${game?.title ?: "Game"} installed successfully! Ready to play."
                )
            }
        }
    }

    fun uninstallGame(gameId: String) {
        _uiState.update { state ->
            val installed = state.installedGameIds - gameId
            val map = state.installedGames.toMutableMap()
            map.remove(gameId)
            state.copy(
                installedGameIds = installed,
                installedGames = map,
                toastMessage = "Game uninstalled. Cache cleared."
            )
        }
    }

    fun launchGame(game: Game) {
        showToast("Launching ${game.title}... Optimizing memory & high-refresh display.")
    }

    // Download simulation loop (advances downloading bytes smoothly)
    private fun startDownloadSimulationLoop() {
        downloadSimulatorJob?.cancel()
        downloadSimulatorJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                _uiState.update { state ->
                    if (state.activeDownloads.isEmpty()) return@update state

                    val updatedMap = state.activeDownloads.toMutableMap()
                    var changed = false

                    for ((id, item) in state.activeDownloads) {
                        if (item.state == DownloadState.DOWNLOADING) {
                            val chunk = (item.downloadSpeedBps * 0.8 + Math.random() * item.downloadSpeedBps * 0.4).toLong()
                            val newDownloaded = (item.downloadedBytes + chunk).coerceAtMost(item.totalBytes)
                            if (newDownloaded >= item.totalBytes) {
                                // Completed download
                                updatedMap[id] = item.copy(
                                    downloadedBytes = item.totalBytes,
                                    state = DownloadState.COMPLETED
                                )
                            } else {
                                updatedMap[id] = item.copy(downloadedBytes = newDownloaded)
                            }
                            changed = true
                        }
                    }

                    if (changed) state.copy(activeDownloads = updatedMap) else state
                }
            }
        }
    }

    // User Profile & Authentication
    fun signInUser(email: String, pass: String) {
        _uiState.update { state ->
            state.copy(
                userProfile = state.userProfile.copy(
                    email = email,
                    username = email.substringBefore("@").replace(".", "_"),
                    displayName = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                    isGuest = false
                ),
                showAuthDialog = false,
                toastMessage = "Signed in as ${email.substringBefore("@")}. Cloud sync active!"
            )
        }
    }

    fun signUpUser(username: String, email: String, pass: String) {
        _uiState.update { state ->
            state.copy(
                userProfile = state.userProfile.copy(
                    username = username,
                    displayName = username,
                    email = email,
                    isGuest = false
                ),
                showAuthDialog = false,
                toastMessage = "Welcome to HEON, $username! Your profile is verified."
            )
        }
    }

    fun signOutUser() {
        _uiState.update { state ->
            state.copy(
                userProfile = UserProfile(isGuest = true, displayName = "Guest Player", username = "Guest_Player"),
                toastMessage = "Signed out. Guest mode active."
            )
        }
    }

    fun updateProfile(displayName: String, username: String) {
        _uiState.update { state ->
            state.copy(
                userProfile = state.userProfile.copy(
                    displayName = displayName.ifBlank { state.userProfile.displayName },
                    username = username.ifBlank { state.userProfile.username }
                ),
                toastMessage = "Player profile updated."
            )
        }
    }

    // Notifications
    fun markNotificationRead(id: String) {
        _uiState.update { state ->
            val updated = state.notifications.map {
                if (it.id == id) it.copy(isRead = true) else it
            }
            state.copy(notifications = updated)
        }
    }

    fun markAllNotificationsRead() {
        _uiState.update { state ->
            val updated = state.notifications.map { it.copy(isRead = true) }
            state.copy(notifications = updated, toastMessage = "All notifications marked as read.")
        }
    }

    fun dismissNotification(id: String) {
        _uiState.update { state ->
            val updated = state.notifications.filterNot { it.id == id }
            state.copy(notifications = updated)
        }
    }

    // Premium Purchase & Restoration
    fun purchasePremiumPlan(plan: PremiumPlan) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        val newTx = TransactionRecord(
            id = "tx_${System.currentTimeMillis()}",
            orderId = "GPA.${(1000..9999).random()}-${(1000..9999).random()}",
            planName = plan.name,
            amountDisplay = plan.priceDisplay,
            dateDisplay = dateFormat.format(Date()),
            status = TransactionStatus.COMPLETED
        )

        _uiState.update { state ->
            state.copy(
                isPremium = true,
                userProfile = state.userProfile.copy(membershipTier = "HEON Prime VIP"),
                transactions = listOf(newTx) + state.transactions,
                secondaryScreen = SecondaryScreen.NONE,
                toastMessage = "Welcome to HEON Prime! All premium perks unlocked."
            )
        }
    }

    fun restorePurchases() {
        _uiState.update { state ->
            state.copy(
                isPremium = true,
                userProfile = state.userProfile.copy(membershipTier = "HEON Prime VIP"),
                toastMessage = "Active Google Play subscription restored successfully."
            )
        }
    }

    // Support & Tickets
    fun submitSupportTicket(category: String, subject: String, description: String) {
        val ticketId = "TK-${(1000..9999).random()}"
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        val newTicket = SupportTicket(
            id = ticketId,
            category = category,
            subject = subject,
            description = description,
            dateCreated = dateFormat.format(Date()),
            status = TicketStatus.OPEN
        )
        _uiState.update { state ->
            state.copy(
                supportTickets = listOf(newTicket) + state.supportTickets,
                toastMessage = "Ticket #$ticketId received. Our staff will respond shortly."
            )
        }
    }

    fun submitBugReport(summary: String, steps: String) {
        showToast("Diagnostic report sent to HEON Dev Team. Thank you for making HEON better!")
    }

    // Admin Controls
    fun authenticateAdmin(passkey: String): Boolean {
        if (passkey == "7788" || passkey == "admin") {
            _uiState.update { it.copy(isAdminLoggedIn = true, toastMessage = "Admin authentication verified.") }
            return true
        }
        showToast("Invalid admin passkey. Access denied.")
        return false
    }

    fun toggleFeaturedAdmin(gameId: String) {
        _uiState.update { state ->
            val updated = state.allGames.map { g ->
                if (g.id == gameId) g.copy(isFeatured = !g.isFeatured) else g
            }
            state.copy(allGames = updated, featuredGames = updated.filter { it.isFeatured })
        }
    }

    fun publishAnnouncementAdmin(title: String, message: String) {
        val newNotif = NotificationItem(
            id = "notif_${System.currentTimeMillis()}",
            title = "[ANNOUNCEMENT] $title",
            message = message,
            timestamp = "Just now",
            isRead = false,
            type = NotificationType.SYSTEM
        )
        _uiState.update { state ->
            state.copy(
                notifications = listOf(newNotif) + state.notifications,
                toastMessage = "Platform announcement broadcast to all players."
            )
        }
    }

    // Cloud Gaming
    fun startCloudSession(game: Game) {
        val region = DefaultCloudRegions.first()
        val session = CloudSession(
            sessionId = "cs_${System.currentTimeMillis()}",
            gameId = game.id,
            gameTitle = game.title,
            region = region,
            quality = if (_uiState.value.isPremium) StreamQualityPreset.HIGH_FPS else StreamQualityPreset.BALANCED
        )
        _uiState.update {
            it.copy(
                activeCloudSession = session,
                secondaryScreen = SecondaryScreen.CLOUD_SESSION,
                toastMessage = "Connecting to ${region.name} cloud server..."
            )
        }
    }

    fun endCloudSession() {
        _uiState.update {
            it.copy(
                activeCloudSession = null,
                secondaryScreen = SecondaryScreen.NONE,
                toastMessage = "Cloud gaming stream ended."
            )
        }
    }

    fun changeCloudRegion(region: CloudServerRegion) {
        _uiState.update { state ->
            val cur = state.activeCloudSession
            if (cur != null) {
                state.copy(activeCloudSession = cur.copy(region = region, latencyMs = region.pingMs))
            } else state
        }
    }

    fun changeStreamQuality(quality: StreamQualityPreset) {
        if (quality.requiresPrime && !_uiState.value.isPremium) {
            showToast("120 FPS & 4K streaming requires HEON Prime VIP membership.")
            return
        }
        _uiState.update { state ->
            val cur = state.activeCloudSession
            if (cur != null) {
                state.copy(activeCloudSession = cur.copy(quality = quality, currentFps = quality.frameRate, currentBitrateMbps = quality.requiredBitrateMbps))
            } else state
        }
    }

    fun toggleTouchControls() {
        _uiState.update { state ->
            val cur = state.activeCloudSession
            if (cur != null) {
                state.copy(activeCloudSession = cur.copy(touchControlsEnabled = !cur.touchControlsEnabled))
            } else state
        }
    }

    // Gamification & Challenges
    fun claimChallenge(challengeId: String) {
        _uiState.update { state ->
            val ch = state.challenges.find { it.id == challengeId } ?: return@update state
            if (ch.isClaimed || !ch.isCompleted) return@update state

            val updatedChallenges = state.challenges.map {
                if (it.id == challengeId) it.copy(isClaimed = true) else it
            }
            val newXp = state.userProfile.xpProgress + (ch.xpReward / 1000f)
            val updatedUser = state.userProfile.copy(
                xpProgress = (newXp % 1f),
                level = if (newXp >= 1f) state.userProfile.level + 1 else state.userProfile.level
            )
            val newPassXp = state.seasonPassInfo.currentTierXp + ch.xpReward
            val updatedPass = state.seasonPassInfo.copy(
                currentTierXp = newPassXp % state.seasonPassInfo.maxTierXp,
                currentTier = if (newPassXp >= state.seasonPassInfo.maxTierXp) state.seasonPassInfo.currentTier + 1 else state.seasonPassInfo.currentTier
            )

            state.copy(
                challenges = updatedChallenges,
                userProfile = updatedUser,
                seasonPassInfo = updatedPass,
                toastMessage = "Claimed +${ch.xpReward} XP! Level progress updated."
            )
        }
    }

    fun equipCosmetic(cosmeticId: String) {
        _uiState.update { state ->
            val item = state.cosmetics.find { it.id == cosmeticId } ?: return@update state
            val updated = state.cosmetics.map {
                if (it.type == item.type) {
                    it.copy(isEquipped = it.id == cosmeticId)
                } else it
            }
            state.copy(cosmetics = updated, toastMessage = "Equipped ${item.name} (${item.type.label})")
        }
    }

    fun toggleOfflineMode() {
        _uiState.update { state ->
            val newOffline = !state.isOfflineMode
            state.copy(
                isOfflineMode = newOffline,
                networkStatus = if (newOffline) NetworkStatus.OFFLINE else NetworkStatus.ONLINE_WIFI,
                toastMessage = if (newOffline) "Offline Mode enabled. Serving cached game catalog." else "Online Mode restored. Cloud sync active."
            )
        }
    }
}
