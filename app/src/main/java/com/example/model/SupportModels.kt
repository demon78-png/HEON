package com.example.model

enum class TicketStatus(val label: String) {
    OPEN("Open"),
    INVESTIGATING("Staff Investigating"),
    RESOLVED("Resolved")
}

data class SupportTicket(
    val id: String,
    val category: String,
    val subject: String,
    val description: String,
    val priority: String = "Normal",
    val status: TicketStatus = TicketStatus.OPEN,
    val dateCreated: String,
    val staffReply: String? = null
)

data class FaqItem(
    val id: String,
    val category: String,
    val question: String,
    val answer: String
)

val DefaultFaqs = listOf(
    FaqItem(
        id = "faq_01",
        category = "Downloads",
        question = "How are game installations handled on Android?",
        answer = "HEON downloads verified APK installation packages with SHA-256 integrity verification. Once downloaded, tap 'Install' to initiate Android's secure Package Installer consent prompt."
    ),
    FaqItem(
        id = "faq_02",
        category = "Cloud Gaming",
        question = "What internet connection is needed for Cloud Streaming?",
        answer = "We recommend a stable 5GHz Wi-Fi or 5G mobile connection with at least 15 Mbps download speed and under 40ms ping for buttery smooth 60fps streaming."
    ),
    FaqItem(
        id = "faq_03",
        category = "Accounts & Sync",
        question = "Can I transfer my progress from Guest Mode?",
        answer = "Yes! Simply go to your Profile and select 'Upgrade to Full Account'. All your saved wishlist games, downloads, and playtime records will safely merge without loss."
    ),
    FaqItem(
        id = "faq_04",
        category = "Billing",
        question = "How do cancellations work for HEON Prime?",
        answer = "You can manage or cancel your subscription at any time directly through Google Play Subscriptions settings. Your benefits remain active until the end of your billing cycle."
    )
)

data class BugReport(
    val reportId: String,
    val summary: String,
    val stepsToReproduce: String,
    val deviceModel: String,
    val androidVersion: String,
    val timestamp: String
)
