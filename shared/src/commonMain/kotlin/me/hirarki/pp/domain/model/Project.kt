package me.hirarki.pp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: String,
    val title: String,
    val summary: String,
    val category: String,
    val tags: List<String> = emptyList(),
    val imagePath: String? = null,
    val problem: String? = null,
    val solution: String? = null,
    val impact: String? = null,
    val metrics: List<ProjectMetric> = emptyList(),
    val externalUrl: String? = null
) {
    init {
        require(id.isNotBlank()) { "Project id cannot be blank" }
        require(title.isNotBlank()) { "Project title cannot be blank" }
        externalUrl?.let { url ->
            require(url.startsWith("https://") || url.startsWith("http://")) {
                "Project external URL must use http:// or https://"
            }
        }
    }
}

@Serializable
data class ProjectMetric(
    val label: String,
    val value: String
)
