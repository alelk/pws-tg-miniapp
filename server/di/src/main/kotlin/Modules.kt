package io.github.alelk.pws.server.di

import io.github.alelk.pws.server.infra.repository.InMemoryBookRepository
import io.github.alelk.pws.server.infra.repository.InMemorySongRepository
import io.github.alelk.pws.server.infra.repository.SqlBookRepository
import io.github.alelk.pws.server.infra.repository.SqlSongRepository
import io.github.alelk.pws.server.infra.db.DatabaseFactory
import io.github.alelk.pws.server.infra.db.DbConfig
import io.github.alelk.pws.domain.book.repository.BookRepository
import io.github.alelk.pws.domain.song.repository.SongRepository
import org.koin.dsl.module

val infraModule = module {
  single { SqlBookRepository() }
  single<BookRepository> { get<SqlBookRepository>() }
  single { SqlSongRepository() }
  single<SongRepository> { get<SqlSongRepository>() }
}

val useCaseModule = module {
  // If you have specific use cases locally, define them here or rely on domain-provided ones.
}

val seedModule = module {
  single { DataSeeder(get(), get(), get()) }
}

val appModule = module {
  includes(dbModule, infraModule, useCaseModule, seedModule)
}
