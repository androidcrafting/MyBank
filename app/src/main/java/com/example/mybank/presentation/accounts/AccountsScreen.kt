package com.example.mybank.presentation.accounts

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mybank.data.model.Account
import com.example.mybank.data.model.AccountType
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.*

// ====================== PREMIUM COLORS ======================
private val PurplePrimary = Color(0xFF8B7FFF)
private val PurpleLight = Color(0xFFB8A9FF)
private val PurpleMedium = Color(0xFF9B8FFF)
private val PurpleVibrant = Color(0xFF7B6FEE)
private val PurpleDark = Color(0xFF5B4FCC)
private val DarkCard = Color(0xFF1E1E2E)
private val PinkPrimary = Color(0xFFFF6FD8)
private val PinkLight = Color(0xFFFF8FE8)
private val TextPrimary = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF9CA3AF)
private val TextWhite = Color(0xFFFFFFFF)
private val SuccessGreen = Color(0xFF00C566)
private val WarningOrange = Color(0xFFFF9F43)
private val CardBgWhite = Color(0xFFFAFAFC)
private val BackgroundDark = Color(0xFF121212)
private val WarningYellowDark = Color(0xFFFFA300)
val GlassShadow = Color(0x1A8B7FFF)

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
 * Data class for displaying account information
 */
data class DisplayAccount(
    val id: String,
    val name: String,
    val accountNumber: String,
    val type: AccountType,
    val balance: Double,
    val currency: String = "EUR",
    val iban: String? = null,
    val cardColorIndex: Int = 0
)

// ====================== MAIN SCREEN ======================

/**
 * AccountsScreen - Premium Banking Accounts Screen
 * 
 * This screen displays all user accounts with a modern, elegant UI.
 * Features include:
 * - Total balance summary
 * - Beautiful account cards with animations
 * - Add new account button
 * - Account type icons and colors
 * 
 * @param onNavigateBack Navigation callback for back action
 * @param onNavigateToAccountDetails Callback when an account is clicked
 */
@Composable
fun AccountsScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToAccountDetails: (String) -> Unit = {},
    viewModel: AccountsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    
    // Context for Toast messages
    val context = LocalContext.current
    
    // Sample accounts data (will be replaced by real data from ViewModel)
    val accounts = remember {
        if (uiState.accounts.isEmpty()) {
            listOf(
                DisplayAccount(
                    id = "1",
                    name = "Main Account",
                    accountNumber = "FR76 1234 5678 9012 3456 7890 123",
                    type = AccountType.CHECKING,
                    balance = 8600.0,
                    cardColorIndex = 0
                ),
                DisplayAccount(
                    id = "2",
                    name = "Savings Account",
                    accountNumber = "FR76 9876 5432 1098 7654 3210 987",
                    type = AccountType.SAVINGS,
                    balance = 15420.50,
                    cardColorIndex = 1
                ),
                DisplayAccount(
                    id = "3",
                    name = "Credit Card",
                    accountNumber = "FR76 5555 4444 3333 2222 1111 000",
                    type = AccountType.CREDIT,
                    balance = -1250.00,
                    cardColorIndex = 2
                )
            )
        } else {
            uiState.accounts.mapIndexed { index, account ->
                DisplayAccount(
                    id = account.id,
                    name = account.accountName,
                    accountNumber = account.iban ?: account.accountNumber,
                    type = account.accountType,
                    balance = account.balance,
                    currency = account.currency,
                    cardColorIndex = index % 3
                )
            }
        }
    }
    
    // Calculate total balance
    val totalBalance = remember(accounts) {
        accounts.sumOf { it.balance }
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // ========== HEADER ==========
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { -40 }
                ) {
                    AccountsHeader(
                        onBackClick = onNavigateBack,
                        onAddClick = {
                            Toast.makeText(context, "Add new account", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
            
            // ========== TOTAL BALANCE CARD ==========
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, 100)) + slideInVertically(tween(600, 100)) { -30 }
                ) {
                    TotalBalanceCard(
                        totalBalance = totalBalance,
                        accountCount = accounts.size,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                    )
                }
            }
            
            // ========== ACCOUNTS SECTION HEADER ==========
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, 200)) + slideInVertically(tween(600, 200)) { -20 }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MY ACCOUNTS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary,
                            letterSpacing = 1.sp
                        )
                        
                        Text(
                            text = "${accounts.size} accounts",
                            fontSize = 12.sp,
                            color = PurplePrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            
            // ========== ACCOUNT CARDS ==========
            itemsIndexed(accounts) { index, account ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, 300 + index * 100)) + 
                           slideInVertically(tween(600, 300 + index * 100)) { 50 }
                ) {
                    PremiumAccountCard(
                        account = account,
                        onClick = { onNavigateToAccountDetails(account.id) },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
            }
            
            // ========== ADD ACCOUNT BUTTON ==========
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, 600)) + slideInVertically(tween(600, 600)) { 30 }
                ) {
                    AddAccountButton(
                        onClick = {
                            Toast.makeText(context, "Add new account", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                    )
                }
            }
        }
    }
}

// ====================== HEADER ======================

@Composable
private fun AccountsHeader(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit
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
        
        // Title
        Text(
            text = "Accounts",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            letterSpacing = (-0.5).sp
        )
        
        // Add button
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(PurplePrimary, PurpleMedium)
                    )
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onAddClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Account",
                tint = TextWhite,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ====================== TOTAL BALANCE CARD ======================

@Composable
private fun TotalBalanceCard(
    totalBalance: Double,
    accountCount: Int,
    modifier: Modifier = Modifier
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    
    // Animated balance
    val animatedBalance by animateFloatAsState(
        targetValue = totalBalance.toFloat(),
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "balanceAnimation"
    )
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = PurplePrimary.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            PurplePrimary,
                            PurpleMedium,
                            PurpleLight
                        )
                    )
                )
        ) {
            // Decorative circles
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    radius = size.width * 0.4f,
                    center = Offset(size.width * 0.9f, size.height * 0.2f)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.08f),
                    radius = size.width * 0.3f,
                    center = Offset(size.width * 0.1f, size.height * 0.8f)
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountBalanceWallet,
                        contentDescription = null,
                        tint = TextWhite.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Total Balance",
                        fontSize = 14.sp,
                        color = TextWhite.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Balance
                val formattedBalance = String.format("%,.2f", animatedBalance)
                Text(
                    text = "$$formattedBalance",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    letterSpacing = (-1).sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Account count
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CreditCard,
                        contentDescription = null,
                        tint = TextWhite,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "$accountCount Active Accounts",
                        fontSize = 12.sp,
                        color = TextWhite,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ====================== PREMIUM ACCOUNT CARD ======================

@Composable
private fun PremiumAccountCard(
    account: DisplayAccount,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "cardScale"
    )
    
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    
    // Get card gradient based on type
    val (cardGradient, iconBgColor, accentColor) = remember(account.cardColorIndex) {
        when (account.cardColorIndex % 3) {
            0 -> Triple(
                Brush.linearGradient(listOf(GlassShadow, BackgroundDark)),
                Color.White.copy(alpha = 0.2f),
                PurplePrimary
            )
            1 -> Triple(
                Brush.linearGradient(listOf(DarkCard, Color(0xFF2D2D44))),
                Color.White.copy(alpha = 0.15f),
                DarkCard
            )
            else -> Triple(
                Brush.linearGradient(listOf(WarningYellowDark, BackgroundDark)),
                Color.White.copy(alpha = 0.2f),
                PinkPrimary
            )
        }
    }
    
    // Get icon based on account type
    val accountIcon = when (account.type) {
        AccountType.CHECKING -> Icons.Outlined.AccountBalance
        AccountType.SAVINGS -> Icons.Outlined.Savings
        AccountType.CREDIT -> Icons.Outlined.CreditCard
    }
    
    val accountTypeLabel = when (account.type) {
        AccountType.CHECKING -> "Checking Account"
        AccountType.SAVINGS -> "Savings Account"
        AccountType.CREDIT -> "Credit Account"
    }
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = accentColor.copy(alpha = 0.3f)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardGradient)
        ) {
            // Decorative circles
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    radius = size.width * 0.35f,
                    center = Offset(size.width * 0.85f, size.height * 0.3f)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.07f),
                    radius = size.width * 0.25f,
                    center = Offset(size.width * 0.95f, size.height * 0.7f)
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Top row: Icon and type
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Account type badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(iconBgColor)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = accountIcon,
                            contentDescription = null,
                            tint = TextWhite,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = accountTypeLabel,
                            fontSize = 11.sp,
                            color = TextWhite,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    // Arrow icon
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "View details",
                            tint = TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Account name
                Text(
                    text = account.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Account number (masked)
                Text(
                    text = "•••• •••• •••• ${account.accountNumber.takeLast(4)}",
                    fontSize = 13.sp,
                    color = TextWhite.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Balance
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Available Balance",
                            fontSize = 11.sp,
                            color = TextWhite.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        val formattedBalance = String.format("%,.2f", kotlin.math.abs(account.balance))
                        Text(
                            text = "${if (account.balance < 0) "-" else ""}$$formattedBalance",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                    
                    // Status indicator
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (account.balance >= 0) SuccessGreen.copy(alpha = 0.3f)
                                else WarningOrange.copy(alpha = 0.3f)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (account.balance >= 0) SuccessGreen else WarningOrange)
                        )
                        Text(
                            text = if (account.balance >= 0) "Active" else "Overdraft",
                            fontSize = 10.sp,
                            color = TextWhite,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
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

// ====================== ADD ACCOUNT BUTTON ======================

@Composable
private fun AddAccountButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "buttonScale"
    )
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 4.dp,
        border = ButtonDefaults.outlinedButtonBorder
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Plus icon in purple circle
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(PurplePrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = PurplePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = "Add New Account",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = "Link a bank account or card",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(24.dp)
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
private fun EmptyAccountsState(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                imageVector = Icons.Outlined.AccountBalance,
                contentDescription = null,
                tint = PurplePrimary,
                modifier = Modifier.size(60.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "No accounts yet",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Add your first account to start\nmanaging your finances",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Add button
        Button(
            onClick = onAddClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PurplePrimary
            ),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 14.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Add Account",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ====================== PREVIEWS ======================

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountsScreenPreview() {
    MaterialTheme {
        // Preview with sample data
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGradient)
        ) {
            Column {
                AccountsHeader(
                    onBackClick = {},
                    onAddClick = {}
                )
                TotalBalanceCard(
                    totalBalance = 22770.50,
                    accountCount = 3,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PremiumAccountCardPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .background(BackgroundGradient)
                .padding(20.dp)
        ) {
            PremiumAccountCard(
                account = DisplayAccount(
                    id = "1",
                    name = "Main Account",
                    accountNumber = "FR76 1234 5678 9012 3456 7890 123",
                    type = AccountType.CHECKING,
                    balance = 8600.0,
                    cardColorIndex = 0
                ),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddAccountButtonPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .background(BackgroundGradient)
                .padding(20.dp)
        ) {
            AddAccountButton(onClick = {})
        }
    }
}
