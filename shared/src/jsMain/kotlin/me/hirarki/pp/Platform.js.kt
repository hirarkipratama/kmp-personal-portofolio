package me.hirarki.pp

import kotlinx.browser.document
import web.navigator.navigator

class JsPlatform: Platform {
    private val userAgent = navigator.userAgent
    private val browserList = listOf("Chrome", "Firefox", "Safari", "Edge")

    override val name: String = userAgent.findAnyOf(browserList, ignoreCase = true)
            ?.let { (startIndex) -> userAgent.substring(startIndex).substringBefore(" ") }
            ?: "Unknown"
}

actual fun getPlatform(): Platform = JsPlatform()

actual fun downloadFile(path: String, filename: String) {
    val anchor = document.createElement("a") as org.w3c.dom.HTMLAnchorElement
    anchor.setAttribute("href", path)
    anchor.setAttribute("download", filename)
    document.body?.appendChild(anchor)
    anchor.click()
    document.body?.removeChild(anchor)
}

actual fun openExternalUrl(url: String) {
    val anchor = document.createElement("a") as org.w3c.dom.HTMLAnchorElement
    anchor.setAttribute("href", url)
    anchor.setAttribute("target", if (url.startsWith("mailto:")) "_self" else "_blank")
    anchor.setAttribute("rel", "noopener noreferrer")
    document.body?.appendChild(anchor)
    anchor.click()
    document.body?.removeChild(anchor)
}