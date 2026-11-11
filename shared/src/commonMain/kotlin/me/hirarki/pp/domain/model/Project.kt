package me.hirarki.pp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val slug: String,
    val title: String,
    val category: String,
    val summary: String? = null,
    val problem: String? = null,
    val solution: String? = null,
    val impact: String? = null,
    val screenshots: List<ProjectScreenshot> = emptyList(),
    val technologies: List<String> = emptyList(),
    val metrics: List<ProjectMetric> = emptyList(),
    val caseStudyUrl: String? = null,
    val featured: Boolean = false
) {
    init {
        require(slug.matches(Regex("[a-z0-9]+(?:-[a-z0-9]+)*"))) {
            "Project slug must contain lowercase letters, numbers, or hyphens"
        }
        require(title.isNotBlank()) { "Project title cannot be blank" }
        require(category.isNotBlank()) { "Project category cannot be blank" }
        require(screenshots.all {
            it.image.startsWith("/images/projects/") &&
                !it.image.contains("..") &&
                !it.image.contains('\\')
        }) {
            "Project screenshots must use a local /images/projects/ path"
        }
        caseStudyUrl?.let { url ->
            require(url.startsWith("https://") || url.startsWith("http://")) {
                "Project case study URL must use http:// or https://"
            }
        }
    }
}

@Serializable
data class ProjectScreenshot(
    val image: String,
    val alt: String,
    val caption: String? = null
) {
    init {
        require(image.isNotBlank()) { "Project screenshot image cannot be blank" }
        require(alt.isNotBlank()) { "Project screenshot alt text cannot be blank" }
    }
}

@Serializable
data class ProjectMetric(
    val label: String,
    val value: String
)
