package com.example.mybank.presentation.notifications

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// ====================== PREMIUM COLORS ======================
private val PurplePrimary = Color(0xFF8B7FFF)
private val PurpleLight = Color(0xFFB8A9FF)
private val PurpleMedium = Color(0xFF9B8FFF)
private val PurpleVibrant = Color(0xFF7B6FEE)
private val PurpleDark = Color(0xFF5B4FCC)
private val TextPrimary = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF9CA3AF)
private val TextWhite = Color(0xFFFFFFFF)
private val SuccessGreen = Color(0xFF00C566)
private val ErrorRed = Color(0xFFFF4757)
private val WarningOrange = Color(0xFFFF9F43)
private val InfoBlue = Color(0xFF4FD8FF)
private val CardBgWhite = Color(0xFFFAFAFC)
private val IconBgLight = Color(0xFFF5F5F7)
private val DividerColor = Color(0xFFE5E7EB)
private val NewBadgeColor = Color(0xFFFF4757)

// Premium background gradient
private val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFE8E0FF),
        Color(0xFFF0ECFF),
        Color(0xFFF8F6FF),
        Color(0xFFFFFEFF)
    )
)

// ====================== DATA CLASSES ======================

/**
 * Notification type enum for different notification styles
 */
enum class NotificationCategory {
    INCOMING_TRANSFER,
    PAYMENT,
    LOW_BALANCE,
    SALARY,
    SCHEDULED_DEBIT,
    SECURITY_ALERT,
    PROMOTION
}

/**
 * Data class representing a premium notification
 */
data class PremiumNotification(
    val id: String,
    val category: NotificationCategory,
    val title: String,
    val message: String,
    val date: String,
    val time: String,
    val isNew: Boolean = false,
    val amount: String? = null,
    val isPositive: Boolean = true
)

// ====================== MAIN SCREEN ======================

/**
 * NotificationsScreen - Premium Banking Notifications Screen
 * 
 * This screen displays all user notifications with a modern, elegant UI.
 * Features include:
 * - Slide-down animation on entry
 * - Category-specific icons and colors
 * - "New" badge for unread notifications
 * - Beautiful card design with shadows
 * - Mark all as read functionality
 * 
 * @param onNavigateBack Navigation callback for back action
 */
@Composable
fun NotificationsScreen(
    onNavigateBack: () -> Unit = {}
) {
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    
    // Context for Toast messages
    val context = LocalContext.current
    
    // Sample notifications data
    val notifications = remember {
        mutableStateListOf(
            PremiumNotification(
                id = "1",
                category = NotificationCategory.INCOMING_TRANSFER,
                title = "Incoming Transfer",
                message = "You have received €150.00 from Jean Martin",
                date = "January 3, 2026",
                time = "10:30",
                isNew = true,
                amount = "+€150.00",
                isPositive = true
            ),
            PremiumNotification(
                id = "2",
                category = NotificationCategory.PAYMENT,
                title = "Payment Completed",
                message = "Casino Supermarket: – €45.20",
                date = "January 3, 2026",
                time = "09:15",
                isNew = true,
                amount = "-€45.20",
                isPositive = false
            ),
            PremiumNotification(
                id = "3",
                category = NotificationCategory.LOW_BALANCE,
                title = "Low Balance",
                message = "Your Livret A account balance is below €200",
                date = "January 2, 2026",
                time = "14:20",
                isNew = true,
                amount = null,
                isPositive = false
            ),
            PremiumNotification(
                id = "4",
                category = NotificationCategory.SALARY,
                title = "Salary Received",
                message = "A transfer of €2,800.00 has been credited to your account",
                date = "January 2, 2026",
                time = "08:00",
                isNew = false,
                amount = "+€2,800.00",
                isPositive = true
            ),
            PremiumNotification(
                id = "5",
                category = NotificationCategory.SCHEDULED_DEBIT,
                title = "Scheduled Debit",
                message = "Netflix: €13.99 will be debited tomorrow",
                date = "January 1, 2026",
                time = "18:00",
                isNew = false,
                amount = "-€13.99",
                isPositive = false
            ),
            PremiumNotification(
                id = "6",
                category = NotificationCategory.SECURITY_ALERT,
                title = "Security Alert",
                message = "New login detected from iPhone 15 Pro in Paris",
                date = "January 1, 2026",
                time = "12:45",
                isNew = false,
                amount = null,
                isPositive = true
            ),
            PremiumNotification(
                id = "7",
                category = NotificationCategory.PROMOTION,
                title = "Special Offer",
                message = "Get 2% cashback on all purchases this weekend!",
                date = "December 31, 2025",
                time = "09:00",
                isNew = false,
                amount = null,
                isPositive = true
            )
        )
    }
    
    // Count new notifications
    val newCount = remember(notifications) {
        notifications.count { it.isNew }
    }
    
    // Mark all as read function
    fun markAllAsRead() {
        val updatedList = notifications.map { it.copy(isNew = false) }
        notifications.clear()
        notifications.addAll(updatedList)
        Toast.makeText(context, "All notifications marked as read", Toast.LENGTH_SHORT).show()
    }
    
    // Trigger entrance animations
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGradient)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // ========== HEADER ==========
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { -40 }
            ) {
                NotificationsHeader(
                    newCount = newCount,
                    onBackClick = onNavigateBack,
                    onMarkAllRead = { markAllAsRead() }
                )
            }
            
            // ========== NOTIFICATIONS LIST ==========
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(notifications) { index, notification ->
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(tween(600, 100 + index * 80)) + 
                               slideInVertically(
                                   animationSpec = tween(600, 100 + index * 80),
                                   initialOffsetY = { it / 2 }
                               )
                    ) {
                        PremiumNotificationCard(
                            notification = notification,
                            onClick = {
                                // Mark as read on click
                                val updatedNotification = notification.copy(isNew = false)
                                val idx = notifications.indexOf(notification)
                                if (idx >= 0) {
                                    notifications[idx] = updatedNotification
                                }
                                Toast.makeText(context, notification.title, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
                
                // Bottom spacing
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
        
        // Empty state
        if (notifications.isEmpty()) {
            EmptyNotificationsState(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

// ====================== HEADER ======================

@Composable
private fun NotificationsHeader(
    newCount: Int,
    onBackClick: () -> Unit,
    onMarkAllRead: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(CardBgWhite)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = TextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
        
        // Title with badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Notifications",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = (-0.5).sp
            )
            
            // New count badge
            if (newCount > 0) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NewBadgeColor
                ) {
                    Text(
                        text = "$newCount new",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                }
            }
        }
        
        // Mark all read button
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (newCount > 0) PurplePrimary.copy(alpha = 0.1f) else CardBgWhite)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = newCount > 0
                ) { onMarkAllRead() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.DoneAll,
                contentDescription = "Mark all read",
                tint = if (newCount > 0) PurplePrimary else TextSecondary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// ====================== NOTIFICATION CARD ======================

@Composable
private fun PremiumNotificationCard(
    notification: PremiumNotification,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "cardScale"
    )
    
    // Get icon and colors based on category
    val (icon, iconBgColor, iconColor) = remember(notification.category) {
        when (notification.category) {
            NotificationCategory.INCOMING_TRANSFER -> Triple(
                Icons.Outlined.CallReceived,
                Color(0xFFE5F9E7),
                SuccessGreen
            )
            NotificationCategory.PAYMENT -> Triple(
                Icons.Outlined.ShoppingCart,
                Color(0xFFFFE5E5),
                ErrorRed
            )
            NotificationCategory.LOW_BALANCE -> Triple(
                Icons.Outlined.Warning,
                Color(0xFFFFF3E0),
                WarningOrange
            )
            NotificationCategory.SALARY -> Triple(
                Icons.Outlined.AccountBalance,
                Color(0xFFE5F9E7),
                SuccessGreen
            )
            NotificationCategory.SCHEDULED_DEBIT -> Triple(
                Icons.Outlined.Schedule,
                Color(0xFFE3F2FD),
                InfoBlue
            )
            NotificationCategory.SECURITY_ALERT -> Triple(
                Icons.Outlined.Security,
                Color(0xFFFFF3E0),
                WarningOrange
            )
            NotificationCategory.PROMOTION -> Triple(
                Icons.Outlined.LocalOffer,
                Color(0xFFE8E0FF),
                PurplePrimary
            )
        }
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = if (notification.isNew) 8.dp else 4.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = if (notification.isNew) PurplePrimary.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.08f)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(26.dp)
                )
            }
            
            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Title row with "New" badge
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (notification.isNew) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NewBadgeColor
                        ) {
                            Text(
                                text = "New",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                    }
                }
                
                // Message
                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Date and time
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarToday,
                        contentDescription = null,
                        tint = TextSecondary.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${notification.date} · ${notification.time}",
                        fontSize = 12.sp,
                        color = TextSecondary.copy(alpha = 0.7f)
                    )
                }
            }
            
            // Amount (if applicable)
            if (notification.amount != null) {
                Text(
                    text = notification.amount,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (notification.isPositive) SuccessGreen else TextPrimary
                )
            }
        }
        
        // New indicator line on the left
        if (notification.isNew) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(PurplePrimary, PurpleLight)
                        ),
                        RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                    )
            )
        }
    }
    
    // Reset press state
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

// ====================== EMPTY STATE ======================

@Composable
private fun EmptyNotificationsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Empty illustration
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(PurplePrimary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.NotificationsOff,
                contentDescription = null,
                tint = PurplePrimary,
                modifier = Modifier.size(60.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "No notifications",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "You're all caught up!\nNew notifications will appear here.",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

// ====================== PREVIEWS ======================

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotificationsScreenPreview() {
    MaterialTheme {
        NotificationsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PremiumNotificationCardPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .background(BackgroundGradient)
                .padding(20.dp)
        ) {
            PremiumNotificationCard(
                notification = PremiumNotification(
                    id = "1",
                    category = NotificationCategory.INCOMING_TRANSFER,
                    title = "Incoming Transfer",
                    message = "You have received €150.00 from Jean Martin",
                    date = "January 3, 2026",
                    time = "10:30",
                    isNew = true,
                    amount = "+€150.00",
                    isPositive = true
                ),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LowBalanceNotificationPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .background(BackgroundGradient)
                .padding(20.dp)
        ) {
            PremiumNotificationCard(
                notification = PremiumNotification(
                    id = "3",
                    category = NotificationCategory.LOW_BALANCE,
                    title = "Low Balance",
                    message = "Your Livret A account balance is below €200",
                    date = "January 2, 2026",
                    time = "14:20",
                    isNew = true,
                    amount = null,
                    isPositive = false
                ),
                onClick = {}
            )
        }
    }
}
