package me.hirarki.pp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun downloadFile(path: String, filename: String)

expect fun openExternalUrl(url: String)