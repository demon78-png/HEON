package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DefaultPremiumPlans
import com.example.model.PremiumPlan
import com.example.model.TransactionRecord
import com.example.ui.components.HeonIconButton
import com.example.ui.components.HeonPrimaryButton
import com.example.ui.components.HeonSecondaryButton
import com.example.ui.theme.HeonAccentNeon
import com.example.ui.theme.HeonAccentPurple
import com.example.ui.theme.HeonBackgroundPrimary
import com.example.ui.theme.HeonBorderSubtle
import com.example.ui.theme.HeonCardElevated
import com.example.ui.theme.HeonSuccess
import com.example.ui.theme.HeonTextMuted
import com.example.ui.theme.HeonTextPrimary
import com.example.ui.theme.HeonTextSecondary

@Composable
fun PremiumScreen(
    isPremium: Boolean,
    transactions: List<TransactionRecord>,
    onBack: () -> Unit,
    onPurchasePlan: (PremiumPlan) -> Unit,
    onRestorePurchases: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var selectedPlanId by remember { mutableStateOf("plan_annual") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HeonBackgroundPrimary)
            .statusBarsPadding()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeonIconButton(
                icon = Icons.Default.ArrowBack,
                contentDescription = "Back",
                onClick = onBack,
                testTag = "premium_back_button"
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "HEON Prime Membership",
                color = HeonTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Hero Banner
            item(key = "prime_hero") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF1E0F38), Color(0xFF112209), Color(0xFF0C101B))
                            )
                        )
                        .border(1.dp, Color(0x66B9FF39), RoundedCornerShape(20.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xCC090D02))
                                    .border(1.dp, HeonAccentNeon, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Diamond,
                                    contentDescription = null,
                                    tint = HeonAccentNeon,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "ELEVATE YOUR PLAY",
                                color = HeonAccentNeon,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Unrestricted Gaming Freedom",
                            color = HeonTextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Priority cloud game servers, 4K artwork & OST packs, day-one early beta access, and 100% ad-free experience across Android.",
                            color = HeonTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // Plan Options
            item(key = "plans_header") {
                Text(
                    text = "Select Your Plan",
                    color = HeonTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(DefaultPremiumPlans, key = { it.id }) { plan ->
                val isSelected = selectedPlanId == plan.id
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF1E2812) else HeonCardElevated
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.5.dp,
                            if (isSelected) HeonAccentNeon else HeonBorderSubtle,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedPlanId = plan.id }
                        .testTag("plan_card_${plan.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = plan.name,
                                    color = HeonTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${plan.priceDisplay} ${plan.billingInterval}",
                                    color = HeonAccentNeon,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            if (plan.badge != null) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(HeonAccentNeon)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = plan.badge,
                                        color = Color(0xFF090D02),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        plan.perks.forEach { perk ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (isSelected) HeonAccentNeon else HeonSuccess,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = perk,
                                    color = HeonTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Checkout CTA
            item(key = "checkout_actions") {
                val activePlan = DefaultPremiumPlans.find { it.id == selectedPlanId } ?: DefaultPremiumPlans.first()
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    HeonPrimaryButton(
                        text = if (isPremium) "Update Subscription Plan" else "Subscribe via Google Play (${activePlan.priceDisplay})",
                        onClick = { onPurchasePlan(activePlan) },
                        icon = Icons.Default.Payment,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "subscribe_cta_button"
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    HeonSecondaryButton(
                        text = "Restore Existing Purchases",
                        onClick = onRestorePurchases,
                        icon = Icons.Default.Restore,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "restore_purchases_button"
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Billed via Google Play Store. Cancel anytime in Google Play Subscriptions settings. No hidden fees.",
                        color = HeonTextMuted,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }

            // Transaction History
            item(key = "transactions_header") {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = HeonTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Transaction History",
                        color = HeonTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            items(transactions, key = { it.id }) { tx ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, HeonBorderSubtle, RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = tx.planName,
                                color = HeonTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${tx.orderId} • ${tx.dateDisplay}",
                                color = HeonTextMuted,
                                fontSize = 10.sp
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = tx.amountDisplay,
                                color = HeonAccentNeon,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = tx.status.label,
                                color = HeonSuccess,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
