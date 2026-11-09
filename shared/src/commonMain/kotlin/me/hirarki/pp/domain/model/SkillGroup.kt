package me.hirarki.pp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SkillGroup(
    val category: String,
    val skills: List<Skill>
) {
    init {
        require(category.isNotBlank()) { "Skill category cannot be blank" }
        require(skills.isNotEmpty()) { "Skill group must contain at least one skill" }
    }
}

@Serializable
data class Skill(
    val name: String,
    val description: String? = null
) {
    init {
        require(name.isNotBlank()) { "Skill name cannot be blank" }
    }
}
