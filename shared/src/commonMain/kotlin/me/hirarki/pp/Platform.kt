package me.hirarki.pp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform