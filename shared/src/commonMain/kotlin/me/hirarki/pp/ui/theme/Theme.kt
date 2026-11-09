package me.hirarki.pp.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object PortfolioColors {
    val Background = Color(0xFF0A0A0A)
    val Surface = Color(0xFF1A1A1A)
    val SurfaceVariant = Color(0xFF2A2A2A)
    
    val Primary = Color(0xFF06B6D4)
    val PrimaryVariant = Color(0xFF0891B2)
    val Secondary = Color(0xFFEC4899)
    val SecondaryVariant = Color(0xFFDB2777)
    
    val TextPrimary = Color(0xFFE5E5E5)
    val TextSecondary = Color(0xFFB3B3B3)
    val TextMuted = Color(0xFF808080)
    
    val Border = Color(0xFF333333)
    val BorderLight = Color(0xFF404040)
    
    val Success = Color(0xFF10B981)
    val Warning = Color(0xFFF59E0B)
    val Error = Color(0xFFEF4444)
}

val PortfolioDarkColorScheme = darkColorScheme(
    primary = PortfolioColors.Primary,
    secondary = PortfolioColors.Secondary,
    background = PortfolioColors.Background,
    surface = PortfolioColors.Surface,
    surfaceVariant = PortfolioColors.SurfaceVariant,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = PortfolioColors.TextPrimary,
    onSurface = PortfolioColors.TextPrimary,
    onSurfaceVariant = PortfolioColors.TextSecondary,
    outline = PortfolioColors.Border,
    error = PortfolioColors.Error
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
    val small = 4.dp
    val medium = 8.dp
    val large = 12.dp
    val xl = 16.dp
}

object PortfolioBreakpoints {
    const val Mobile = 360
    const val MobileLarge = 390
    const val Tablet = 768
    const val Desktop = 1024
    const val DesktopLarge = 1440
}
