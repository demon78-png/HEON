package com.example.model

enum class ChallengeType(val label: String) {
    DAILY("Daily Bounty"),
    WEEKLY("Weekly Mission"),
    SEASONAL("Season Horizon")
}

data class PlayerChallenge(
    val id: String,
    val title: String,
    val description: String,
    val type: ChallengeType,
    val xpReward: Int,
    val targetCount: Int,
    val currentProgress: Int,
    val isClaimed: Boolean = false,
    val expiresIn: String = "18h remaining"
) {
    val isCompleted: Boolean
        get() = currentProgress >= targetCount

    val progressFloat: Float
        get() = if (targetCount > 0) (currentProgress.toFloat() / targetCount.toFloat()).coerceIn(0f, 1f) else 0f
}

enum class RewardType(val label: String) {
    AVATAR_FRAME("Avatar Frame"),
    PROFILE_GLOW("Neon Profile Glow"),
    TITLE_BADGE("Gamer Title"),
    OST_ALBUM("Original Soundtrack")
}

data class CosmeticReward(
    val id: String,
    val name: String,
    val type: RewardType,
    val iconEmoji: String,
    val rarity: String = "Epic",
    val requiredLevel: Int = 10,
    val isOwned: Boolean = false,
    val isEquipped: Boolean = false
)

data class SeasonPassInfo(
    val seasonNumber: Int = 3,
    val seasonName: String = "Cyber Horizon",
    val daysRemaining: Int = 24,
    val currentTier: Int = 18,
    val maxTier: Int = 50,
    val currentTierXp: Int = 450,
    val maxTierXp: Int = 1000,
    val isPremiumUnlocked: Boolean = false
)
