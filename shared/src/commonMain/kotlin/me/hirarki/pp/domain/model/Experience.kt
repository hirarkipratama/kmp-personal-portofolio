package me.hirarki.pp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Experience(
    val id: String,
    val role: String,
    val organization: String,
    val location: String? = null,
    val workMode: WorkMode? = null,
    val startDate: String,
    val endDate: String? = null,
    val current: Boolean = false,
    val achievements: List<String> = emptyList(),
    val technologies: List<String> = emptyList()
) {
    init {
        require(id.isNotBlank()) { "Experience id cannot be blank" }
        require(role.isNotBlank()) { "Role cannot be blank" }
        require(organization.isNotBlank()) { "Organization cannot be blank" }
        require(startDate.isNotBlank()) { "Experience start date cannot be blank" }
        require(achievements.all { it.isNotBlank() }) { "Experience achievements cannot be blank" }
        require(technologies.all { it.isNotBlank() }) { "Experience technologies cannot be blank" }
    }
}

@Serializable
enum class WorkMode {
    REMOTE,
    HYBRID,
    ONSITE
}
