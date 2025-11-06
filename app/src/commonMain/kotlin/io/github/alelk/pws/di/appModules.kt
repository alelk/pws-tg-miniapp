package io.github.alelk.pws.di

import io.github.alelk.pws.app.mockDataModule
import io.github.alelk.pws.features.books.booksScreenModelModule
import io.github.alelk.pws.features.book.songs.bookSongsScreenModelModule
import io.github.alelk.pws.features.song.detail.songDetailScreenModelModule
import org.koin.core.module.Module

val appModules: List<Module> = listOf(
  mockDataModule,
  booksScreenModelModule,
  bookSongsScreenModelModule,
  songDetailScreenModelModule
)