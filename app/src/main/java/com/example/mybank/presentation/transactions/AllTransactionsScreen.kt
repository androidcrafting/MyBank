package com.example.mybank.presentation.transactions

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.mybank.ui.theme.TextPrimaryDark
import com.example.mybank.ui.theme.WarningYellowDark
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

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
private val CardBgWhite = Color(0xFFFAFAFC)
private val IconBgLight = Color(0xFFF5F5F7)
private val SearchBgColor = Color(0xFFF3F4F6)
private val DividerColor = Color(0xFFE5E7EB)
private val BlueCyan = Color(0xFF4FD8FF)

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
 * Filter chip options for transactions
 */
enum class TransactionFilter(val displayName: String) {
    ALL("All"),
    INCOME("Income"),
    EXPENSE("Expense"),
    TRANSFER("Transfer"),
    SHOPPING("Shopping"),
    FOOD("Food & Drinks"),
    TRANSPORT("Transport")
}

/**
 * Data class representing a transaction for display
 */
data class DisplayTransaction(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val type: String, // "income", "expense", "transfer"
    val category: String,
    val dateTime: String,
    val date: String,
    val avatarUrl: String? = null,
    val avatarInitials: String,
    val avatarColor: Color = WarningYellowDark,
    val icon: ImageVector = Icons.Default.Receipt
)

// ====================== MAIN SCREEN ======================

/**
 * AllTransactionsScreen - Premium Banking All Transactions Screen
 * 
 * This screen displays all user transactions with a modern, elegant UI.
 * Features include:
 * - Search functionality
 * - Filter chips for quick filtering
 * - Transactions grouped by date
 * - Beautiful animations and transitions
 * - Professional avatars and icons
 * 
 * @param onNavigateBack Navigation callback for back action
 * @param onTransactionClick Callback when a transaction is clicked
 */
@Composable
fun AllTransactionsScreen(
    onNavigateBack: () -> Unit = {},
    onTransactionClick: (String) -> Unit = {}
) {
    // State for search text
    var searchText by remember { mutableStateOf("") }
    
    // State for selected filter
    var selectedFilter by remember { mutableStateOf(TransactionFilter.ALL) }
    
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    
    // Context for Toast messages
    val context = LocalContext.current
    
    // Sample transactions data grouped by date
    val allTransactions = remember {
        listOf(
            // Today
            DisplayTransaction(
                id = "1",
                title = "Supermarket",
                subtitle = "Grocery shopping",
                amount = -40.0,
                type = "expense",
                category = "shopping",
                dateTime = "16:40",
                date = "Today",
                avatarInitials = "SM",
                avatarColor = Color(0xFFFFE5E5),
                icon = Icons.Outlined.ShoppingCart
            ),
            DisplayTransaction(
                id = "2",
                title = "Kapital Bank",
                subtitle = "Salary deposit",
                amount = 2500.0,
                type = "income",
                category = "transfer",
                dateTime = "12:30",
                date = "Today",
                avatarUrl = "https://randomuser.me/api/portraits/men/32.jpg",
                avatarInitials = "KB",
                avatarColor = Color(0xFFE5F9E7),
                icon = Icons.Outlined.AccountBalance
            ),
            DisplayTransaction(
                id = "3",
                title = "Netflix",
                subtitle = "Monthly subscription",
                amount = -15.99,
                type = "expense",
                category = "entertainment",
                dateTime = "10:15",
                date = "Today",
                avatarInitials = "NF",
                avatarColor = Color(0xFFFFE5E5),
                icon = Icons.Outlined.PlayCircle
            ),
            // Yesterday
            DisplayTransaction(
                id = "4",
                title = "Anna Lopez",
                subtitle = "Money transfer",
                amount = 125.0,
                type = "income",
                category = "transfer",
                dateTime = "18:20",
                date = "Yesterday",
                avatarUrl = "https://randomuser.me/api/portraits/women/44.jpg",
                avatarInitials = "AL",
                avatarColor = Color(0xFFE5F9E7),
                icon = Icons.Outlined.Person
            ),
            DisplayTransaction(
                id = "5",
                title = "Uber",
                subtitle = "Ride to work",
                amount = -12.50,
                type = "expense",
                category = "transport",
                dateTime = "09:22",
                date = "Yesterday",
                avatarInitials = "UB",
                avatarColor = Color(0xFFFFE5E5),
                icon = Icons.Outlined.DirectionsCar
            ),
            DisplayTransaction(
                id = "6",
                title = "Starbucks",
                subtitle = "Coffee & snack",
                amount = -8.75,
                type = "expense",
                category = "food",
                dateTime = "08:45",
                date = "Yesterday",
                avatarInitials = "SB",
                avatarColor = Color(0xFFFFE5E5),
                icon = Icons.Outlined.LocalCafe
            ),
            // Jan 2, 2026
            DisplayTransaction(
                id = "7",
                title = "John Smith",
                subtitle = "Debt repayment",
                amount = 500.0,
                type = "income",
                category = "transfer",
                dateTime = "14:30",
                date = "Jan 2, 2026",
                avatarUrl = "https://randomuser.me/api/portraits/men/75.jpg",
                avatarInitials = "JS",
                avatarColor = Color(0xFFE5F9E7),
                icon = Icons.Outlined.Person
            ),
            DisplayTransaction(
                id = "8",
                title = "Amazon",
                subtitle = "Electronics purchase",
                amount = -299.99,
                type = "expense",
                category = "shopping",
                dateTime = "11:20",
                date = "Jan 2, 2026",
                avatarInitials = "AM",
                avatarColor = Color(0xFFFFE5E5),
                icon = Icons.Outlined.ShoppingBag
            ),
            DisplayTransaction(
                id = "9",
                title = "Electricity Bill",
                subtitle = "Monthly bill",
                amount = -85.00,
                type = "expense",
                category = "bills",
                dateTime = "09:00",
                date = "Jan 2, 2026",
                avatarInitials = "EB",
                avatarColor = Color(0xFFFFE5E5),
                icon = Icons.Outlined.ElectricBolt
            ),
            // Jan 1, 2026
            DisplayTransaction(
                id = "10",
                title = "New Year Bonus",
                subtitle = "Company bonus",
                amount = 1000.0,
                type = "income",
                category = "salary",
                dateTime = "00:01",
                date = "Jan 1, 2026",
                avatarInitials = "NY",
                avatarColor = Color(0xFFE5F9E7),
                icon = Icons.Outlined.Celebration
            ),
            DisplayTransaction(
                id = "11",
                title = "Restaurant",
                subtitle = "New Year dinner",
                amount = -150.00,
                type = "expense",
                category = "food",
                dateTime = "21:30",
                date = "Jan 1, 2026",
                avatarInitials = "RS",
                avatarColor = Color(0xFFFFE5E5),
                icon = Icons.Outlined.Restaurant
            )
        )
    }
    
    // Filter transactions based on search and filter
    val filteredTransactions = remember(searchText, selectedFilter, allTransactions) {
        allTransactions.filter { transaction ->
            val matchesSearch = searchText.isEmpty() || 
                transaction.title.contains(searchText, ignoreCase = true) ||
                transaction.subtitle.contains(searchText, ignoreCase = true)
            
            val matchesFilter = when (selectedFilter) {
                TransactionFilter.ALL -> true
                TransactionFilter.INCOME -> transaction.amount > 0
                TransactionFilter.EXPENSE -> transaction.amount < 0
                TransactionFilter.TRANSFER -> transaction.category == "transfer"
                TransactionFilter.SHOPPING -> transaction.category == "shopping"
                TransactionFilter.FOOD -> transaction.category == "food"
                TransactionFilter.TRANSPORT -> transaction.category == "transport"
            }
            
            matchesSearch && matchesFilter
        }
    }
    
    // Group transactions by date
    val groupedTransactions = remember(filteredTransactions) {
        filteredTransactions.groupBy { it.date }
    }
    
    // Calculate totals
    val totalIncome = remember(filteredTransactions) {
        filteredTransactions.filter { it.amount > 0 }.sumOf { it.amount }
    }
    val totalExpense = remember(filteredTransactions) {
        filteredTransactions.filter { it.amount < 0 }.sumOf { kotlin.math.abs(it.amount) }
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
            .statusBarsPadding() // Disable edge-to-edge for status bar
            .navigationBarsPadding() // Disable edge-to-edge for navigation bar
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
        ) {
            // ========== HEADER ==========
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { -40 }
                ) {
                    TransactionsHeader(
                        onBackClick = onNavigateBack
                    )
                }
            }
            
            // ========== SUMMARY CARDS ==========
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, 100)) + slideInVertically(tween(600, 100)) { -30 }
                ) {
                    SummaryCards(
                        totalIncome = totalIncome,
                        totalExpense = totalExpense,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                    )
                }
            }
            
            // ========== SEARCH BAR ==========
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, 150)) + slideInVertically(tween(600, 150)) { -25 }
                ) {
                    SearchBar(
                        searchText = searchText,
                        onSearchTextChange = { searchText = it },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
            }
            
            // ========== FILTER CHIPS ==========
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, 200)) + slideInVertically(tween(600, 200)) { -20 }
                ) {
                    FilterChips(
                        selectedFilter = selectedFilter,
                        onFilterSelected = { selectedFilter = it },
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            }
            
            // ========== TRANSACTIONS LIST ==========
            groupedTransactions.forEach { (date, transactions) ->
                // Date header
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(tween(600, 250)) + slideInVertically(tween(600, 250)) { 20 }
                    ) {
                        DateHeader(
                            date = date,
                            transactionCount = transactions.size,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                        )
                    }
                }
                
                // Transactions for this date
                itemsIndexed(transactions) { index, transaction ->
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(tween(600, 300 + index * 50)) + slideInVertically(tween(600, 300 + index * 50)) { 30 }
                    ) {
                        PremiumTransactionItem(
                            transaction = transaction,
                            onClick = {
                                onTransactionClick(transaction.id)
                                Toast.makeText(context, "Transaction: ${transaction.title}", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            
            // Empty state
            if (groupedTransactions.isEmpty()) {
                item {
                    EmptyState(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp)
                    )
                }
            }
        }
    }
}

// ====================== HEADER ======================

@Composable
private fun TransactionsHeader(
    onBackClick: () -> Unit
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
            text = "All Transactions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            letterSpacing = (-0.5).sp
        )
        
        // Filter/Sort button
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(CardBgWhite)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { /* Sort action */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.FilterList,
                contentDescription = "Filter",
                tint = TextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// ====================== SUMMARY CARDS ======================

@Composable
private fun SummaryCards(
    totalIncome: Double,
    totalExpense: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Income card
        SummaryCard(
            title = "Income",
            amount = totalIncome,
            isIncome = true,
            icon = Icons.Outlined.TrendingUp,
            modifier = Modifier.weight(1f)
        )
        
        // Expense card
        SummaryCard(
            title = "Expense",
            amount = totalExpense,
            isIncome = false,
            icon = Icons.Outlined.TrendingDown,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SummaryCard(
    title: String,
    amount: Double,
    isIncome: Boolean,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isIncome) Color(0xFFE5F9E7) else Color(0xFFFFE5E5)
    val iconColor = if (isIncome) SuccessGreen else ErrorRed
    val textColor = if (isIncome) SuccessGreen else ErrorRed
    
    Surface(
        modifier = modifier
            .height(100.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = if (isIncome) SuccessGreen.copy(alpha = 0.2f) else ErrorRed.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )
                Text(
                    text = "${if (isIncome) "+" else "-"}$${String.format("%,.2f", amount)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
            
            // Icon container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// ====================== SEARCH BAR ======================

@Composable
private fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (isFocused) 2.dp else 0.dp,
                color = if (isFocused) PurplePrimary else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        color = SearchBgColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = TextSecondary,
                modifier = Modifier.size(22.dp)
            )
            
            Box(modifier = Modifier.weight(1f)) {
                if (searchText.isEmpty()) {
                    Text(
                        text = "Search transactions...",
                        fontSize = 15.sp,
                        color = TextSecondary
                    )
                }
                androidx.compose.foundation.text.BasicTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 15.sp,
                        color = TextPrimary
                    ),
                    singleLine = true
                )
            }
            
            if (searchText.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = TextSecondary,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onSearchTextChange("") }
                )
            }
        }
    }
}

// ====================== FILTER CHIPS ======================

@Composable
private fun FilterChips(
    selectedFilter: TransactionFilter,
    onFilterSelected: (TransactionFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(TransactionFilter.entries) { filter ->
            FilterChip(
                filter = filter,
                isSelected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

@Composable
private fun FilterChip(
    filter: TransactionFilter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "chipScale"
    )
    
    Surface(
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) PurplePrimary else Color.White,
        shadowElevation = if (isSelected) 8.dp else 2.dp,
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(
            1.dp,
            DividerColor
        ) else null
    ) {
        Text(
            text = filter.displayName,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (isSelected) TextWhite else TextPrimary
        )
    }
}

// ====================== DATE HEADER ======================

@Composable
private fun DateHeader(
    date: String,
    transactionCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = date,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = PurplePrimary.copy(alpha = 0.1f)
        ) {
            Text(
                text = "$transactionCount transactions",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = PurplePrimary
            )
        }
    }
}

// ====================== TRANSACTION ITEM ======================

@Composable
private fun PremiumTransactionItem(
    transaction: DisplayTransaction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "transactionScale"
    )
    
    val isIncome = transaction.amount > 0
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.08f)
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Avatar with professional image or icon
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = Color.Black.copy(alpha = 0.1f)
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .background(transaction.avatarColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (transaction.avatarUrl != null) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(transaction.avatarUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(transaction.avatarColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = transaction.icon,
                                        contentDescription = null,
                                        tint = if (isIncome) SuccessGreen else PurpleDark,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            error = {
                                Icon(
                                    imageVector = transaction.icon,
                                    contentDescription = null,
                                    tint = if (isIncome) SuccessGreen else PurpleDark,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        )
                    } else {
                        Icon(
                            imageVector = transaction.icon,
                            contentDescription = null,
                            tint = if (isIncome) SuccessGreen else TextPrimaryDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                // Transaction details
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = transaction.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = transaction.subtitle,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = transaction.dateTime,
                        fontSize = 11.sp,
                        color = TextSecondary.copy(alpha = 0.7f)
                    )
                }
            }
            
            // Amount
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${if (isIncome) "+" else ""}$${String.format("%.2f", kotlin.math.abs(transaction.amount))}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isIncome) SuccessGreen else TextPrimary
                )
                
                // Status badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isIncome) SuccessGreen.copy(alpha = 0.1f) else IconBgLight
                ) {
                    Text(
                        text = if (isIncome) "received" else "payment",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isIncome) SuccessGreen else TextSecondary
                    )
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

// ====================== EMPTY STATE ======================

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
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
                imageVector = Icons.Outlined.SearchOff,
                contentDescription = null,
                tint = PurplePrimary,
                modifier = Modifier.size(60.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "No transactions found",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Try adjusting your search or filters\nto find what you're looking for",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

// ====================== PREVIEWS ======================

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AllTransactionsScreenPreview() {
    MaterialTheme {
        AllTransactionsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryCardsPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .background(BackgroundGradient)
                .padding(20.dp)
        ) {
            SummaryCards(
                totalIncome = 4125.0,
                totalExpense = 612.23
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PremiumTransactionItemPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .background(BackgroundGradient)
                .padding(20.dp)
        ) {
            PremiumTransactionItem(
                transaction = DisplayTransaction(
                    id = "1",
                    title = "Supermarket",
                    subtitle = "Grocery shopping",
                    amount = -40.0,
                    type = "expense",
                    category = "shopping",
                    dateTime = "16:40",
                    date = "Today",
                    avatarInitials = "SM",
                    icon = Icons.Outlined.ShoppingCart
                ),
                onClick = {}
            )
        }
    }
}
