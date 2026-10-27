package com.example.model

data class GameCategory(
    val id: String,
    val name: String,
    val icon: String, // Emoji or icon identifier
    val description: String,
    val gameCount: Int = 0
)

val DefaultCategories = listOf(
    GameCategory("action", "Action", "⚡", "High-octane reflexes and intense combat", 18),
    GameCategory("rpg", "RPG", "🗡️", "Rich storytelling and character progression", 24),
    GameCategory("racing", "Racing", "🏎️", "Hyper-speed tracks and vehicle tuning", 12),
    GameCategory("strategy", "Strategy", "♟️", "Tactical mastery and base building", 14),
    GameCategory("adventure", "Adventure", "🧭", "Atmospheric worlds and immersive quests", 16),
    GameCategory("sci-fi", "Sci-Fi", "🚀", "Futuristic cybernetics and deep space", 20),
    GameCategory("multiplayer", "Multiplayer", "👥", "Competitive lobbies and co-op raids", 22),
    GameCategory("indie", "Indie", "💎", "Creative breakthroughs and unique art styles", 15),
    GameCategory("casual", "Casual", "🎯", "Quick fun sessions and tactile gameplay", 10)
)
