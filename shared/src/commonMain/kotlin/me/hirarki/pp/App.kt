package me.hirarki.pp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.hirarki.pp.domain.model.SiteContent
import me.hirarki.pp.domain.repository.PortfolioRepository
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
    val repository = remember { PortfolioRepository() }
    var siteContent by remember { mutableStateOf<SiteContent?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(Unit) {
        repository.loadSiteContent()
            .onSuccess { content ->
                siteContent = content
                isLoading = false
            }
            .onFailure { error ->
                errorMessage = error.message ?: "Unknown error"
                isLoading = false
            }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PortfolioSpacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.xl, Alignment.CenterVertically)
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                Text(
                    text = "Loading content...",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            errorMessage != null -> {
                Text(
                    text = "Error loading content",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = errorMessage ?: "",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            siteContent != null -> {
                ContentLoadedView(siteContent!!)
            }
        }
    }
}

@Composable
fun ContentLoadedView(siteContent: SiteContent) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
    ) {
        Text(
            text = siteContent.identity.name,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = siteContent.identity.role,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Text(
            text = siteContent.hero.headline,
            fontSize = 20.sp,
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
            text = "Phase 4 - Content Loading Proof",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        
        ChecklistItem("✓ Content fetched from JSON")
        ChecklistItem("✓ Serialization working")
        ChecklistItem("✓ Repository pattern implemented")
        ChecklistItem("✓ Decap CMS configured")
        
        Text(
            text = "Summary: ${siteContent.hero.summary}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = PortfolioSpacing.small)
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