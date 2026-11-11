package me.hirarki.pp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.hirarki.pp.domain.model.Project
import me.hirarki.pp.domain.model.SiteContent
import me.hirarki.pp.domain.model.SkillGroup
import me.hirarki.pp.domain.model.SocialLink
import me.hirarki.pp.domain.repository.PortfolioRepository
import me.hirarki.pp.ui.theme.PortfolioColors
import me.hirarki.pp.ui.theme.PortfolioDarkColorScheme
import me.hirarki.pp.ui.theme.PortfolioRadius
import me.hirarki.pp.ui.theme.PortfolioSpacing
import me.hirarki.pp.ui.theme.PortfolioTokens
import me.hirarki.pp.ui.theme.PortfolioTypography

@Composable
fun App() {
    MaterialTheme(
        colorScheme = PortfolioDarkColorScheme,
        typography = PortfolioTypography
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PortfolioContent()
        }
    }
}

@Composable
private fun PortfolioContent() {
    val repository = remember { PortfolioRepository() }
    var siteContent by remember { mutableStateOf<SiteContent?>(null) }
    var projects by remember { mutableStateOf<List<Project>>(emptyList()) }
    var skillGroups by remember { mutableStateOf<List<SkillGroup>>(emptyList()) }
    var socialLinks by remember { mutableStateOf<List<SocialLink>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val siteResult = repository.loadSiteContent()
        val projectsResult = repository.loadProjects()
        val skillGroupsResult = repository.loadSkillGroups()
        val socialLinksResult = repository.loadSocialLinks()

        siteContent = siteResult.getOrNull()
        projects = projectsResult.getOrDefault(emptyList())
        skillGroups = skillGroupsResult.getOrDefault(emptyList())
        socialLinks = socialLinksResult.getOrDefault(emptyList())

        isLoading = false
        errorMessage = listOfNotNull(
            siteResult.exceptionOrNull()?.message,
            projectsResult.exceptionOrNull()?.message,
            skillGroupsResult.exceptionOrNull()?.message,
            socialLinksResult.exceptionOrNull()?.message
        ).firstOrNull()
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(PortfolioSpacing.medium))
                    Text(
                        text = "Loading portfolio shell...",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        errorMessage != null && siteContent == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Unable to load portfolio data",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(PortfolioSpacing.small))
                    Text(
                        text = errorMessage ?: "Unexpected error",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        siteContent != null -> {
            PortfolioPage(
                siteContent = siteContent!!,
                projects = projects,
                skillGroups = skillGroups,
                socialLinks = socialLinks
            )
        }
    }
}

@Composable
private fun PortfolioPage(
    siteContent: SiteContent,
    projects: List<Project>,
    skillGroups: List<SkillGroup>,
    socialLinks: List<SocialLink>
) {
    val navItems = listOf("Overview", "Projects", "Skills", "Experience", "Contact")
    val contentScrollState = rememberScrollState()
    val pageModifier = Modifier
        .fillMaxSize()
        .background(PortfolioColors.Background)

    BoxWithConstraints(modifier = pageModifier) {
        val compact = maxWidth < 768.dp
        var menuOpen by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 280.dp)
                .background(brush = PortfolioTokens.contentGlow)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(contentScrollState)
                .padding(horizontal = if (compact) 16.dp else 24.dp)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = PortfolioTokens.pageMaxWidth)
            ) {
                PortfolioHeader(
                    navItems = navItems,
                    compact = compact,
                    menuOpen = menuOpen,
                    onToggleMenu = { menuOpen = !menuOpen },
                    socialLinks = socialLinks
                )
            }

            Spacer(modifier = Modifier.height(PortfolioSpacing.xl))

            PortfolioHero(
                siteContent = siteContent,
                compact = compact
            )

            Spacer(modifier = Modifier.height(PortfolioSpacing.xxl))

            SectionBlock(
                title = "Key metrics",
                subtitle = "Verified portfolio indicators",
                modifier = Modifier.fillMaxWidth().widthIn(max = PortfolioTokens.pageMaxWidth)
            ) {
                if (siteContent.metrics.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
                    ) {
                        siteContent.metrics.forEach { metric ->
                            MetricCard(metric, Modifier.weight(1f))
                        }
                    }
                } else {
                    PlaceholderCard("Metrics pending approval")
                }
            }

            Spacer(modifier = Modifier.height(PortfolioSpacing.xxl))

            SectionBlock(
                title = "Selected work",
                subtitle = "Project and case-study references",
                modifier = Modifier.fillMaxWidth().widthIn(max = PortfolioTokens.pageMaxWidth)
            ) {
                if (projects.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
                    ) {
                        projects.take(3).forEach { project ->
                            ProjectCard(project, compact, Modifier.weight(1f))
                        }
                    }
                } else {
                    PlaceholderCard("Project content placeholder")
                }
            }

            Spacer(modifier = Modifier.height(PortfolioSpacing.xxl))

            SectionBlock(
                title = "Tech stack",
                subtitle = "Core disciplines and frameworks",
                modifier = Modifier.fillMaxWidth().widthIn(max = PortfolioTokens.pageMaxWidth)
            ) {
                if (skillGroups.isNotEmpty()) {
                    val allSkills = skillGroups.flatMap { it.skills }
                    SkillTagFlow(allSkills.map { it.name })
                } else {
                    PlaceholderCard("Skills placeholder ready for approved content")
                }
            }

            Spacer(modifier = Modifier.height(PortfolioSpacing.xxl))

            SectionBlock(
                title = "Contact",
                subtitle = "Available touchpoints and public channels",
                modifier = Modifier.fillMaxWidth().widthIn(max = PortfolioTokens.pageMaxWidth)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)) {
                    if (socialLinks.isNotEmpty()) {
                        socialLinks.forEach { link ->
                            SocialLinkButton(link)
                        }
                    } else {
                        PlaceholderCard("Professional links pending confirmation")
                    }

                    PortfolioButton(
                        text = siteContent.footer.backToTopLabel,
                        onClick = null,
                        variant = "secondary"
                    )
                }
            }
        }
    }
}

@Composable
private fun PortfolioHeader(
    navItems: List<String>,
    compact: Boolean,
    menuOpen: Boolean,
    onToggleMenu: () -> Unit,
    socialLinks: List<SocialLink>
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = PortfolioSpacing.large),
        color = Color(0x9911182B),
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(PortfolioRadius.large)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Hirarki",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Mobile Developer Portfolio",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (compact) {
                    PortfolioButton(
                        text = if (menuOpen) "Close" else "Menu",
                        onClick = onToggleMenu,
                        variant = "secondary"
                    )
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)) {
                        navItems.forEach { item ->
                            Text(
                                text = item,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(PortfolioRadius.full))
                                    .background(Color(0x141C2A46))
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            if (compact && menuOpen) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.small)
                ) {
                    navItems.forEach { item ->
                        Text(
                            text = item,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(PortfolioRadius.medium))
                                .background(Color(0x141C2A46))
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PortfolioHero(siteContent: SiteContent, compact: Boolean) {
    val heroModifier = if (compact) {
        Modifier.fillMaxWidth()
    } else {
        Modifier.fillMaxWidth()
    }

    Surface(
        modifier = heroModifier
            .clip(RoundedCornerShape(PortfolioRadius.xl))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                shape = RoundedCornerShape(PortfolioRadius.xl)
            ),
        color = Color(0xFF121A2A),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (compact) 20.dp else 32.dp),
            horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.xl),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
            ) {
                Text(
                    text = siteContent.identity.role,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = siteContent.identity.name,
                    style = if (compact) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = siteContent.hero.headline,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = siteContent.hero.summary,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.small),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PortfolioButton(text = "Featured projects", onClick = null, variant = "primary")
                    PortfolioButton(text = "Download CV", onClick = null, variant = "secondary")
                }

                if (siteContent.hero.technologies.isNotEmpty()) {
                    SkillTagFlow(siteContent.hero.technologies)
                }
            }

            if (!compact) {
                Card(
                    modifier = Modifier.width(280.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)),
                    shape = RoundedCornerShape(PortfolioRadius.large)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(
                                        PortfolioColors.Primary.copy(alpha = 0.3f),
                                        PortfolioColors.Secondary.copy(alpha = 0.2f),
                                        Color.Transparent
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Profile\nPreview",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionBlock(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.xs)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        content()
    }
}

@Composable
private fun MetricCard(
    metric: me.hirarki.pp.domain.model.Metric,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(PortfolioRadius.medium),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier.padding(PortfolioSpacing.large),
            verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.small)
        ) {
            Text(
                text = metric.value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = metric.label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (!metric.description.isNullOrBlank()) {
                Text(
                    text = metric.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProjectCard(
    project: Project,
    compact: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.92f),
        shape = RoundedCornerShape(PortfolioRadius.large),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier.padding(PortfolioSpacing.large),
            verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.small)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (compact) 170.dp else 220.dp)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                PortfolioColors.Primary.copy(alpha = 0.28f),
                                PortfolioColors.Secondary.copy(alpha = 0.20f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(PortfolioRadius.medium)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = project.title.take(18).ifEmpty { "Project" },
                    style = if (compact) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = project.category,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = project.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = project.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            SkillTagFlow(project.tags)
        }
    }
}

@Composable
private fun SocialLinkButton(link: SocialLink) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(PortfolioRadius.medium),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = link.platform,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = link.label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun TagChip(label: String) {
    Surface(
        shape = RoundedCornerShape(PortfolioRadius.full),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun PortfolioButton(
    text: String,
    onClick: (() -> Unit)?,
    variant: String = "primary"
) {
    val colors = when (variant) {
        "secondary" -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
        else -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        )
    }

    Button(
        onClick = { onClick?.invoke() },
        modifier = Modifier,
        colors = colors,
        shape = RoundedCornerShape(PortfolioRadius.full),
        enabled = onClick != null
    ) {
        Text(text = text)
    }
}

@Composable
private fun PlaceholderCard(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(PortfolioRadius.large),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PortfolioSpacing.large),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SkillTagFlow(items: List<String>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.small)
    ) {
        items.chunked(3).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.small)
            ) {
                rowItems.forEach { item ->
                    TagChip(item)
                }
            }
        }
    }
}

