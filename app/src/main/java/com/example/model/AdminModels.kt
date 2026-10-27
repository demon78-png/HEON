package com.example.model

data class AuditLog(
    val id: String,
    val actor: String,
    val action: String,
    val target: String,
    val timestamp: String,
    val status: String = "SUCCESS",
    val details: String
)

data class PlatformMetrics(
    val totalGamesCatalog: Int = 8,
    val publishedGames: Int = 7,
    val earlyAccessGames: Int = 1,
    val registeredPlayers: Int = 14290,
    val activeDownloadsToday: Int = 384,
    val primeSubscribers: Int = 1890,
    val openSupportTickets: Int = 4,
    val cloudServerHealth: String = "Operational (99.98% uptime)"
)
