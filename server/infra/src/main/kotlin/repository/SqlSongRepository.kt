package io.github.alelk.pws.server.infra.repository

import io.github.alelk.pws.domain.core.ids.BookId
import io.github.alelk.pws.domain.core.ids.SongId
import io.github.alelk.pws.domain.song.model.SongDetail
import io.github.alelk.pws.domain.song.model.SongSummary
import io.github.alelk.pws.domain.song.repository.SongRepository
import io.github.alelk.pws.server.infra.db.SongTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import io.github.alelk.pws.domain.core.NonEmptyString
import io.github.alelk.pws.domain.core.Version
import io.github.alelk.pws.domain.core.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import io.github.alelk.pws.domain.core.Year
import io.github.alelk.pws.domain.core.BibleRef
import io.github.alelk.pws.domain.song.lyric.Verse

class SqlSongRepository : SongRepository {
  private val details = MutableStateFlow<Map<SongId, SongDetail>>(emptyMap())
  private val songsInBook = MutableStateFlow<Map<BookId, Map<Int, SongSummary>>>(emptyMap())

  private fun rowToSummary(r: org.jetbrains.exposed.sql.ResultRow) = SongSummary(
    id = SongId(r[SongTable.songId]),
    version = Version.fromString(r[SongTable.version]),
    locale = Locale.of(r[SongTable.locale]),
    name = NonEmptyString(r[SongTable.name]),
    edited = r[SongTable.edited]
  )

  private fun summaryToDetail(summary: SongSummary) = SongDetail(
    id = summary.id,
    version = summary.version,
    locale = summary.locale,
    name = summary.name, // pass NonEmptyString directly
    lyric = io.github.alelk.pws.domain.song.lyric.Lyric(listOf(Verse(setOf(1), "Placeholder"))),
    author = null,
    translator = null,
    composer = null,
    tonalities = null,
    year = null,
    bibleRef = null,
    edited = summary.edited
  )

  override fun observe(id: SongId): Flow<SongDetail?> = details.map { it[id] }
  override fun observeAllInBook(bookId: BookId): Flow<Map<Int, SongSummary>> = songsInBook.map { it[bookId] ?: emptyMap() }
  override suspend fun get(id: SongId): SongDetail? = details.value[id]
  override suspend fun delete(id: SongId) { details.value = details.value - id }

  fun refresh() {
    val rows = transaction { SongTable.selectAll().toList() }
    val summaries = rows.map { rowToSummary(it) }
    songsInBook.value = rows.groupBy { BookId.parse(it[SongTable.bookId]) }
      .mapValues { (_, list) -> list.associate { it[SongTable.numberInBook] to rowToSummary(it) } }
    // populate details map from summaries
    details.value = summaries.associate { it.id to summaryToDetail(it) }
  }
}
