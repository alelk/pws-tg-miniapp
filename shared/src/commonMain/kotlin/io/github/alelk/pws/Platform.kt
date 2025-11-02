package io.github.alelk.pws

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform