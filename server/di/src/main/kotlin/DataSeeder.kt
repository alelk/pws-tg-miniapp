package io.github.alelk.pws.server.di

import io.github.alelk.pws.domain.book.model.BookSummary
import io.github.alelk.pws.domain.core.Locale
import io.github.alelk.pws.domain.core.NonEmptyString
import io.github.alelk.pws.domain.core.Version
import io.github.alelk.pws.domain.core.ids.BookId
import io.github.alelk.pws.domain.core.ids.SongId
import io.github.alelk.pws.domain.core.ids.SongNumberId
import io.github.alelk.pws.domain.song.model.SongSummary
import io.github.alelk.pws.server.infra.repository.SqlBookRepository
import io.github.alelk.pws.server.infra.repository.SqlSongRepository
import io.github.alelk.pws.server.infra.db.BookTable
import io.github.alelk.pws.server.infra.db.SongTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.exists

class DataSeeder(
  private val db: Database, // force DB initialization via Koin
  private val bookRepo: SqlBookRepository,
  private val songRepo: SqlSongRepository
) {
  fun seedIfEmpty() = runBlocking {
    transaction {
      val hasBooks = BookTable.selectAll().limit(1).any()
      if (!hasBooks) {
        val bookGraceId = BookId.parse("book-grace")
        val bookLightId = BookId.parse("book-light")
        val books = listOf(
          BookSummary(
            id = bookGraceId,
            version = Version.fromString("1.0"),
            locale = Locale.of("en"),
            name = "Grace Songs",
            displayShortName = "Grace",
            displayName = "Grace Songs Collection",
            countSongs = 2,
            firstSongNumberId = SongNumberId.parse("${bookGraceId}/1"),
            enabled = true,
            priority = 10
          ),
          BookSummary(
            id = bookLightId,
            version = Version.fromString("1.0"),
            locale = Locale.of("en"),
            name = "Light Songs",
            displayShortName = "Light",
            displayName = "Songs of Light",
            countSongs = 1,
            firstSongNumberId = SongNumberId.parse("${bookLightId}/1"),
            enabled = true,
            priority = 5
          )
        )
        books.forEach { bs ->
          BookTable.insert {
            it[bookId] = bs.id.toString()
            it[name] = bs.name
            it[displayShortName] = bs.displayShortName
            it[displayName] = bs.displayName
            it[priority] = bs.priority
            it[enabled] = bs.enabled
            it[countSongs] = bs.countSongs
            it[firstSongNumber] = bs.firstSongNumberId.toString()
            it[locale] = bs.locale.value
            it[version] = bs.version.toString()
          }
        }
        // Insert sample songs
        val songs = listOf(
          SongSummary(
            id = SongId(1),
            version = Version.fromString("1.0"),
            locale = Locale.of("en"),
            name = NonEmptyString("Song of Grace"),
            edited = false
          ),
          SongSummary(
            id = SongId(2),
            version = Version.fromString("1.0"),
            locale = Locale.of("en"),
            name = NonEmptyString("Shining Light"),
            edited = true
          )
        )
        songs.forEachIndexed { idx, ss ->
          SongTable.insert {
            it[songId] = ss.id.value
            it[bookId] = books.first().id.toString()
            it[numberInBook] = idx + 1
            it[name] = ss.name.value
            it[edited] = ss.edited
            it[locale] = ss.locale.value
            it[version] = ss.version.toString()
          }
        }
      }
    }
    bookRepo.refresh()
    songRepo.refresh()
  }
}
