package com.example.model

enum class AvailabilityStatus(val label: String) {
    AVAILABLE("Available"),
    EARLY_ACCESS("Early Access"),
    COMING_SOON("Coming Soon"),
    MAINTENANCE("In Maintenance")
}

enum class CompatibilityStatus(val label: String, val isPlayable: Boolean) {
    VERIFIED_COMPATIBLE("Verified for this device", true),
    PLAYABLE_WITH_OPTIMIZATION("Playable (Performance Settings Advised)", true),
    INSUFFICIENT_STORAGE("Insufficient Storage", false),
    UNSUPPORTED_CHIPSET("Incompatible Hardware", false)
}

data class SystemRequirements(
    val minAndroidVersion: String = "Android 10.0 (API 29)",
    val recommendedRam: String = "6 GB RAM",
    val storageRequired: String = "2.8 GB free space",
    val chipsetSupport: String = "Snapdragon 700+, Dimensity 800+, Tensor",
    val gamepadSupport: Boolean = true,
    val refreshRateHz: Int = 120
)

data class PlayerReview(
    val reviewerName: String,
    val avatarTag: String,
    val rating: Double,
    val date: String,
    val comment: String,
    val hoursPlayed: Int,
    val isVerifiedPurchase: Boolean = true
)

data class DownloadOption(
    val title: String,
    val type: String, // e.g. "APK (Arm64)", "Direct Cloud", "Google Play Link"
    val sizeDisplay: String,
    val version: String,
    val url: String,
    val checksumSha256: String = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
)

data class Game(
    val id: String,
    val title: String,
    val slug: String,
    val tagline: String,
    val description: String,
    val coverImageRes: Int? = null,
    val bannerImageRes: Int? = null,
    val coverImageUrl: String = "",
    val bannerImageUrl: String = "",
    val genres: List<String> = emptyList(),
    val platforms: List<String> = listOf("Android"),
    val developer: String,
    val publisher: String,
    val releaseDate: String,
    val catalogAddedAt: String,
    val rating: Double = 4.5,
    val reviewCount: Int = 1200,
    val isFeatured: Boolean = false,
    val isEditorsPick: Boolean = false,
    val isTrending: Boolean = false,
    val isRecentlyAdded: Boolean = false,
    val availabilityStatus: AvailabilityStatus = AvailabilityStatus.AVAILABLE,
    val officialWebsite: String = "https://heon.gg",
    val officialStoreUrl: String = "",
    val sizeDisplay: String = "1.8 GB",
    val sizeBytes: Long = 1_932_735_283L,
    val version: String = "1.0.4",
    val ageRating: String = "16+",
    val downloadOptions: List<DownloadOption> = emptyList(),
    val sampleVideoOrTrailerLabel: String = "Official Launch Gameplay",
    val keyFeatures: List<String> = listOf(
        "120 FPS High Frame Rate on supported flagships",
        "Full Bluetooth & USB-C Gamepad Support",
        "Cloud Save synchronization across devices",
        "Zero intrusive ads or predatory microtransactions"
    ),
    val systemRequirements: SystemRequirements = SystemRequirements(),
    val compatibility: CompatibilityStatus = CompatibilityStatus.VERIFIED_COMPATIBLE,
    val sampleReviews: List<PlayerReview> = listOf(
        PlayerReview(
            reviewerName = "ValkyriePrime",
            avatarTag = "VP",
            rating = 5.0,
            date = "Yesterday",
            comment = "Astonishing visual polish on mobile. Combat fluidity rivals high-end PC action titles.",
            hoursPlayed = 38
        ),
        PlayerReview(
            reviewerName = "GlitchRunner",
            avatarTag = "GR",
            rating = 4.8,
            date = "3 days ago",
            comment = "Gamepad mapping is instant and responsive. Solid 90fps locked on my device.",
            hoursPlayed = 21
        )
    )
)
