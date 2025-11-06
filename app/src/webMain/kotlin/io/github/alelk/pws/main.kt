package io.github.alelk.pws

import androidx.compose.ui.ExperimentalComposeUiApi
import com.kirillNay.telegram.miniapp.compose.telegramWebApp
import com.kirillNay.telegram.miniapp.webApp.webApp
import io.github.alelk.pws.di.initKoin
import io.github.alelk.pws.di.setupNavigation

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
  initKoin()
  setupNavigation()

  telegramWebApp { style ->
    val userName = webApp.initDataUnsafe.user?.firstName + " " + webApp.initDataUnsafe.user?.lastName
    App()
  }
}