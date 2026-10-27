package com.example.model

enum class DownloadState(val label: String) {
    QUEUED("Queued"),
    DOWNLOADING("Downloading"),
    PAUSED("Paused"),
    VERIFYING("Verifying Checksum"),
    COMPLETED("Downloaded"),
    INSTALLING("Installing"),
    FAILED("Download Failed"),
    CANCELLED("Cancelled")
}

data class DownloadItem(
    val gameId: String,
    val gameTitle: String,
    val version: String,
    val totalBytes: Long,
    val downloadedBytes: Long = 0L,
    val downloadSpeedBps: Long = 12_500_000L, // 12.5 MB/s
    val state: DownloadState = DownloadState.DOWNLOADING,
    val checksumSha256: String = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
    val errorMessage: String? = null,
    val startedAt: Long = System.currentTimeMillis()
) {
    val progressFloat: Float
        get() = if (totalBytes > 0) (downloadedBytes.toFloat() / totalBytes.toFloat()).coerceIn(0f, 1f) else 0f

    val formattedDownloadedSize: String
        get() = formatBytes(downloadedBytes)

    val formattedTotalSize: String
        get() = formatBytes(totalBytes)

    val formattedSpeed: String
        get() = "${formatBytes(downloadSpeedBps)}/s"

    companion object {
        fun formatBytes(bytes: Long): String {
            if (bytes <= 0) return "0 B"
            val kb = bytes / 1024.0
            val mb = kb / 1024.0
            val gb = mb / 1024.0
            return when {
                gb >= 1.0 -> String.format("%.2f GB", gb)
                mb >= 1.0 -> String.format("%.1f MB", mb)
                kb >= 1.0 -> String.format("%.1f KB", kb)
                else -> "$bytes B"
            }
        }
    }
}

data class InstalledGameInfo(
    val gameId: String,
    val installedVersion: String,
    val installPath: String = "/data/app/com.heon.games/",
    val installedSizeBytes: Long,
    val installedAt: Long = System.currentTimeMillis(),
    val lastPlayedAt: Long? = null,
    val hasUpdateAvailable: Boolean = false,
    val latestVersionAvailable: String? = null
)
