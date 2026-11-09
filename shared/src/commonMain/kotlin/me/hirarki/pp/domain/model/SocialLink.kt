package me.hirarki.pp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SocialLink(
    val platform: String,
    val url: String,
    val label: String
) {
    init {
        require(platform.isNotBlank()) { "Platform cannot be blank" }
        require(url.isNotBlank()) { "URL cannot be blank" }
        require(url.startsWith("https://") || url.startsWith("http://") || url.startsWith("mailto:")) {
            "URL must use https://, http://, or mailto: scheme"
        }
        require(label.isNotBlank()) { "Label cannot be blank" }
    }
}
