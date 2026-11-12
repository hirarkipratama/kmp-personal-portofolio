package me.hirarki.pp.domain.model

import kotlinx.serialization.Serializable

private val httpsUrlPattern = Regex(
    """^https://(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\.)+[A-Za-z]{2,}(?::[0-9]{1,5})?(?:[/?#][^\s]*)?$"""
)
private val mailtoUrlPattern = Regex(
    """^mailto:[^@\s?]+@(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\.)+[A-Za-z]{2,}(?:\?[^\s]*)?$"""
)

@Serializable
data class SocialLink(
    val platform: String,
    val url: String,
    val label: String
) {
    init {
        require(platform.isNotBlank()) { "Platform cannot be blank" }
        require(url.isNotBlank()) { "URL cannot be blank" }
        require(httpsUrlPattern.matches(url) || mailtoUrlPattern.matches(url)) {
            "URL must be a valid https:// link or mailto: email address"
        }
        require(label.isNotBlank()) { "Label cannot be blank" }
    }
}
