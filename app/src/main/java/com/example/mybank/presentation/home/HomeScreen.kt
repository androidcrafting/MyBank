package com.example.mybank.presentation.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mybank.data.model.Account
import com.example.mybank.data.model.Transaction
import com.example.mybank.data.model.TransactionType
import java.text.SimpleDateFormat
import java.util.*

// ====================== PREMIUM COLORS ======================
private val PurplePrimary = Color(0xFF8B7FFF)
private val PurpleLight = Color(0xFFA99FFF)
private val PurpleMedium = Color(0xFF9B8FFF)
private val DarkPurple = Color(0xFF2E2650)
private val DarkCard = Color(0xFF1E1E2E)
val ShadowMedium = Color(0x338B7FFF)
val BackgroundDark = Color(0xFF121212)
val WarningYellowDark = Color(0xFFFFA300)
private val PinkPrimary = Color(0xFFFF6FD8)
private val PinkLight = Color(0xFFFF8FE8)
private val BlueCyan = Color(0xFF4FD8FF)
private val TextPrimary = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF9CA3AF)
private val TextWhite = Color(0xFFFFFFFF)
private val SuccessGreen = Color(0xFF00C566)
private val DividerColor = Color(0xFFE5E7EB)
private val BgGradientStart = Color(0xFFE8E4FF)
private val BgGradientMid = Color(0xFFF5F3FF)
private val BgGradientEnd = Color(0xFFFFFBFF)
private val CardBgWhite = Color(0xFFFAFAFC)
private val IconBgLight = Color(0xFFF5F5F7)

// Premium background gradient
private val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFE8E0FF),
        Color(0xFFF0ECFF),
        Color(0xFFF8F6FF),
        Color(0xFFFFFEFF)
    )
)

@Composable
fun HomeScreen(
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToAccountDetails: (String) -> Unit = {},
    onNavigateToCards: () -> Unit = {},
    onNavigateToTransfers: () -> Unit = {},
    onNavigateToAllTransactions: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    
    // More menu state
    var showMoreMenu by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CardBgWhite)
    ) {
        // Main content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header with avatar and greeting
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { -40 }
                ) {
                    PremiumHeader(
                        userName = "Ilyass",
                        onNotificationClick = onNavigateToNotifications
                    )
                }
            }
            
            // Total Balance
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, 100)) + slideInVertically(tween(600, 100)) { -30 }
                ) {
                    TotalBalanceSection(
                        balance = if (uiState.totalBalance > 0) uiState.totalBalance else 10000.0
                    )
                }
            }
            
            // Cards Section
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, 200)) + slideInVertically(tween(600, 200)) { -20 }
                ) {
                    CardsSection(
                        accounts = uiState.accounts,
                        onCardClick = onNavigateToAccountDetails,
                        onAddCardClick = onNavigateToAccounts
                    )
                }
            }
            
            // Finance Quick Actions
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, 300)) + slideInVertically(tween(600, 300)) { -20 }
                ) {
                    FinanceQuickActions()
                }
            }
            
            // Last Transactions
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, 400)) + slideInVertically(tween(600, 400)) { -20 }
                ) {
                    LastTransactionsSection(
                        transactions = uiState.recentTransactions,
                        onSeeAllClick = onNavigateToAllTransactions
                    )
                }
            }
        }
        
        // Bottom Navigation Bar
        PremiumBottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            onCardsClick = onNavigateToCards,
            onTransfersClick = onNavigateToTransfers,
            onMoreClick = { showMoreMenu = true }
        )
        
        // More Menu Overlay
        MoreMenuOverlay(
            isVisible = showMoreMenu,
            onDismiss = { showMoreMenu = false },
            onSettingsClick = {
                showMoreMenu = false
                onNavigateToSettings()
            },
            onAccountsClick = {
                showMoreMenu = false
                onNavigateToAccounts()
            }
        )
    }
}

// ====================== PREMIUM HEADER ======================
@Composable

private fun PremiumHeader(
    userName: String,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar and greeting
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular avatar with gradient
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .shadow(8.dp, CircleShape, spotColor = PurplePrimary.copy(alpha = 0.3f))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFFA300),
                                Color(0xFF121212)
                            )
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "JT",
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Text(
                text = "Hello, $userName",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
        
        // Notification bell with badge
        Box(
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = TextPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
            
            // Red notification badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-6).dp, y = 6.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF4757)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "3",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ====================== TOTAL BALANCE SECTION ======================

@Composable
private fun TotalBalanceSection(balance: Double) {
    val animatedBalance by animateFloatAsState(
        targetValue = balance.toFloat(),
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "balance"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        Text(
            text = "Balance",
            fontSize = 14.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Normal
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Format balance with space separator: $8 600
        val formattedBalance = String.format("%,.0f", animatedBalance).replace(",", " ")
        Text(
            text = "$$formattedBalance",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            letterSpacing = (-1).sp
        )
    }
}

// ====================== CARDS SECTION ======================

@Composable
private fun CardsSection(
    accounts: List<Account>,
    onCardClick: (String) -> Unit,
    onAddCardClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        // Section header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CARDS",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
            
            Text(
                text = "Add +",
                fontSize = 13.sp,
                color = BackgroundDark,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onAddCardClick() }
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Cards carousel with default cards if empty
        val displayCards = if (accounts.isEmpty()) {
            listOf(
                CardData("Salary", 2230.0, "6917", "01/04", 0),
                CardData("Credit card", 5230.0, "4433", "02/04", 1),
                CardData("Debit card", 985.0, "1788", "03/04", 2)
            )
        } else {
            accounts.take(3).mapIndexed { index, account ->
                CardData(account.accountName, account.balance, account.accountNumber.takeLast(4), "0${index + 1}/04", index)
            }
        }
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(displayCards) { index, card ->
                PremiumBankCard(
                    cardData = card,
                    onClick = { 
                        if (accounts.isNotEmpty() && index < accounts.size) {
                            onCardClick(accounts[index].id)
                        }
                    }
                )
            }
        }
    }
}

private data class CardData(
    val name: String,
    val balance: Double,
    val lastFour: String,
    val expiry: String,
    val colorIndex: Int
)

@Composable
private fun PremiumBankCard(
    cardData: CardData,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardScale"
    )
    
    val cardGradient = when (cardData.colorIndex % 3) {
        0 -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF2E2650),
                Color(0xE6FFFFFF)
            )
        )
        1 -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF2E2650),
                Color(0xFF1A1A2E)
            )
        )
        else -> Brush.linearGradient(
            colors = listOf(
                Color(0xFFFFA300),
                Color(0xFF121212)
            )
        )
    }
    
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp)
            .scale(scale)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = when (cardData.colorIndex % 3) {
                    0 -> PurplePrimary.copy(alpha = 0.4f)
                    1 -> DarkPurple.copy(alpha = 0.4f)
                    else -> PinkPrimary.copy(alpha = 0.4f)
                }
            )
            .clip(RoundedCornerShape(20.dp))
            .background(cardGradient)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = !isPressed
                onClick()
            }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // VISA logo
            Text(
                text = "VISA",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                letterSpacing = 1.sp
            )
            
            // Card name and balance
            Column {
                Text(
                    text = cardData.name,
                    fontSize = 12.sp,
                    color = TextWhite.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Normal
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                val formattedBalance = String.format("%,.0f", cardData.balance).replace(",", " ")
                Text(
                    text = "$$formattedBalance",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }
            
            // Card number and expiry
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "** ${cardData.lastFour}",
                    fontSize = 12.sp,
                    color = TextWhite.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = cardData.expiry,
                    fontSize = 12.sp,
                    color = TextWhite.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// ====================== FINANCE QUICK ACTIONS ======================
@Composable
private fun FinanceQuickActions() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
//        Text(
//            text = "Actions",
//            fontSize = 12.sp,
//            fontWeight = FontWeight.SemiBold,
//            color = TextSecondary,
//            letterSpacing = 1.sp,
//            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
//        )
        
//        Spacer(modifier = Modifier.height(8.dp))
//
//        LazyRow(
//            contentPadding = PaddingValues(horizontal = 20.dp),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            item {
//                QuickActionButton(
//                    icon = Icons.Outlined.StarOutline,
//                    title = "Bonus",
//                    backgroundColor = CardBgWhite,
//                    iconTint = TextPrimary,
//                    isWhiteCard = true
//                )
//            }
//            item {
//                QuickActionButton(
//                    icon = Icons.Outlined.BarChart,
//                    title = "Finance\nanalysis",
//                    backgroundColor = CardBgWhite,
//                    iconTint = TextPrimary,
//                    isWhiteCard = true
//                )
//            }
//            item {
//                QuickActionButton(
//                    icon = Icons.Outlined.Receipt,
//                    title = "Payment",
//                    backgroundColor = CardBgWhite,
//                    iconTint = TextPrimary,
//                    isWhiteCard = true
//                )
//            }
//            item {
//                QuickActionButton(
//                    icon = Icons.Outlined.TrendingUp,
//                    title = "Investment",
//                    backgroundColor = CardBgWhite,
//                    iconTint = TextPrimary,
//                    isWhiteCard = true
//                )
//            }
//        }
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    title: String,
    backgroundColor: Color,
    iconTint: Color = TextWhite,
    isWhiteCard: Boolean = false
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "buttonScale"
    )
    
    Column(
        modifier = Modifier
            .width(80.dp)
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { isPressed = !isPressed },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .shadow(
                    elevation = if (isWhiteCard) 4.dp else 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = if (isWhiteCard) Color.Black.copy(alpha = 0.08f) else PurplePrimary.copy(alpha = 0.3f)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(26.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = title,
            fontSize = 11.sp,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2
        )
    }
}

// ====================== LAST TRANSACTIONS SECTION ======================
@Composable
private fun LastTransactionsSection(
    transactions: List<Transaction>,
    onSeeAllClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "LAST TRANSACTIONS",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onSeeAllClick() }
            ) {
                Text(
                    text = "See all",
                    fontSize = 13.sp,
                    color = BackgroundDark,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = BackgroundDark,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Display transactions or default items
        val displayTransactions = if (transactions.isEmpty()) {
            listOf(
                TransactionDisplay("Supermarket", "Today, 16:40", -40.0, "Payment"),
                TransactionDisplay("Rent", "Today, 12:30", 20.0, "Transfer")
            )
        } else {
            val dateFormat = SimpleDateFormat("'Today', HH:mm", Locale.getDefault())
            transactions.take(4).map { tx ->
                TransactionDisplay(
                    title = tx.description,
                    time = dateFormat.format(Date(tx.timestamp)),
                    amount = if (tx.type == TransactionType.CREDIT) tx.amount else -tx.amount,
                    type = if (tx.type == TransactionType.CREDIT) "transfer" else "payment"
                )
            }
        }
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            displayTransactions.forEach { transaction ->
                PremiumTransactionItem(transaction = transaction)
            }
        }
    }
}

private data class TransactionDisplay(
    val title: String,
    val time: String,
    val amount: Double,
    val type: String
)

@Composable
private fun PremiumTransactionItem(transaction: TransactionDisplay) {
    val isIncome = transaction.amount > 0
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Transaction icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(IconBgLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        transaction.title.contains("Supermarket", ignoreCase = true) -> Icons.Default.ShoppingCart
                        transaction.title.contains("Bank", ignoreCase = true) -> Icons.Default.AccountBalance
                        transaction.title.contains("Uber", ignoreCase = true) -> Icons.Default.DirectionsCar
                        else -> Icons.Default.Receipt
                    },
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
            
            Column {
                Text(
                    text = transaction.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = transaction.time,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${if (isIncome) "+" else ""}${transaction.amount.toInt()}$",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isIncome) SuccessGreen else TextPrimary
            )
            Text(
                text = transaction.type,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}

// ====================== MORE MENU OVERLAY ======================
@Composable
private fun MoreMenuOverlay(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSettingsClick: () -> Unit,
    onAccountsClick: () -> Unit
) {
    // Background overlay animation
    val overlayAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(300),
        label = "overlayAlpha"
    )
    
    // Menu slide animation
    val menuOffset by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 100.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "menuOffset"
    )
    
    val menuScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "menuScale"
    )
    
    if (isVisible || overlayAlpha > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = overlayAlpha }
        ) {
            // Semi-transparent background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onDismiss() }
            )
            
            // Menu card positioned above bottom nav
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp, start = 20.dp, end = 20.dp)
                    .offset(y = menuOffset)
                    .scale(menuScale)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 20.dp
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Header
                        Text(
                            text = "Quick Actions",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        // Settings option
                        MoreMenuItem(
                            icon = Icons.Outlined.Settings,
                            title = "Settings",
                            subtitle = "App preferences & security",
                            iconBgColor = Color(0xFFE8E0FF),
                            iconColor = PurplePrimary,
                            onClick = onSettingsClick
                        )
                        
                        HorizontalDivider(
                            color = DividerColor,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        
                        // Accounts option
                        MoreMenuItem(
                            icon = Icons.Outlined.AccountBalance,
                            title = "Accounts",
                            subtitle = "View all your accounts",
                            iconBgColor = Color(0xFFE5F9E7),
                            iconColor = SuccessGreen,
                            onClick = onAccountsClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MoreMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconBgColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "menuItemScale"
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
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
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(26.dp)
            )
        }
        
        // Text content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = TextSecondary
            )
        }
        
        // Arrow icon
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(24.dp)
        )
    }
    
    // Reset press state
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(150)
            isPressed = false
        }
    }
}

// ====================== BOTTOM NAVIGATION BAR ======================
@Composable
private fun PremiumBottomNavigationBar(
    modifier: Modifier = Modifier,
    onCardsClick: () -> Unit = {},
    onTransfersClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        color = Color.White,
        shadowElevation = 16.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Outlined.Home,
                selectedIcon = Icons.Filled.Home,
                label = "Home",
                isSelected = selectedIndex == 0,
                onClick = { selectedIndex = 0 }
            )
            BottomNavItem(
                icon = Icons.Outlined.Layers,
                selectedIcon = Icons.Filled.Layers,
                label = "Cards",
                isSelected = selectedIndex == 1,
                onClick = { 
                    selectedIndex = 1
                    onCardsClick()
                }
            )
            BottomNavItem(
                icon = Icons.Outlined.CompareArrows,
                selectedIcon = Icons.Filled.CompareArrows,
                label = "Transfers",
                isSelected = selectedIndex == 2,
                onClick = { 
                    selectedIndex = 2
                    onTransfersClick()
                }
            )
            BottomNavItem(
                icon = Icons.Outlined.Apps,
                selectedIcon = Icons.Filled.Apps,
                label = "More",
                isSelected = selectedIndex == 3,
                onClick = { 
                    selectedIndex = 3
                    onMoreClick()
                }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    selectedIcon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "navScale"
    )
    
    // Animate the indicator dot
    val indicatorAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        label = "indicatorAlpha"
    )
    
    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = if (isSelected) selectedIcon else icon,
            contentDescription = label,
            tint = if (isSelected) WarningYellowDark else TextSecondary,
            modifier = Modifier
                .size(24.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = if (isSelected) WarningYellowDark else TextSecondary,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
        
        // Purple indicator dot for selected item
        Box(
            modifier = Modifier
                .size(5.dp)
                .graphicsLayer { alpha = indicatorAlpha }
                .clip(CircleShape)
                .background(WarningYellowDark)
        )
    }
}
