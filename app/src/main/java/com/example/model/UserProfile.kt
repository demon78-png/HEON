package com.example.model

data class UserProfile(
    val id: String = "guest_001",
    val username: String = "CyberPilot_99",
    val displayName: String = "Neon Rider",
    val email: String = "pilot@heon.gg",
    val isGuest: Boolean = true,
    val isAdmin: Boolean = false,
    val membershipTier: String = "HEON Explorer",
    val savedGamesCount: Int = 3,
    val downloadsCount: Int = 1,
    val hoursPlayedTotal: Int = 42,
    val level: Int = 14,
    val xpProgress: Float = 0.68f
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.SYSTEM,
    val targetGameId: String? = null
)

enum class NotificationType {
    SYSTEM,
    UPDATE,
    DISCOUNT,
    COMMUNITY
}
