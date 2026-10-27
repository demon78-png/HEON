package com.example.model

data class Achievement(
    val id: String,
    val gameId: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val xpValue: Int,
    val isUnlocked: Boolean = false,
    val unlockedDate: String? = null
)

data class LeaderboardEntry(
    val rank: Int,
    val username: String,
    val avatarTag: String,
    val score: Long,
    val scoreDisplay: String,
    val tierBadge: String,
    val isCurrentPlayer: Boolean = false
)

enum class FriendStatus(val label: String) {
    ONLINE("Online"),
    IN_GAME("In Game"),
    OFFLINE("Offline")
}

data class FriendPlayer(
    val id: String,
    val username: String,
    val avatarTag: String,
    val status: FriendStatus,
    val currentGame: String? = null,
    val mutualGamesCount: Int = 3
)
