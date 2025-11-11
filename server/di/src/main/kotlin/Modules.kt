package io.github.alelk.pws.server.di

import io.github.alelk.pws.server.infra.repository.InMemoryBookRepository
import io.github.alelk.pws.server.infra.repository.InMemorySongRepository
import io.github.alelk.pws.domain.book.repository.BookRepository
import io.github.alelk.pws.domain.song.repository.SongRepository
import org.koin.dsl.module

val infraModule = module {
  single<BookRepository> { InMemoryBookRepository() }
  single<SongRepository> { InMemorySongRepository() }
}

val useCaseModule = module {
  // If you have specific use cases locally, define them here or rely on domain-provided ones.
}

val appModule = module {
  includes(infraModule, useCaseModule)
}

