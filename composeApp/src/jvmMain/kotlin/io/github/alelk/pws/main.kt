package io.github.alelk.pws

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "pws-ui",
    ) {
        App()
    }
}