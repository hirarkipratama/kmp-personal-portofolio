package me.hirarki.pp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinproject.shared.generated.resources.Res
import kotlinproject.shared.generated.resources.profile
import me.hirarki.pp.domain.model.AboutContent
import me.hirarki.pp.domain.model.Experience
import me.hirarki.pp.domain.model.Project
import me.hirarki.pp.domain.model.ProjectScreenshot
import me.hirarki.pp.domain.model.Service
import me.hirarki.pp.domain.model.SiteContent
import me.hirarki.pp.domain.model.SkillGroup
import me.hirarki.pp.domain.model.SocialLink
import me.hirarki.pp.domain.model.WorkMode
import me.hirarki.pp.domain.repository.PortfolioRepository
import me.hirarki.pp.domain.repository.loadProjectImage
import me.hirarki.pp.openExternalUrl
import me.hirarki.pp.ui.theme.PortfolioColors
import me.hirarki.pp.ui.theme.PortfolioDarkColorScheme
import me.hirarki.pp.ui.theme.PortfolioRadius
import me.hirarki.pp.ui.theme.PortfolioSpacing
import me.hirarki.pp.ui.theme.PortfolioTokens
import me.hirarki.pp.ui.theme.PortfolioTypography
import org.jetbrains.compose.resources.painterResource

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
    var services by remember { mutableStateOf<List<Service>>(emptyList()) }
    var experience by remember { mutableStateOf<List<Experience>>(emptyList()) }
    var skillGroups by remember { mutableStateOf<List<SkillGroup>>(emptyList()) }
    var socialLinks by remember { mutableStateOf<List<SocialLink>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val siteResult = repository.loadSiteContent()
        val projectsResult = repository.loadProjects()
        val servicesResult = repository.loadServices()
        val experienceResult = repository.loadExperience()
        val skillGroupsResult = repository.loadSkillGroups()
        val socialLinksResult = repository.loadSocialLinks()

        siteContent = siteResult.getOrNull()
        projects = projectsResult.getOrDefault(emptyList())
        services = servicesResult.getOrDefault(emptyList())
        experience = experienceResult.getOrDefault(emptyList())
        skillGroups = skillGroupsResult.getOrDefault(emptyList())
        socialLinks = socialLinksResult.getOrDefault(emptyList())

        isLoading = false
        errorMessage = listOfNotNull(
            siteResult.exceptionOrNull()?.message,
            projectsResult.exceptionOrNull()?.message,
            servicesResult.exceptionOrNull()?.message,
            experienceResult.exceptionOrNull()?.message,
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
                services = services,
                experience = experience,
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
    services: List<Service>,
    experience: List<Experience>,
    skillGroups: List<SkillGroup>,
    socialLinks: List<SocialLink>
) {
    val navItems = listOf("About", "Projects", "Services", "Experience") +
        if (socialLinks.isNotEmpty()) listOf("Contact") else emptyList()
    val contentScrollState = rememberScrollState()
    val projectsRequester = remember { BringIntoViewRequester() }
    val contactRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
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
                    onContactClick = {
                        coroutineScope.launch { contactRequester.bringIntoView() }
                        menuOpen = false
                    }
                )
            }

            Spacer(modifier = Modifier.height(PortfolioSpacing.xl))

            PortfolioHero(
                siteContent = siteContent,
                compact = compact,
                onFeaturedProjects = {
                    coroutineScope.launch { projectsRequester.bringIntoView() }
                },
                onDownloadCv = siteContent.hero.cvPath
                    ?.takeIf(::isSafeCvPath)
                    ?.let { path ->
                        { downloadFile(path, "Hirarki-Ardi-Pratama-CV.pdf") }
                    }
            )

            Spacer(modifier = Modifier.height(PortfolioSpacing.xxl))

            if (siteContent.metrics.isNotEmpty()) {
                SectionBlock(
                    title = "Impact metrics",
                    subtitle = "Verified experience and product outcomes",
                    modifier = Modifier.fillMaxWidth().widthIn(max = PortfolioTokens.pageMaxWidth)
                ) {
                    siteContent.metrics.chunked(if (compact) 2 else siteContent.metrics.size).forEach { rowMetrics ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
                        ) {
                            rowMetrics.forEach { metric ->
                                MetricCard(metric, Modifier.weight(1f))
                            }
                            if (compact && rowMetrics.size == 1) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(PortfolioSpacing.xxl))

            if (siteContent.about != null || skillGroups.isNotEmpty() || siteContent.hero.technologies.isNotEmpty()) {
                AboutProfileMatrix(
                    siteContent = siteContent,
                    about = siteContent.about,
                    skillGroups = skillGroups,
                    compact = compact
                )
                Spacer(modifier = Modifier.height(PortfolioSpacing.xxl))
            }

            if (projects.isNotEmpty()) {
                SectionBlock(
                    title = "Selected work",
                    subtitle = "Mobile applications and product experiences",
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = PortfolioTokens.pageMaxWidth)
                        .bringIntoViewRequester(projectsRequester)
                ) {
                    projects.filter { it.featured }.forEach { project ->
                        ProjectCard(project, compact, featured = true)
                    }

                    projects.filterNot { it.featured }
                        .chunked(if (compact) 1 else 2)
                        .forEach { projectRow ->
                            val wideRow = !compact && projectRow.size == 1
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
                            ) {
                                projectRow.forEach { project ->
                                    ProjectCard(
                                        project = project,
                                        compact = compact,
                                        featured = wideRow,
                                        modifier = if (wideRow) Modifier else Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                }

                Spacer(modifier = Modifier.height(PortfolioSpacing.xxl))
            }

            if (services.isNotEmpty()) {
                SectionBlock(
                    title = "Services & specializations",
                    subtitle = "Selected mobile engineering capabilities",
                    modifier = Modifier.fillMaxWidth().widthIn(max = PortfolioTokens.pageMaxWidth)
                ) {
                    services.chunked(if (compact) 1 else 2).forEach { serviceRow ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
                        ) {
                            serviceRow.forEach { service ->
                                ServiceCard(service, Modifier.weight(1f))
                            }
                            if (!compact && serviceRow.size == 1) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(PortfolioSpacing.xxl))
            }

            if (experience.isNotEmpty()) {
                SectionBlock(
                    title = "Engineering journey & experience",
                    subtitle = "Roles, impact, and technologies",
                    modifier = Modifier.fillMaxWidth().widthIn(max = PortfolioTokens.pageMaxWidth)
                ) {
                    ExperienceTimeline(experience, compact)
                }

                Spacer(modifier = Modifier.height(PortfolioSpacing.xxl))
            }

            if (socialLinks.isNotEmpty()) {
                SectionBlock(
                    title = "Contact",
                    subtitle = "Professional links and contact methods",
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = PortfolioTokens.pageMaxWidth)
                        .bringIntoViewRequester(contactRequester)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)) {
                        socialLinks.forEach { link ->
                            SocialLinkButton(link)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(PortfolioSpacing.xxl))
            }

            PortfolioFooter(
                siteContent = siteContent,
                compact = compact,
                onBackToTop = { coroutineScope.launch { contentScrollState.scrollTo(0) } }
            )
        }
    }
}

@Composable
private fun AboutProfileMatrix(
    siteContent: SiteContent,
    about: AboutContent?,
    skillGroups: List<SkillGroup>,
    compact: Boolean
) {
    SectionBlock(
        title = "About / profile matrix",
        subtitle = "Professional focus and the tools behind the work",
        modifier = Modifier.fillMaxWidth().widthIn(max = PortfolioTokens.pageMaxWidth)
    ) {
        if (compact) {
            Column(verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)) {
                ProfileInfoCard(title = "Professional narrative") {
                    Text(
                        text = siteContent.hero.summary,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (!about?.focusAreas.isNullOrEmpty()) {
                    ProfileInfoCard(title = "Engineering focus") {
                        SkillTagFlow(about.focusAreas, itemsPerRow = 2)
                    }
                }
                if (siteContent.hero.technologies.isNotEmpty()) {
                    ProfileInfoCard(title = "Mobile frameworks & languages") {
                        SkillTagFlow(siteContent.hero.technologies, itemsPerRow = 2)
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium),
                verticalAlignment = Alignment.Top
            ) {
                ProfileInfoCard(
                    title = "Professional narrative",
                    modifier = Modifier.weight(1.2f)
                ) {
                    Text(
                        text = siteContent.hero.summary,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
                ) {
                    if (!about?.focusAreas.isNullOrEmpty()) {
                        ProfileInfoCard(title = "Engineering focus") {
                            SkillTagFlow(about.focusAreas, itemsPerRow = 2)
                        }
                    }
                    if (siteContent.hero.technologies.isNotEmpty()) {
                        ProfileInfoCard(title = "Mobile frameworks & languages") {
                            SkillTagFlow(siteContent.hero.technologies, itemsPerRow = 2)
                        }
                    }
                }
            }
        }

        if (skillGroups.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)) {
                skillGroups.chunked(if (compact) 1 else 2).forEach { rowGroups ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
                    ) {
                        rowGroups.forEach { group ->
                            SkillGroupCard(group, Modifier.weight(1f))
                        }
                        if (!compact && rowGroups.size == 1) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(PortfolioRadius.large),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        )
    ) {
        Column(
            modifier = Modifier.padding(PortfolioSpacing.large),
            verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.semantics { heading() }
            )
            content()
        }
    }
}

@Composable
private fun SkillGroupCard(
    group: SkillGroup,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(PortfolioRadius.large),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        )
    ) {
        Column(
            modifier = Modifier.padding(PortfolioSpacing.large),
            verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
        ) {
            Text(
                text = group.category,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.semantics { heading() }
            )
            group.skills.forEach { skill ->
                Column(verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.xs)) {
                    Text(
                        text = skill.name,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (!skill.description.isNullOrBlank()) {
                        Text(
                            text = skill.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
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
    onContactClick: () -> Unit
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
                            if (item == "Contact") {
                                TextButton(onClick = onContactClick) {
                                    Text(
                                        text = item,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            } else {
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
            }

            if (compact && menuOpen) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.small)
                ) {
                    navItems.forEach { item ->
                        if (item == "Contact") {
                            TextButton(
                                onClick = onContactClick,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = item,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
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
}

@Composable
private fun PortfolioHero(
    siteContent: SiteContent,
    compact: Boolean,
    onFeaturedProjects: () -> Unit,
    onDownloadCv: (() -> Unit)?
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(PortfolioRadius.xl))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                shape = RoundedCornerShape(PortfolioRadius.xl)
            ),
        color = Color(0xFF121A2A),
        tonalElevation = 0.dp
    ) {
        if (compact) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.large)
            ) {
                HeroCopy(siteContent, onFeaturedProjects, onDownloadCv, compact)
                ProfilePanel(siteContent, Modifier.fillMaxWidth())
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.xl),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeroCopy(
                    siteContent = siteContent,
                    onFeaturedProjects = onFeaturedProjects,
                    onDownloadCv = onDownloadCv,
                    compact = compact,
                    modifier = Modifier.weight(1f)
                )
                ProfilePanel(siteContent, Modifier.width(280.dp))
            }
        }
    }
}

private fun isSafeCvPath(path: String): Boolean =
    path.startsWith("/cv/") && path.split('/').none { it == "." || it == ".." }

@Composable
private fun HeroCopy(
    siteContent: SiteContent,
    onFeaturedProjects: () -> Unit,
    onDownloadCv: (() -> Unit)?,
    compact: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
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
            PortfolioButton(text = "Featured projects", onClick = onFeaturedProjects, variant = "primary")
            if (onDownloadCv != null) {
                PortfolioButton(text = "Download CV", onClick = onDownloadCv, variant = "secondary")
            }
        }

        if (siteContent.hero.technologies.isNotEmpty()) {
            SkillTagFlow(siteContent.hero.technologies)
        }
    }
}

@Composable
private fun ProfilePanel(siteContent: SiteContent, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)),
        shape = RoundedCornerShape(PortfolioRadius.large)
    ) {
        Column {
            if (!siteContent.hero.profileImagePath.isNullOrBlank()) {
                Image(
                    painter = painterResource(Res.drawable.profile),
                    contentDescription = "Portrait of ${siteContent.identity.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(PortfolioRadius.large))
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
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
                        text = "Profile",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column(
                modifier = Modifier.padding(PortfolioSpacing.medium),
                verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.xs)
            ) {
                Text(
                    text = siteContent.identity.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = siteContent.identity.role,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.semantics { heading() }
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
private fun ServiceCard(
    service: Service,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.92f),
        shape = RoundedCornerShape(PortfolioRadius.large),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
        )
    ) {
        Column(
            modifier = Modifier.padding(PortfolioSpacing.large),
            verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
        ) {
            Text(
                text = service.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.semantics { heading() }
            )
            Text(
                text = service.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            service.capabilities.forEach { capability ->
                BulletText(capability)
            }
        }
    }
}

@Composable
private fun ExperienceTimeline(
    experience: List<Experience>,
    compact: Boolean
) {
    Column {
        experience.forEachIndexed { index, item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.small),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier.width(20.dp).height(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(PortfolioRadius.full)
                            )
                    )
                }
                ExperienceCard(item, compact, Modifier.weight(1f))
            }
            if (index < experience.lastIndex) {
                Row {
                    Box(
                        modifier = Modifier.width(20.dp).height(PortfolioSpacing.large),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.outline)
                        )
                    }
                    Spacer(Modifier.width(PortfolioSpacing.small))
                }
            }
        }
    }
}

@Composable
private fun ExperienceCard(
    experience: Experience,
    compact: Boolean,
    modifier: Modifier = Modifier
) {
    val workMode = experience.workMode?.displayName()
    val location = experience.location?.takeIf(String::isNotBlank)
        ?.takeUnless { it.equals(workMode, ignoreCase = true) }
    val metadata = listOfNotNull(
        experienceDateRange(experience),
        location,
        workMode
    ).joinToString(" | ")

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.92f),
        shape = RoundedCornerShape(PortfolioRadius.large),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
        )
    ) {
        Column(
            modifier = Modifier.padding(PortfolioSpacing.large),
            verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.xs)) {
                Text(
                    text = experience.role,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.semantics { heading() }
                )
                Text(
                    text = experience.organization,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = metadata,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            experience.achievements.forEach { achievement ->
                BulletText(achievement)
            }

            if (experience.technologies.isNotEmpty()) {
                SkillTagFlow(experience.technologies, itemsPerRow = if (compact) 2 else 4)
            }
        }
    }
}

@Composable
private fun BulletText(text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.small),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 7.dp)
                .size(5.dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(PortfolioRadius.full)
                )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun experienceDateRange(experience: Experience): String {
    val endDate = when {
        experience.current -> "Present"
        !experience.endDate.isNullOrBlank() -> formatExperienceDate(experience.endDate)
        else -> null
    }
    return listOfNotNull(formatExperienceDate(experience.startDate), endDate).joinToString(" - ")
}

private fun formatExperienceDate(value: String): String {
    val dateParts = value.split('-')
    if (dateParts.size != 2 || dateParts[0].length != 4) return value

    val month = dateParts[1].toIntOrNull()?.takeIf { it in 1..12 } ?: return value
    val monthNames = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )
    return "${monthNames[month - 1]} ${dateParts[0]}"
}

private fun WorkMode.displayName(): String = when (this) {
    WorkMode.REMOTE -> "Remote"
    WorkMode.HYBRID -> "Hybrid"
    WorkMode.ONSITE -> "On-site"
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
    featured: Boolean,
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
            verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
        ) {
            if (featured && !compact) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.large),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProjectScreenshotRail(
                        project = project,
                        compact = compact,
                        featured = true,
                        modifier = Modifier.weight(1.15f)
                    )
                    ProjectDetails(project, Modifier.weight(0.85f))
                }
            } else {
                ProjectScreenshotRail(
                    project = project,
                    compact = compact,
                    featured = featured
                )
                ProjectDetails(project, Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun ProjectScreenshotRail(
    project: Project,
    compact: Boolean,
    featured: Boolean,
    modifier: Modifier = Modifier
) {
    val screenshotWidth = when {
        compact -> 152.dp
        featured -> 164.dp
        else -> 132.dp
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "${project.title} screenshots" },
        horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
    ) {
        items(project.screenshots, key = { it.image }) { screenshot ->
            Column(
                modifier = Modifier.width(screenshotWidth),
                verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.small)
            ) {
                ProjectScreenshotImage(
                    screenshot = screenshot,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.47f)
                )
                if (!screenshot.caption.isNullOrBlank()) {
                    Text(
                        text = screenshot.caption,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectScreenshotImage(
    screenshot: ProjectScreenshot,
    modifier: Modifier = Modifier
) {
    var bitmap by remember(screenshot.image) { mutableStateOf<ImageBitmap?>(null) }
    var errorMessage by remember(screenshot.image) { mutableStateOf<String?>(null) }
    var isInViewport by remember(screenshot.image) { mutableStateOf(false) }
    val viewportHeight = LocalWindowInfo.current.containerSize.height.toFloat()

    LaunchedEffect(screenshot.image, isInViewport) {
        if (!isInViewport) return@LaunchedEffect

        try {
            bitmap = loadProjectImage(screenshot.image)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            errorMessage = exception.message ?: "Image unavailable"
        }
    }

    val shape = RoundedCornerShape(PortfolioRadius.medium)
    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                val bounds = coordinates.boundsInWindow()
                if (bounds.bottom > 0f && bounds.top < viewportHeight) {
                    isInViewport = true
                }
            }
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f), shape)
            .semantics { contentDescription = screenshot.alt },
        contentAlignment = Alignment.Center
    ) {
        when (val loadedBitmap = bitmap) {
            null -> {
                if (errorMessage != null) {
                    Text(
                        text = "Image unavailable",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            else -> Image(
                bitmap = loadedBitmap,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun ProjectDetails(
    project: Project,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.small)
    ) {
        Text(
            text = project.category,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = project.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.semantics { heading() }
        )

        project.summary?.takeIf(String::isNotBlank)?.let { summary ->
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        ProjectDetail("Problem", project.problem)
        ProjectDetail("Solution", project.solution)
        ProjectDetail("Impact", project.impact)

        if (project.metrics.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
            ) {
                project.metrics.forEach { metric ->
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.xs)
                    ) {
                        Text(
                            text = metric.value,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = metric.label,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        if (project.technologies.isNotEmpty()) {
            SkillTagFlow(project.technologies)
        }

        project.caseStudyUrl?.let { url ->
            PortfolioButton(
                text = "View case study",
                onClick = { openExternalUrl(url) },
                variant = "secondary"
            )
        }
    }
}

@Composable
private fun ProjectDetail(label: String, value: String?) {
    value?.takeIf(String::isNotBlank)?.let {
        Column(verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.xs)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SocialLinkButton(link: SocialLink) {
    var isFocused by remember { mutableStateOf(false) }
    Button(
        onClick = { openExternalUrl(link.url) },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused }
            .then(
                if (isFocused) {
                    Modifier.border(
                        2.dp,
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(PortfolioRadius.medium)
                    )
                } else {
                    Modifier
                }
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(PortfolioRadius.medium),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.xs)) {
                Text(
                    text = link.platform,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = link.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "Open",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PortfolioFooter(
    siteContent: SiteContent,
    compact: Boolean,
    onBackToTop: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = PortfolioTokens.pageMaxWidth),
        shape = RoundedCornerShape(PortfolioRadius.large),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
    ) {
        if (compact) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PortfolioSpacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.medium)
            ) {
                FooterCopyright(siteContent)
                PortfolioButton(
                    text = siteContent.footer.backToTopLabel,
                    onClick = onBackToTop,
                    variant = "secondary"
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PortfolioSpacing.large, vertical = PortfolioSpacing.medium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FooterCopyright(siteContent)
                PortfolioButton(
                    text = siteContent.footer.backToTopLabel,
                    onClick = onBackToTop,
                    variant = "secondary"
                )
            }
        }
    }
}

@Composable
private fun FooterCopyright(siteContent: SiteContent) {
    Text(
        text = siteContent.footer.copyright,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
    )
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
    var isFocused by remember { mutableStateOf(false) }
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
        modifier = Modifier
            .onFocusChanged { isFocused = it.isFocused }
            .then(
                if (isFocused) {
                    Modifier.border(
                        2.dp,
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(PortfolioRadius.full)
                    )
                } else {
                    Modifier
                }
            ),
        colors = colors,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
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
private fun SkillTagFlow(items: List<String>, itemsPerRow: Int = 3) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PortfolioSpacing.small)
    ) {
        items.chunked(itemsPerRow).forEach { rowItems ->
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
