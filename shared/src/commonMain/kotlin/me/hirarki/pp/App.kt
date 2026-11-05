package me.hirarki.pp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF06B6D4),
    secondary = Color(0xFFEC4899),
    background = Color(0xFF0A0A0A),
    surface = Color(0xFF1A1A1A),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFE5E5E5),
    onSurface = Color(0xFFE5E5E5)
)

@Composable
fun App() {
    MaterialTheme(colorScheme = DarkColorScheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PortfolioSpikeContent()
        }
    }
}

@Composable
fun PortfolioSpikeContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Phase 2 Technical Spike",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "Mobile Developer Portfolio",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Text(
                text = "Kotlin Multiplatform + Compose Web/Wasm",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Validation Checklist",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            
            ChecklistItem("✓ Compose Web/Wasm Beta (v1.12.2)")
            ChecklistItem("✓ Material3 Dark Theme")
            ChecklistItem("✓ Responsive Layout")
            ChecklistItem("✓ Static Build Output")
            ChecklistItem("? DOM Accessibility")
            ChecklistItem("? SEO Crawlability")
        }
        
        Text(
            text = "Testing browser compatibility, DOM structure, and static hosting readiness",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun ChecklistItem(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}