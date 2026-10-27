package com.example.data

import com.example.R
import com.example.model.AvailabilityStatus
import com.example.model.DefaultCategories
import com.example.model.DownloadOption
import com.example.model.Game
import com.example.model.GameCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class SortOption(val displayName: String) {
    RECOMMENDED("Recommended"),
    RECENTLY_ADDED("Recently Added"),
    RATING_HIGH("Highest Rated"),
    TITLE_AZ("Title: A to Z"),
    TITLE_ZA("Title: Z to A"),
    SIZE_LOW("Size: Small to Large")
}

interface GameCatalogRepository {
    suspend fun getFeaturedGames(): List<Game>
    suspend fun getTrendingGames(): List<Game>
    suspend fun getRecentlyAddedGames(): List<Game>
    suspend fun getRecommendedGames(): List<Game>
    suspend fun getEditorsPicks(): List<Game>
    suspend fun getGamesByCategory(categoryId: String): List<Game>
    suspend fun searchAndFilterGames(
        query: String = "",
        categoryId: String? = null,
        platform: String? = null,
        sortBy: SortOption = SortOption.RECOMMENDED
    ): List<Game>
    suspend fun getGameById(id: String): Game?
    suspend fun getCategories(): List<GameCategory>
    fun getBookmarkedGameIds(): StateFlow<Set<String>>
    fun getInstalledGameIds(): StateFlow<Set<String>>
    suspend fun toggleBookmark(gameId: String): Boolean
    suspend fun setInstallStatus(gameId: String, isInstalled: Boolean)
}

class LocalGameCatalogRepository : GameCatalogRepository {

    private val _bookmarkedGameIds = MutableStateFlow<Set<String>>(setOf("game_01", "game_03"))
    override fun getBookmarkedGameIds(): StateFlow<Set<String>> = _bookmarkedGameIds.asStateFlow()

    private val _installedGameIds = MutableStateFlow<Set<String>>(setOf("game_01"))
    override fun getInstalledGameIds(): StateFlow<Set<String>> = _installedGameIds.asStateFlow()

    private val gamesList: List<Game> = listOf(
        Game(
            id = "game_01",
            title = "CyberBlade: Resurgence",
            slug = "cyberblade-resurgence",
            tagline = "Neon Katana Combat in Neo-Tokyo",
            description = "Step into the augmentations of Ren, a rogue cyber-operative hunting down rogue AI cartels across the rain-drenched rooftops of Neo-Tokyo. Features fluid acrobatic combat, deep cybernetic build crafting, and cutting-edge ray-traced visuals engineered for high-performance mobile devices.",
            bannerImageRes = R.drawable.hero_banner_aetheria_1793051197472,
            coverImageRes = R.drawable.game_cover_syndicate_1793051334001,
            genres = listOf("Action", "RPG", "Sci-Fi"),
            platforms = listOf("Android", "PC", "Cloud"),
            developer = "Aether Dynamics",
            publisher = "HEON Studios",
            releaseDate = "October 2026",
            catalogAddedAt = "2026-10-20",
            rating = 4.9,
            reviewCount = 14200,
            isFeatured = true,
            isEditorsPick = true,
            isTrending = true,
            isRecentlyAdded = false,
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            officialWebsite = "https://cyberblade.gg",
            sizeDisplay = "2.4 GB",
            version = "2.1.0",
            ageRating = "16+",
            downloadOptions = listOf(
                DownloadOption("Full High-Res APK", "Direct Android Package", "2.4 GB", "v2.1.0", "https://cdn.heon.gg/pkg/cyberblade-v210.apk"),
                DownloadOption("Cloud Stream Client", "Low Latency Stream", "45 MB", "v2.1.0", "https://stream.heon.gg/cyberblade")
            )
        ),
        Game(
            id = "game_02",
            title = "Velocity: Neo-City Drift",
            slug = "velocity-neo-city-drift",
            tagline = "Anti-Gravity Street Racing with Holographic Tracks",
            description = "Burn anti-matter fuel across dynamic levitating tracks perched above cloud-piercing megastructures. Tune hybrid hypercars with energy shields, EMP boosts, and responsive gyroscope steering designed exclusively for mobile touch and gamepad play.",
            bannerImageRes = R.drawable.hero_banner_drift_1793051209994,
            coverImageRes = R.drawable.hero_banner_drift_1793051209994,
            genres = listOf("Racing", "Sci-Fi", "Multiplayer"),
            platforms = listOf("Android", "Cloud"),
            developer = "Quantum Overdrive",
            publisher = "Veloce Interactive",
            releaseDate = "September 2026",
            catalogAddedAt = "2026-09-28",
            rating = 4.8,
            reviewCount = 9850,
            isFeatured = true,
            isEditorsPick = false,
            isTrending = true,
            isRecentlyAdded = true,
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            officialWebsite = "https://velocitydrift.com",
            sizeDisplay = "1.6 GB",
            version = "1.4.2",
            ageRating = "3+",
            downloadOptions = listOf(
                DownloadOption("Optimized APK", "Direct APK", "1.6 GB", "v1.4.2", "https://cdn.heon.gg/pkg/velocity-142.apk")
            )
        ),
        Game(
            id = "game_03",
            title = "Galactic Odyssey: Nexus Gate",
            slug = "galactic-odyssey-nexus-gate",
            tagline = "Endless Interstellar Quantum Exploration",
            description = "Chart uncharted star systems beyond the ancient warp gate. Pilot modular starships, mine asteroid belts, establish planetary research outposts, and encounter mysterious cosmic entities across a procedurally infinite galaxy.",
            bannerImageRes = R.drawable.hero_banner_chrono_1793051334442,
            coverImageRes = R.drawable.hero_banner_chrono_1793051334442,
            genres = listOf("Adventure", "Sci-Fi", "RPG"),
            platforms = listOf("Android", "PC"),
            developer = "Singularity Lab",
            publisher = "Starlight Games",
            releaseDate = "August 2026",
            catalogAddedAt = "2026-08-15",
            rating = 4.7,
            reviewCount = 6720,
            isFeatured = true,
            isEditorsPick = true,
            isTrending = false,
            isRecentlyAdded = false,
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            officialWebsite = "https://galacticodyssey.space",
            sizeDisplay = "3.1 GB",
            version = "1.0.8",
            ageRating = "12+",
            downloadOptions = listOf(
                DownloadOption("Expedition APK + Cache", "Installer Bundle", "3.1 GB", "v1.0.8", "https://cdn.heon.gg/pkg/odyssey-full.apk")
            )
        ),
        Game(
            id = "game_04",
            title = "Shadow Unit-7: Neon Void",
            slug = "shadow-unit-7-neon-void",
            tagline = "Tactical Cyberpunk Infiltration and Breach",
            description = "A tactical top-down and third-person stealth shooter. Infiltrate corporate arcologies in Neo-Kyoto using acoustic cloaking, micro-drones, and thermal imaging. Plan your insertion point and exfiltrate undetected.",
            bannerImageRes = R.drawable.game_cover_syndicate_1793051334001,
            coverImageRes = R.drawable.game_cover_syndicate_1793051334001,
            genres = listOf("Action", "Strategy", "Multiplayer"),
            platforms = listOf("Android", "Console", "PC"),
            developer = "DarkFrame Studios",
            publisher = "Hexagon Publishing",
            releaseDate = "October 2026",
            catalogAddedAt = "2026-10-18",
            rating = 4.85,
            reviewCount = 11200,
            isFeatured = false,
            isEditorsPick = true,
            isTrending = true,
            isRecentlyAdded = true,
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            officialWebsite = "https://shadowunit7.com",
            sizeDisplay = "1.9 GB",
            version = "1.1.2",
            ageRating = "18+",
            downloadOptions = listOf(
                DownloadOption("Standard Tactical APK", "Package", "1.9 GB", "v1.1.2", "https://cdn.heon.gg/pkg/shadowunit7.apk")
            )
        ),
        Game(
            id = "game_05",
            title = "Aetheria: Chrono Weaver",
            slug = "aetheria-chrono-weaver",
            tagline = "Time-bending Fantasy Tactical Deckbuilder",
            description = "Rewind turns, branch timelines, and summon mythic guardians in a strategic tactical card battler set in an enchanted clockwork kingdom.",
            bannerImageRes = R.drawable.hero_banner_aetheria_1793051197472,
            coverImageRes = R.drawable.hero_banner_aetheria_1793051197472,
            genres = listOf("Strategy", "RPG", "Indie"),
            platforms = listOf("Android"),
            developer = "Mythic Loom",
            publisher = "IndieCabal",
            releaseDate = "July 2026",
            catalogAddedAt = "2026-07-10",
            rating = 4.6,
            reviewCount = 3890,
            isFeatured = false,
            isEditorsPick = false,
            isTrending = false,
            isRecentlyAdded = false,
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            sizeDisplay = "850 MB",
            version = "1.0.3",
            ageRating = "7+"
        ),
        Game(
            id = "game_06",
            title = "HyperForge: Titan Protocol",
            slug = "hyperforge-titan-protocol",
            tagline = "Assemble Gigantic Mechs and Battle Colossi",
            description = "Design, assemble, and pilot 60-meter bipedal combat mechs. Customize ballistic cannons, plasma repeaters, and heavy thrusters for 4v4 arena warfare.",
            bannerImageRes = R.drawable.hero_banner_drift_1793051209994,
            coverImageRes = R.drawable.hero_banner_drift_1793051209994,
            genres = listOf("Action", "Multiplayer", "Sci-Fi"),
            platforms = listOf("Android", "PC"),
            developer = "Heavy Iron Interactive",
            publisher = "HEON Studios",
            releaseDate = "November 2026",
            catalogAddedAt = "2026-10-25",
            rating = 4.75,
            reviewCount = 5400,
            isFeatured = false,
            isEditorsPick = false,
            isTrending = true,
            isRecentlyAdded = true,
            availabilityStatus = AvailabilityStatus.EARLY_ACCESS,
            sizeDisplay = "2.8 GB",
            version = "0.9.4",
            ageRating = "12+"
        ),
        Game(
            id = "game_07",
            title = "Voxel Raiders: Dungeon Crawl",
            slug = "voxel-raiders-dungeon-crawl",
            tagline = "Retro Rogue-lite with Destructible Physics",
            description = "Delve into cursed labyrinths with vibrant voxel physics. Mine terrain, collapse tunnels on skeleton hordes, and forge enchanted artifacts.",
            bannerImageRes = R.drawable.hero_banner_chrono_1793051334442,
            coverImageRes = R.drawable.hero_banner_chrono_1793051334442,
            genres = listOf("Adventure", "Indie", "Casual"),
            platforms = listOf("Android"),
            developer = "PixelForge",
            publisher = "PixelForge",
            releaseDate = "May 2026",
            catalogAddedAt = "2026-05-14",
            rating = 4.65,
            reviewCount = 8200,
            isFeatured = false,
            isEditorsPick = false,
            isTrending = false,
            isRecentlyAdded = false,
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            sizeDisplay = "420 MB",
            version = "1.5.0",
            ageRating = "3+"
        ),
        Game(
            id = "game_08",
            title = "Apex Apex: Speed Horizon",
            slug = "apex-speed-horizon",
            tagline = "Hyper-realistic Gran Turismo Mobile Simulator",
            description = "Experience precision physics and laser-scanned circuits across the globe. Features 120Hz high frame rate support for flagship mobile chipsets.",
            bannerImageRes = R.drawable.hero_banner_drift_1793051209994,
            coverImageRes = R.drawable.hero_banner_drift_1793051209994,
            genres = listOf("Racing", "Simulation"),
            platforms = listOf("Android", "Console"),
            developer = "Apex Sim Studio",
            publisher = "Motorsport Global",
            releaseDate = "September 2026",
            catalogAddedAt = "2026-09-12",
            rating = 4.9,
            reviewCount = 18400,
            isFeatured = false,
            isEditorsPick = true,
            isTrending = true,
            isRecentlyAdded = false,
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            sizeDisplay = "3.6 GB",
            version = "2.0.1",
            ageRating = "3+"
        )
    )

    override suspend fun getFeaturedGames(): List<Game> {
        return gamesList.filter { it.isFeatured }
    }

    override suspend fun getTrendingGames(): List<Game> {
        return gamesList.filter { it.isTrending }
    }

    override suspend fun getRecentlyAddedGames(): List<Game> {
        return gamesList.filter { it.isRecentlyAdded }
    }

    override suspend fun getRecommendedGames(): List<Game> {
        return gamesList.sortedByDescending { it.rating }
    }

    override suspend fun getEditorsPicks(): List<Game> {
        return gamesList.filter { it.isEditorsPick }
    }

    override suspend fun getGamesByCategory(categoryId: String): List<Game> {
        val targetGenre = DefaultCategories.find { it.id.equals(categoryId, ignoreCase = true) }?.name ?: categoryId
        return gamesList.filter { game ->
            game.genres.any { it.equals(targetGenre, ignoreCase = true) }
        }
    }

    override suspend fun searchAndFilterGames(
        query: String,
        categoryId: String?,
        platform: String?,
        sortBy: SortOption
    ): List<Game> {
        val cleanQuery = query.trim().lowercase()

        var results = gamesList.filter { game ->
            val matchesQuery = cleanQuery.isEmpty() ||
                game.title.lowercase().contains(cleanQuery) ||
                game.description.lowercase().contains(cleanQuery) ||
                game.developer.lowercase().contains(cleanQuery) ||
                game.publisher.lowercase().contains(cleanQuery) ||
                game.genres.any { it.lowercase().contains(cleanQuery) }

            val matchesCategory = if (categoryId.isNullOrBlank() || categoryId == "all") {
                true
            } else {
                val catName = DefaultCategories.find { it.id.equals(categoryId, ignoreCase = true) }?.name ?: categoryId
                game.genres.any { it.equals(catName, ignoreCase = true) }
            }

            val matchesPlatform = if (platform.isNullOrBlank() || platform == "all") {
                true
            } else {
                game.platforms.any { it.equals(platform, ignoreCase = true) }
            }

            matchesQuery && matchesCategory && matchesPlatform
        }

        results = when (sortBy) {
            SortOption.RECOMMENDED -> results.sortedByDescending { it.rating * 1000 + it.reviewCount }
            SortOption.RECENTLY_ADDED -> results.sortedByDescending { it.catalogAddedAt }
            SortOption.RATING_HIGH -> results.sortedByDescending { it.rating }
            SortOption.TITLE_AZ -> results.sortedBy { it.title.lowercase() }
            SortOption.TITLE_ZA -> results.sortedByDescending { it.title.lowercase() }
            SortOption.SIZE_LOW -> results.sortedBy { it.sizeDisplay }
        }

        return results
    }

    override suspend fun getGameById(id: String): Game? {
        return gamesList.find { it.id == id }
    }

    override suspend fun getCategories(): List<GameCategory> {
        return DefaultCategories
    }

    override suspend fun toggleBookmark(gameId: String): Boolean {
        var isBookmarked = false
        _bookmarkedGameIds.update { current ->
            if (current.contains(gameId)) {
                isBookmarked = false
                current - gameId
            } else {
                isBookmarked = true
                current + gameId
            }
        }
        return isBookmarked
    }

    override suspend fun setInstallStatus(gameId: String, isInstalled: Boolean) {
        _installedGameIds.update { current ->
            if (isInstalled) current + gameId else current - gameId
        }
    }
}
