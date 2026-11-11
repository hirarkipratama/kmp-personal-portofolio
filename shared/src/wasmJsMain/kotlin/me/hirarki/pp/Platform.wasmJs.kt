package me.hirarki.pp

import kotlinx.browser.document

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

actual fun downloadFile(path: String, filename: String) {
    val anchor = document.createElement("a") as org.w3c.dom.HTMLAnchorElement
    anchor.setAttribute("href", path)
    anchor.setAttribute("download", filename)
    document.body?.appendChild(anchor)
    anchor.click()
    document.body?.removeChild(anchor)
}