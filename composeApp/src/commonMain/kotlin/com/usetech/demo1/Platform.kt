package com.usetech.demo1

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform