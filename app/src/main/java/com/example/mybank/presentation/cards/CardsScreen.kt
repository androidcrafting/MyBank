package com.example.mybank.presentation.cards

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// ====================== PREMIUM COLORS ======================
private val PurplePrimary = Color(0xFF8B7FFF)
private val PurpleLight = Color(0xFFB8A9FF)
private val PurpleMedium = Color(0xFF9B8FFF)
private val PurpleVibrant = Color(0xFF7B6FEE)
private val PurpleCard = Color(0xFF8B7FFF)
private val PurpleCardLight = Color(0xFFB4A8FF)
private val TextPrimary = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF9CA3AF)
private val TextWhite = Color(0xFFFFFFFF)
private val CardBgGray = Color(0xFFF5F5F7)
private val IconBgLight = Color(0xFFF5F5F7)

// Premium background gradient (light purple to white)
private val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFE8E0FF),
        Color(0xFFF0ECFF),
        Color(0xFFF8F6FF),
        Color(0xFFFFFEFF)
    )
)

// Card gradient (purple tones matching the image)
private val PurpleCardGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF2E2650),
        Color(0xE6FFFFFF),

    ),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
)

// Black card gradient (dark premium look)
private val BlackCardGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF1A1A2E),
        Color(0xFF2D2D44),
        Color(0xFF16162A)
    ),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
)

// Card type enum for different card styles
enum class CardType {
    PURPLE,
    BLACK
}

// Data class for card information
data class BankCard(
    val id: Int,
    val label: String,
    val balance: Double,
    val lastFourDigits: String,
    val cardType: CardType
)

/**
 * CardsScreen - Premium Banking Cards Screen
 * 
 * This screen displays the user's debit/credit cards with a modern banking UI.
 * Features include:
 * - Premium card display with gradient background and decorative elements
 * - Quick action buttons (Add money, Freeze, Settings)
 * - Transaction placeholder
 * - Bottom navigation
 * 
 * @param onNavigateToHome Navigation callback for Home tab
 * @param onNavigateToTransfers Navigation callback for Transfers tab
 * @param onNavigateToMore Navigation callback for More tab
 * @param onAddCard Callback when add card button is pressed
 */
@Composable
fun CardsScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToTransfers: () -> Unit = {},
    onNavigateToMore: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onAddCard: () -> Unit = {}
) {
    // More menu state
    var showMoreMenu by remember { mutableStateOf(false) }
    
    // List of cards - Purple Salary card and Black Credit card
    val cards = remember {
        listOf(
            BankCard(
                id = 0,
                label = "Salary",
                balance = 2230.0,
                lastFourDigits = "6917",
                cardType = CardType.PURPLE
            ),
            BankCard(
                id = 1,
                label = "Credit card",
                balance = 5430.0,
                lastFourDigits = "4521",
                cardType = CardType.BLACK
            )
        )
    }
    
    // Track which card is selected (for freeze functionality)
    var selectedCardIndex by remember { mutableIntStateOf(0) }
    var isFrozen by remember { mutableStateOf(false) }
    
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    
    // Context for Toast messages
    val context = LocalContext.current
    
    // Trigger entrance animations
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGradient)
    ) {
        // Main scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp) // Space for bottom navigation
        ) {
            // ========== TOP HEADER ==========
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { -40 }
            ) {
                CardsHeader(
                    onAddClick = {
                        onAddCard()
                        Toast.makeText(context, "Add new card", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // ========== HORIZONTAL SCROLLABLE CARDS ==========
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600, 100)) + slideInVertically(tween(600, 100)) { -30 }
            ) {
                CardsCarousel(
                    cards = cards,
                    selectedCardIndex = selectedCardIndex,
                    isFrozen = isFrozen,
                    onCardSelected = { index ->
                        selectedCardIndex = index
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // ========== ACTION BUTTONS ==========
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600, 200)) + slideInVertically(tween(600, 200)) { -20 }
            ) {
                CardActionButtons(
                    isFrozen = isFrozen,
                    onAddMoney = {
                        Toast.makeText(context, "Add money", Toast.LENGTH_SHORT).show()
                    },
                    onFreeze = {
                        isFrozen = !isFrozen
                        val message = if (isFrozen) "Card frozen" else "Card unfrozen"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    },
                    onSettings = {
                        Toast.makeText(context, "Card settings", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // ========== NO TRANSACTION PLACEHOLDER ==========
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600, 300)) + slideInVertically(tween(600, 300)) { -20 }
            ) {
                NoTransactionPlaceholder(
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
        
        // ========== BOTTOM NAVIGATION BAR ==========
        CardsBottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedIndex = 1, // Cards tab is selected
            onHomeClick = onNavigateToHome,
            onCardsClick = { /* Already on Cards */ },
            onTransfersClick = onNavigateToTransfers,
            onMoreClick = { showMoreMenu = true }
        )
        
        // More Menu Overlay
        CardsMoreMenuOverlay(
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

// ====================== TOP HEADER ======================
/**
 * Header section with "Cards" title and add button
 */
@Composable
private fun CardsHeader(
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cards title
        Text(
            text = "Cards",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            letterSpacing = (-0.5).sp
        )
        
        // Add button
        IconButton(
            onClick = onAddClick,
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Card",
                tint = TextPrimary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// ====================== CARDS CAROUSEL ======================
/**
 * Horizontal scrollable carousel of bank cards
 */
@Composable
private fun CardsCarousel(
    cards: List<BankCard>,
    selectedCardIndex: Int,
    isFrozen: Boolean,
    onCardSelected: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    
    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(cards) { index, card ->
            PremiumDebitCard(
                card = card,
                isFrozen = isFrozen && index == selectedCardIndex,
                isSelected = index == selectedCardIndex,
                onClick = { onCardSelected(index) },
                modifier = Modifier.width(320.dp)
            )
        }
    }
}

// ====================== PREMIUM DEBIT CARD ======================
/**
 * Premium debit card with gradient background and decorative circles
 * Designed to match modern banking app aesthetics
 */
@Composable
private fun PremiumDebitCard(
    card: BankCard,
    isFrozen: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Card press animation
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardScale"
    )
    
    // Freeze overlay alpha animation
    val freezeAlpha by animateFloatAsState(
        targetValue = if (isFrozen) 0.5f else 0f,
        animationSpec = tween(300),
        label = "freezeAlpha"
    )
    
    // Animated balance display
    val animatedBalance by animateFloatAsState(
        targetValue = card.balance.toFloat(),
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "balanceAnimation"
    )
    
    // Select gradient based on card type
    val cardGradient = when (card.cardType) {
        CardType.PURPLE -> PurpleCardGradient
        CardType.BLACK -> BlackCardGradient
    }
    
    // Shadow color based on card type
    val shadowColor = when (card.cardType) {
        CardType.PURPLE -> PurplePrimary.copy(alpha = 0.4f)
        CardType.BLACK -> Color.Black.copy(alpha = 0.5f)
    }
    
    // Circle overlay alpha based on card type (darker for black card)
    val circleAlpha = when (card.cardType) {
        CardType.PURPLE -> 0.12f
        CardType.BLACK -> 0.08f
    }
    
    Box(
        modifier = modifier
            .aspectRatio(1.7f) // Standard card aspect ratio
            .scale(scale)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = shadowColor,
                ambientColor = shadowColor.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = !isPressed
                onClick()
            }
    ) {
        // Card background with gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(cardGradient)
        )
        
        // Decorative circles using Canvas
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val width = size.width
            val height = size.height
            
            // Large circle - top right area
            drawCircle(
                color = Color.White.copy(alpha = circleAlpha),
                radius = width * 0.45f,
                center = Offset(width * 0.85f, height * 0.2f)
            )
            
            // Medium circle - overlapping
            drawCircle(
                color = Color.White.copy(alpha = circleAlpha * 0.7f),
                radius = width * 0.35f,
                center = Offset(width * 0.7f, height * 0.35f)
            )
            
            // Small circle - accent
            drawCircle(
                color = Color.White.copy(alpha = circleAlpha * 0.8f),
                radius = width * 0.25f,
                center = Offset(width * 0.95f, height * 0.5f)
            )
            
            // Additional decorative circle
            drawCircle(
                color = Color.White.copy(alpha = circleAlpha * 0.5f),
                radius = width * 0.3f,
                center = Offset(width * 0.6f, height * 0.1f)
            )
        }
        
        // Card content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section - Label
            Column {
                Text(
                    text = card.label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextWhite.copy(alpha = 0.9f),
                    letterSpacing = 0.5.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Balance - formatted with space: $2 230
                val formattedBalance = String.format("%,.0f", animatedBalance).replace(",", " ")
                Text(
                    text = "$$formattedBalance",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    letterSpacing = (-1).sp
                )
            }
            
            // Bottom section - Card number and VISA
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Card chip icon and number
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hidden card icon (chip/contactless symbol)
                    Icon(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = "Hidden",
                        tint = TextWhite.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                    
                    // Card number
                    Text(
                        text = "** ${card.lastFourDigits}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextWhite.copy(alpha = 0.8f),
                        letterSpacing = 1.sp
                    )
                }
                
                // VISA logo
                Text(
                    text = "VISA",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    letterSpacing = 2.sp
                )
            }
        }
        
        // Freeze overlay
        if (isFrozen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = freezeAlpha))
                    .clip(RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AcUnit,
                        contentDescription = "Frozen",
                        tint = TextWhite,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Card Frozen",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                }
            }
        }
    }
}

// ====================== ACTION BUTTONS ======================
/**
 * Three circular action buttons: Add money, Freeze, Settings
 */
@Composable
private fun CardActionButtons(
    isFrozen: Boolean,
    onAddMoney: () -> Unit,
    onFreeze: () -> Unit,
    onSettings: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
    ) {
        // Add money button
        ActionButton(
            icon = Icons.Default.Add,
            label = "Add money",
            onClick = onAddMoney
        )
        
        // Freeze button (toggle state)
        ActionButton(
            icon = if (isFrozen) Icons.Default.PlayArrow else Icons.Default.Pause,
            label = if (isFrozen) "Unfreeze" else "Freeze",
            onClick = onFreeze,
            isActive = isFrozen
        )
        
        // Settings button
        ActionButton(
            icon = Icons.Outlined.Settings,
            label = "Settings",
            onClick = onSettings
        )
    }
}

/**
 * Individual circular action button with icon and label
 */
@Composable
private fun ActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    isActive: Boolean = false
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "buttonScale"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
                // Reset press state after animation
            }
    ) {
        // Circular button with border
        Surface(
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            color = if (isActive) PurplePrimary.copy(alpha = 0.1f) else Color.White,
            border = ButtonDefaults.outlinedButtonBorder,
            shadowElevation = 2.dp
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isActive) PurplePrimary else TextPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(10.dp))
        
        // Label
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
    }
    
    // Reset press state
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

// ====================== NO TRANSACTION PLACEHOLDER ======================
/**
 * Placeholder section showing "No transaction yet"
 */
@Composable
private fun NoTransactionPlaceholder(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CardBgGray,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Card icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CreditCard,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(14.dp))
            
            // Text
            Text(
                text = "No transaction yet",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = TextSecondary
            )
        }
    }
}

// ====================== MORE MENU OVERLAY ======================
@Composable
private fun CardsMoreMenuOverlay(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSettingsClick: () -> Unit,
    onAccountsClick: () -> Unit
) {
    val overlayAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(300),
        label = "overlayAlpha"
    )
    
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onDismiss() }
            )
            
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
                        Text(
                            text = "Quick Actions",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        CardsMoreMenuItem(
                            icon = Icons.Outlined.Settings,
                            title = "Settings",
                            subtitle = "App preferences & security",
                            iconBgColor = Color(0xFFE8E0FF),
                            iconColor = PurplePrimary,
                            onClick = onSettingsClick
                        )
                        
                        HorizontalDivider(
                            color = Color(0xFFE5E7EB),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        
                        CardsMoreMenuItem(
                            icon = Icons.Outlined.AccountBalance,
                            title = "Accounts",
                            subtitle = "View all your accounts",
                            iconBgColor = Color(0xFFE5F9E7),
                            iconColor = Color(0xFF00C566),
                            onClick = onAccountsClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardsMoreMenuItem(
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
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(24.dp)
        )
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

// ====================== BOTTOM NAVIGATION BAR ======================
/**
 * Premium bottom navigation bar with animated selection
 */
@Composable
private fun CardsBottomNavigationBar(
    modifier: Modifier = Modifier,
    selectedIndex: Int = 1,
    onHomeClick: () -> Unit,
    onCardsClick: () -> Unit,
    onTransfersClick: () -> Unit,
    onMoreClick: () -> Unit
) {
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
                onClick = onHomeClick
            )
            BottomNavItem(
                icon = Icons.Outlined.Layers,
                selectedIcon = Icons.Filled.Layers,
                label = "Cards",
                isSelected = selectedIndex == 1,
                onClick = onCardsClick
            )
            BottomNavItem(
                icon = Icons.Outlined.CompareArrows,
                selectedIcon = Icons.Filled.CompareArrows,
                label = "Transfers",
                isSelected = selectedIndex == 2,
                onClick = onTransfersClick
            )
            BottomNavItem(
                icon = Icons.Outlined.Apps,
                selectedIcon = Icons.Filled.Apps,
                label = "More",
                isSelected = selectedIndex == 3,
                onClick = onMoreClick
            )
        }
    }
}

/**
 * Individual bottom navigation item with animation and indicator dot
 */
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
        animationSpec = tween(200),
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
            tint = if (isSelected) PurplePrimary else TextSecondary,
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
            color = if (isSelected) PurplePrimary else TextSecondary,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
        
        // Purple indicator dot for selected item
        Box(
            modifier = Modifier
                .size(5.dp)
                .graphicsLayer { alpha = indicatorAlpha }
                .clip(CircleShape)
                .background(PurplePrimary)
        )
    }
}

// ====================== PREVIEW ======================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CardsScreenPreview() {
    MaterialTheme {
        CardsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PremiumDebitCardPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundGradient)
                .padding(20.dp)
        ) {
            PremiumDebitCard(
                card = BankCard(
                    id = 0,
                    label = "Salary",
                    balance = 2230.0,
                    lastFourDigits = "6917",
                    cardType = CardType.PURPLE
                ),
                isFrozen = false,
                isSelected = true,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BlackDebitCardPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundGradient)
                .padding(20.dp)
        ) {
            PremiumDebitCard(
                card = BankCard(
                    id = 1,
                    label = "Credit card",
                    balance = 5430.0,
                    lastFourDigits = "4521",
                    cardType = CardType.BLACK
                ),
                isFrozen = false,
                isSelected = true,
                onClick = {}
            )
        }
    }
}
