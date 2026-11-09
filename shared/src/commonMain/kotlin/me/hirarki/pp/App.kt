package me.hirarki.pp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.hirarki.pp.ui.theme.PortfolioDarkColorScheme
import me.hirarki.pp.ui.theme.PortfolioSpacing

@Composable
fun App() {
    MaterialTheme(colorScheme = PortfolioDarkColorScheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PortfolioContent()
        }
    }
}

@Composable
fun PortfolioContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PortfolioSpacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.xl, Alignment.CenterVertically)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
        ) {
            Text(
                text = "Phase 3 Foundation Complete",
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
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(PortfolioSpacing.large),
            verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.small)
        ) {
            Text(
                text = "Foundation Checklist",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            
            ChecklistItem("✓ kotlinx.serialization added")
            ChecklistItem("✓ Content models created")
            ChecklistItem("✓ PortfolioRepository implemented")
            ChecklistItem("✓ Design tokens centralized")
            ChecklistItem("✓ JSON content templates")
            ChecklistItem("✓ Directory structure ready")
        }
        
        Text(
            text = "Ready for Phase 4: Content model and Decap CMS proof",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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