package io.github.alelk.pws.di

import cafe.adriel.voyager.core.registry.ScreenRegistry
import io.github.alelk.pws.features.book.songs.bookSongsScreenModule
import io.github.alelk.pws.features.books.booksScreenModule
import io.github.alelk.pws.features.song.detail.songDetailScreenModule

internal fun setupNavigation() {
  ScreenRegistry {
    booksScreenModule()
    bookSongsScreenModule()
    songDetailScreenModule()
  }
}