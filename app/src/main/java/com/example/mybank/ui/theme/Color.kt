package com.example.mybank.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ====================== PREMIUM BANKING PALETTE ======================

// Primary Purple/Lavender (Main brand color)
val PurplePrimary = Color(0xFF8B7FFF)
val PurpleLight = Color(0xFFA99FFF)
val PurpleMedium = Color(0xFF9B8FFF)
val PurpleDark = Color(0xFF6B5FDE)
val PurpleDeep = Color(0xFF5B4FCE)

// Secondary Pink/Magenta
val PinkPrimary = Color(0xFFFF6FD8)
val PinkLight = Color(0xFFFF8FE8)
val PinkMedium = Color(0xFFFF5FC8)
val PinkDark = Color(0xFFEF4FB8)
val PinkVibrant = Color(0xFFFF69B4)

// Accent Blue/Cyan
val BlueCyan = Color(0xFF4FD8FF)
val BlueLight = Color(0xFF6FE8FF)
val BlueMedium = Color(0xFF3FC8EF)
val BlueTeal = Color(0xFF00D9D5)

// Dark Card Gradients
val DarkPurple = Color(0xFF2E2650)
val DarkBlue = Color(0xFF1E1E2E)
val DarkCard = Color(0xFF1A1A2E)

// Background Gradient Colors (Light purple -> Blue -> White)
val BgGradientStart = Color(0xFFE8E0FF)     // Light purple top
val BgGradientMid1 = Color(0xFFF0ECFF)      // Softer lavender
val BgGradientMid2 = Color(0xFFF8F6FF)      // Very light purple
val BgGradientEnd = Color(0xFFFFFEFF)       // Almost white bottom
val BgWhite = Color(0xFFFAFAFC)

// Text Colors
val TextPrimary = Color(0xFF1A1A2E)         // Deep dark blue-black
val TextSecondary = Color(0xFF9CA3AF)       // Soft gray
val TextMuted = Color(0xFFB8BCC4)           // Light gray
val TextWhite = Color(0xFFFFFFFF)

// Status Colors
val SuccessGreen = Color(0xFF00C566)        // Vibrant green for income
val SuccessGreenLight = Color(0xFF00E878)
val SuccessGreenDark = Color(0xFF00B05C)
val ErrorRed = Color(0xFFFF4757)            // Soft red for expenses
val ErrorRedLight = Color(0xFFFF6B7A)
val ErrorRedDark = Color(0xFFEF3747)

// Icon Background Colors
val IconPurple = Color(0xFF8B7FFF)
val IconCyan = Color(0xFF4FD8FF)
val IconBlue = Color(0xFF5B8DEF)
val IconPink = Color(0xFFFF6FD8)
val IconBgLight = Color(0xFFF5F5F7)         // Light gray for icon backgrounds

// Card Surface Colors
val CardWhite = Color(0xFFFFFFFF)
val CardBgLight = Color(0xFFFAFAFC)
val CardShadow = Color(0x0D000000)          // Very subtle shadow

// ====================== GRADIENT BRUSHES ======================

val CardGradientPurple = Brush.linearGradient(
    colors = listOf(
        Color(0xFF8B7FFF),
        Color(0xFFA99FFF)
    )
)

val CardGradientDark = Brush.linearGradient(
    colors = listOf(
        Color(0xFF2E2650),
        Color(0xFF1A1A2E)
    )
)

val CardGradientPink = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFF6FD8),
        Color(0xFFFF8FE8)
    )
)

val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        BgGradientStart,
        BgGradientMid1,
        BgGradientMid2,
        BgGradientEnd
    )
)

val AvatarGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF7B6FEE),
        Color(0xFF9B8FFF)
    )
)

// ====================== GLASSMORPHISM ======================
val GlassSurface = Color(0xE6FFFFFF)        // 90% white
val GlassStroke = Color(0x33FFFFFF)         // 20% white
val GlassShadow = Color(0x1A8B7FFF)         // Purple tinted shadow

// ====================== LEGACY COMPATIBILITY ======================
val PrimaryBlue = PurplePrimary
val PrimaryBlueLight = PurpleLight
val PrimaryBlueDark = PurpleDark

val WarningYellow = Color(0xFFFFB800)
val WarningYellowLight = Color(0xFFFFD84D)
val WarningYellowDark = Color(0xFFFFA300)

val BackgroundLight = BgWhite
val SurfaceLight = Color(0xFFFFFFFF)
val TextPrimaryLight = TextPrimary
val TextSecondaryLight = TextSecondary

val BackgroundDark = Color(0xFF121212)
val SurfaceDark = Color(0xFF1E1E1E)
val TextPrimaryDark = Color(0xFFFFFFFF)
val TextSecondaryDark = Color(0xFFB0B0B0)

// Shadow Colors
val ShadowLight = Color(0x1A8B7FFF)
val ShadowMedium = Color(0x338B7FFF)
