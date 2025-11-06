package io.github.alelk.pws

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.alelk.pws.di.initKoin
import io.github.alelk.pws.di.setupNavigation

fun main() {
  initKoin()
  setupNavigation()
  application {
    Window(
      onCloseRequest = ::exitApplication,
      title = "pws-ui",
    ) {
      App()
    }
  }
}