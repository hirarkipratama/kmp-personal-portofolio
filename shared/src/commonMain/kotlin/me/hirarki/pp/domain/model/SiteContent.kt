package me.hirarki.pp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SiteContent(
    val identity: Identity,
    val hero: Hero,
    val metrics: List<Metric> = emptyList(),
    val footer: Footer
)

@Serializable
data class Identity(
    val name: String,
    val role: String,
    val email: String? = null
)

@Serializable
data class Hero(
    val headline: String,
    val summary: String,
    val technologies: List<String> = emptyList(),
    val profileImagePath: String? = null,
    val cvPath: String? = null
)

@Serializable
data class Metric(
    val label: String,
    val value: String,
    val description: String? = null
)

@Serializable
data class Footer(
    val copyright: String,
    val backToTopLabel: String = "Back to top"
)
