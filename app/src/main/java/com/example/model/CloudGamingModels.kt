package com.example.model

enum class CloudSessionState(val label: String) {
    IDLE("Ready"),
    QUEUED("In Server Queue"),
    PROVISIONING("Provisioning GPU Instance"),
    CONNECTING("Establishing WebRTC Tunnel"),
    STREAMING("Active Stream"),
    PAUSED("Stream Suspended"),
    RECONNECTING("Reconnecting to Edge Node"),
    TERMINATED("Session Ended")
}

data class CloudServerRegion(
    val id: String,
    val name: String,
    val location: String,
    val pingMs: Int,
    val isRecommended: Boolean = false
)

val DefaultCloudRegions = listOf(
    CloudServerRegion("eu_central", "EU Central (Frankfurt)", "Germany", 16, true),
    CloudServerRegion("us_east", "US East (Virginia)", "United States", 28),
    CloudServerRegion("ap_east", "AP East (Tokyo)", "Japan", 44)
)

enum class StreamQualityPreset(
    val label: String,
    val resolution: String,
    val frameRate: Int,
    val requiredBitrateMbps: Float,
    val requiresPrime: Boolean = false
) {
    BALANCED("Balanced", "1080p", 60, 15f),
    HIGH_FPS("Pro Performance", "1080p", 120, 22f, requiresPrime = true),
    ULTRA_CINEMATIC("4K Ultra", "2160p", 60, 35f, requiresPrime = true)
}

data class CloudSession(
    val sessionId: String,
    val gameId: String,
    val gameTitle: String,
    val region: CloudServerRegion,
    val quality: StreamQualityPreset = StreamQualityPreset.BALANCED,
    val state: CloudSessionState = CloudSessionState.STREAMING,
    val latencyMs: Int = 18,
    val currentFps: Int = 60,
    val currentBitrateMbps: Float = 14.8f,
    val packetLossPercent: Float = 0.02f,
    val elapsedSeconds: Int = 0,
    val touchControlsEnabled: Boolean = true
)
