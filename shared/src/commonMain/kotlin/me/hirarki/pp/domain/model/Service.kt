package me.hirarki.pp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val id: String,
    val title: String,
    val description: String,
    val capabilities: List<String> = emptyList()
) {
    init {
        require(id.isNotBlank()) { "Service id cannot be blank" }
        require(title.isNotBlank()) { "Service title cannot be blank" }
        require(description.isNotBlank()) { "Service description cannot be blank" }
        require(capabilities.all { it.isNotBlank() }) { "Service capabilities cannot be blank" }
    }
}
