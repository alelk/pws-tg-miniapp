package io.github.alelk.pws

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.Navigator
import io.github.alelk.pws.app.mockDataModule
import io.github.alelk.pws.core.navigation.SharedScreens
import io.github.alelk.pws.features.book.songs.bookSongsScreenModelModule
import io.github.alelk.pws.features.book.songs.bookSongsScreenModule
import io.github.alelk.pws.features.books.booksScreenModelModule
import io.github.alelk.pws.features.books.booksScreenModule
import io.github.alelk.pws.features.song.detail.songDetailScreenModelModule
import io.github.alelk.pws.features.song.detail.songDetailScreenModule
import org.koin.compose.KoinApplication
import org.koin.dsl.koinApplication

private var screensRegistered = false

@Composable
fun App() {
  if (!screensRegistered) {
    ScreenRegistry {
      booksScreenModule()
      bookSongsScreenModule()
      songDetailScreenModule()
    }
    screensRegistered = true
  }

  // Start Koin inside composition (for simplicity). In production move to platform init.
  KoinApplication(application = {
    koinApplication {
      modules(
        mockDataModule,
        booksScreenModelModule,
        bookSongsScreenModelModule,
        songDetailScreenModelModule
      )
    }
  }) {
    MaterialTheme {
      val root = rememberScreen(SharedScreens.Books)
      Navigator(root)
    }
  }
}