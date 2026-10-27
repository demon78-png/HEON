package com.example.ui.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DefaultFaqs
import com.example.model.FaqItem
import com.example.model.SupportTicket
import com.example.model.TicketStatus
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
fun SupportScreen(
    tickets: List<SupportTicket>,
    onBack: () -> Unit,
    onSubmitTicket: (String, String, String) -> Unit,
    onSubmitBugReport: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var expandedFaqId by remember { mutableStateOf<String?>("faq_01") }
    var showTicketDialog by remember { mutableStateOf(false) }
    var showBugDialog by remember { mutableStateOf(false) }

    var ticketSubject by remember { mutableStateOf("") }
    var ticketCategory by remember { mutableStateOf("Downloads & Installation") }
    var ticketDescription by remember { mutableStateOf("") }

    var bugSummary by remember { mutableStateOf("") }
    var bugSteps by remember { mutableStateOf("") }

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
                testTag = "support_back_button"
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Help Center & Support",
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
            // Action banner
            item(key = "support_actions_banner") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HeonPrimaryButton(
                        text = "New Ticket",
                        onClick = { showTicketDialog = true },
                        icon = Icons.Default.ConfirmationNumber,
                        modifier = Modifier.weight(1f),
                        testTag = "open_new_ticket_dialog"
                    )
                    HeonSecondaryButton(
                        text = "Report Bug",
                        onClick = { showBugDialog = true },
                        icon = Icons.Default.BugReport,
                        modifier = Modifier.weight(1f),
                        testTag = "open_bug_report_dialog"
                    )
                }
            }

            // Ticket Submission Form Inline if active
            if (showTicketDialog) {
                item(key = "ticket_form") {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, HeonAccentNeon, RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Submit Support Request",
                                color = HeonTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = ticketSubject,
                                onValueChange = { ticketSubject = it },
                                placeholder = { Text("Subject (e.g. Download issue on Wi-Fi)", color = HeonTextMuted, fontSize = 12.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF141722),
                                    unfocusedContainerColor = Color(0xFF141722),
                                    focusedBorderColor = HeonAccentNeon,
                                    unfocusedBorderColor = HeonBorderSubtle,
                                    focusedTextColor = HeonTextPrimary,
                                    unfocusedTextColor = HeonTextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = ticketDescription,
                                onValueChange = { ticketDescription = it },
                                placeholder = { Text("Explain your issue in detail...", color = HeonTextMuted, fontSize = 12.sp) },
                                maxLines = 4,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF141722),
                                    unfocusedContainerColor = Color(0xFF141722),
                                    focusedBorderColor = HeonAccentNeon,
                                    unfocusedBorderColor = HeonBorderSubtle,
                                    focusedTextColor = HeonTextPrimary,
                                    unfocusedTextColor = HeonTextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                HeonPrimaryButton(
                                    text = "Send Request",
                                    onClick = {
                                        if (ticketSubject.isNotBlank()) {
                                            onSubmitTicket(ticketCategory, ticketSubject, ticketDescription)
                                            ticketSubject = ""
                                            ticketDescription = ""
                                            showTicketDialog = false
                                        }
                                    },
                                    icon = Icons.Default.Send,
                                    modifier = Modifier.weight(1f)
                                )
                                HeonSecondaryButton(
                                    text = "Cancel",
                                    onClick = { showTicketDialog = false },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // Bug Report Form Inline if active
            if (showBugDialog) {
                item(key = "bug_form") {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Submit Bug / Diagnostic Report",
                                color = HeonTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Auto-attaching: ${Build.MANUFACTURER} ${Build.MODEL} (Android ${Build.VERSION.RELEASE})",
                                color = HeonAccentNeon,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = bugSummary,
                                onValueChange = { bugSummary = it },
                                placeholder = { Text("Bug summary (e.g. Frame stutter during race)", color = HeonTextMuted, fontSize = 12.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF141722),
                                    unfocusedContainerColor = Color(0xFF141722),
                                    focusedBorderColor = Color(0xFFEF4444),
                                    unfocusedBorderColor = HeonBorderSubtle,
                                    focusedTextColor = HeonTextPrimary,
                                    unfocusedTextColor = HeonTextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = bugSteps,
                                onValueChange = { bugSteps = it },
                                placeholder = { Text("Steps to reproduce the bug...", color = HeonTextMuted, fontSize = 12.sp) },
                                maxLines = 3,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF141722),
                                    unfocusedContainerColor = Color(0xFF141722),
                                    focusedBorderColor = Color(0xFFEF4444),
                                    unfocusedBorderColor = HeonBorderSubtle,
                                    focusedTextColor = HeonTextPrimary,
                                    unfocusedTextColor = HeonTextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                HeonPrimaryButton(
                                    text = "Submit Report",
                                    onClick = {
                                        if (bugSummary.isNotBlank()) {
                                            onSubmitBugReport(bugSummary, bugSteps)
                                            bugSummary = ""
                                            bugSteps = ""
                                            showBugDialog = false
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                                HeonSecondaryButton(
                                    text = "Cancel",
                                    onClick = { showBugDialog = false },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // My Submitted Tickets
            if (tickets.isNotEmpty()) {
                item(key = "my_tickets_header") {
                    Text(
                        text = "My Support Tickets",
                        color = HeonTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(tickets, key = { it.id }) { ticket ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, HeonBorderSubtle, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "#${ticket.id} • ${ticket.subject}",
                                    color = HeonTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (ticket.status == TicketStatus.RESOLVED) Color(0xFF1B3D14) else Color(0xFF3D3214))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = ticket.status.label,
                                        color = if (ticket.status == TicketStatus.RESOLVED) HeonSuccess else Color(0xFFFBBF24),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = ticket.description, color = HeonTextSecondary, fontSize = 11.sp)
                            if (ticket.staffReply != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF141824))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "Staff Response: ${ticket.staffReply}",
                                        color = HeonAccentNeon,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // FAQs Accordion
            item(key = "faq_header") {
                Text(
                    text = "Frequently Asked Questions",
                    color = HeonTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(DefaultFaqs, key = { it.id }) { faq ->
                val isExpanded = expandedFaqId == faq.id
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = HeonCardElevated),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, HeonBorderSubtle, RoundedCornerShape(12.dp))
                        .clickable {
                            expandedFaqId = if (isExpanded) null else faq.id
                        }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = faq.question,
                                color = HeonTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = HeonAccentNeon,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        AnimatedVisibility(visible = isExpanded) {
                            Column {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = faq.answer,
                                    color = HeonTextSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
