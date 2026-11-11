package me.hirarki.pp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object PortfolioColors {
    val Background = Color(0xFF070B14)
    val BackgroundAlt = Color(0xFF0F172A)
    val Surface = Color(0xFF111827)
    val SurfaceElevated = Color(0xFF172033)
    val SurfaceSoft = Color(0xFF1C2438)

    val Primary = Color(0xFF22D3EE)
    val PrimaryStrong = Color(0xFF67E8F9)
    val Secondary = Color(0xFFF472B6)
    val SecondaryStrong = Color(0xFFF9A8D4)

    val TextPrimary = Color(0xFFE5EEF7)
    val TextSecondary = Color(0xFFB7C4D8)
    val TextMuted = Color(0xFF8EA3C3)

    val Border = Color(0xFF2C3A55)
    val BorderLight = Color(0xFF405674)

    val Success = Color(0xFF34D399)
    val Warning = Color(0xFFFBBF24)
    val Error = Color(0xFFF87171)

    val GlowCyan = Color(0xFF22D3EE)
    val GlowPink = Color(0xFFF472B6)
}

val PortfolioDarkColorScheme = darkColorScheme(
    primary = PortfolioColors.Primary,
    secondary = PortfolioColors.Secondary,
    background = PortfolioColors.Background,
    surface = PortfolioColors.Surface,
    surfaceVariant = PortfolioColors.SurfaceSoft,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = PortfolioColors.TextPrimary,
    onSurface = PortfolioColors.TextPrimary,
    onSurfaceVariant = PortfolioColors.TextSecondary,
    outline = PortfolioColors.Border,
    error = PortfolioColors.Error
)

val PortfolioTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.04).sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.03).sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.02).sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)

object PortfolioSpacing {
    val xs = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
    val xxxl = 64.dp
}

object PortfolioRadius {
    val small = 6.dp
    val medium = 12.dp
    val large = 18.dp
    val xl = 24.dp
    val full = 999.dp
}

object PortfolioBreakpoints {
    const val Mobile = 360
    const val MobileLarge = 390
    const val Tablet = 768
    const val Desktop = 1024
    const val DesktopLarge = 1440
}

object PortfolioTokens {
    val pageMaxWidth = 1200.dp
    val contentGlow = Brush.linearGradient(
        colors = listOf(
            PortfolioColors.GlowCyan.copy(alpha = 0.18f),
            PortfolioColors.GlowPink.copy(alpha = 0.12f),
            Color.Transparent
        )
    )
}

