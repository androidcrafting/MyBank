package com.example.mybank.presentation.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybank.R
import com.example.mybank.ui.theme.GlassShadow
import com.example.mybank.ui.theme.WarningYellow
import kotlinx.coroutines.delay

// ====================== PREMIUM COLORS ======================
private val PurplePrimary = Color(0xFF8B7FFF)
private val PurpleLight = Color(0xFFA99FFF)
private val PurpleMedium = Color(0xFF9B8FFF)
private val PinkPrimary = Color(0xFFFF6FD8)
private val PinkLight = Color(0xFFFF8FE8)
private val TextPrimary = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF6B7280)
private val TextWhite = Color(0xFFFFFFFF)

private val SplashGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFFFD84D),
        Color(0x1A8B7FFF),
        Color(0xFFF5F1FF),
        Color(0xFFB0B0B0)
    )
)

@Composable

fun SplashScreen(
    isAuthenticated: Boolean,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isVisible = true
        delay(300)
        showContent = true
        delay(2700)
        if (isAuthenticated) {
            onNavigateToHome()
        } else {
            onNavigateToLogin()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashGradient)
    ) {
        // Animated background blobs
        SplashBackgroundBlobs()
        
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo with glow effect
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(800)) + scaleIn(
                    initialScale = 0.8f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            ) {
                PremiumAppLogo()
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // App name
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(600, 200)) + slideInVertically(tween(600, 200)) { 30 }
            ) {
                Text(
                    text = "MyBank",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    letterSpacing = (-2).sp
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Tagline
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(600, 400)) + slideInVertically(tween(600, 400)) { 20 }
            ) {
                Text(
                    text = "Smart banking in your pocket",
                    fontSize = 16.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(60.dp))
            
            // Loading indicator
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(600, 600))
            ) {
                LoadingDots()
            }
        }
        
        // Bottom text
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(tween(600, 800)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Secure & Trusted",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "256-bit encrypted",
                    fontSize = 11.sp,
                    color = TextSecondary.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// ====================== SPLASH BACKGROUND BLOBS ======================
@Composable
private fun SplashBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "splash_bg")
    
    val blob1Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob1"
    )
    
    val blob2Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob2"
    )
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Purple blob (top left)
        Box(
            modifier = Modifier
                .size(350.dp)
                .offset(
                    x = (-80 + blob1Offset * 60).dp,
                    y = (80 + blob1Offset * 40).dp
                )
                .blur(120.dp)
                .alpha(0.5f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            PurpleLight,
                            PurpleLight.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )
        
        // Pink blob (bottom right)
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(
                    x = (200 - blob2Offset * 50).dp,
                    y = (500 + blob2Offset * 30).dp
                )
                .blur(100.dp)
                .alpha(0.4f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            PinkLight,
                            PinkLight.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )
        
        // Small accent blob
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(
                    x = (250 + blob1Offset * 30).dp,
                    y = (150 - blob2Offset * 40).dp
                )
                .blur(80.dp)
                .alpha(0.3f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            PurpleMedium,
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )
    }
}

// ====================== PREMIUM APP LOGO WITH ICON ======================
@Composable
private fun PremiumAppLogo() {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_orange")
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(180.dp)
    ) {
        // Outer glow
        Box(
            modifier = Modifier
                .size(170.dp)
                .scale(pulseScale)
                .alpha(glowAlpha)
                .blur(35.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            WarningYellow,
                            GlassShadow.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )
        
        // Main logo container with shadow - white background, no black
        Box(
            modifier = Modifier
                .size(140.dp)
                .shadow(
                    elevation = 30.dp,
                    shape = CircleShape,
                    spotColor = WarningYellow.copy(alpha = 0.5f)
                )
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            // App Icon from drawable - fits inside white container
            Image(
                painter = painterResource(id = R.drawable.logo_orange),
                contentDescription = "MyBank Logo",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

// ====================== LOADING DOTS ======================
@Composable
private fun LoadingDots() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            val delay = index * 200
            
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.6f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1000,
                        delayMillis = delay,
                        easing = EaseInOutSine
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_scale_$index"
            )
            
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.4f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1000,
                        delayMillis = delay,
                        easing = EaseInOutSine
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_alpha_$index"
            )
            
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .scale(scale)
                    .alpha(alpha)
                    .shadow(4.dp, CircleShape, spotColor = WarningYellow.copy(alpha = 0.3f))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                WarningYellow,
                                GlassShadow
                            )
                        ),
                        CircleShape
                    )
            )
        }
    }
}
