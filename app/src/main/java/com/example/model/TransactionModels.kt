package com.example.model

enum class TransactionStatus(val label: String) {
    COMPLETED("Completed"),
    PENDING("Processing"),
    RESTORED("Restored"),
    REFUNDED("Refunded"),
    FAILED("Failed")
}

data class PremiumPlan(
    val id: String,
    val name: String,
    val priceDisplay: String,
    val billingInterval: String,
    val isPopular: Boolean = false,
    val badge: String? = null,
    val perks: List<String>
)

val DefaultPremiumPlans = listOf(
    PremiumPlan(
        id = "plan_monthly",
        name = "HEON Prime Monthly",
        priceDisplay = "$4.99",
        billingInterval = "per month",
        isPopular = false,
        perks = listOf(
            "Priority Cloud Game Streaming bandwidth",
            "Zero advertising throughout the platform",
            "Exclusive Member Discounts on indie releases",
            "Full 4K Ultra-HD Wallpapers & OST Downloads",
            "Discord Role & Verified Player badge"
        )
    ),
    PremiumPlan(
        id = "plan_annual",
        name = "HEON Prime Annual",
        priceDisplay = "$49.99",
        billingInterval = "per year (Save 17%)",
        isPopular = true,
        badge = "BEST VALUE",
        perks = listOf(
            "Everything in Monthly + 2 Months Free",
            "Day-one closed beta test invitations",
            "Unlimited cloud saves & multi-device sync",
            "Exclusive Neon Avatar Frames & Profile Glow",
            "VIP priority in customer support queue"
        )
    )
)

data class TransactionRecord(
    val id: String,
    val orderId: String,
    val planName: String,
    val amountDisplay: String,
    val dateDisplay: String,
    val status: TransactionStatus,
    val paymentMethod: String = "Google Play Billing (**** 4118)"
)
