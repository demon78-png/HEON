package com.example.model

enum class PublicationStatus(val label: String) {
    DRAFT("Draft"),
    UNDER_REVIEW("Under Review"),
    PUBLISHED("Live in Catalog"),
    ARCHIVED("Archived"),
    REJECTED("Rejected")
}

data class GameVersionHistory(
    val version: String,
    val releaseDate: String,
    val changeLog: String,
    val artifactSizeDisplay: String,
    val checksumSha256: String,
    val isLive: Boolean = true
)

enum class NetworkStatus(val label: String) {
    ONLINE_WIFI("Online (High-Speed Wi-Fi)"),
    ONLINE_CELLULAR("Online (Cellular Data)"),
    OFFLINE("Offline Mode (Local Cache Active)")
}

data class SyncQueueOperation(
    val id: String,
    val actionType: String, // e.g. "TOGGLE_WISHLIST", "SAVE_PLAY_TIME", "SUBMIT_RATING"
    val targetId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)
