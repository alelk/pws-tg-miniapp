package io.github.alelk.pws.server.infra.repository

import io.github.alelk.pws.domain.book.model.BookDetail
import io.github.alelk.pws.domain.book.model.BookSummary
import io.github.alelk.pws.domain.book.query.BookQuery
import io.github.alelk.pws.domain.book.query.BookSort
import io.github.alelk.pws.domain.book.repository.BookRepository
import io.github.alelk.pws.domain.core.Locale
import io.github.alelk.pws.domain.core.NonEmptyString
import io.github.alelk.pws.domain.core.Version
import io.github.alelk.pws.domain.core.ids.BookId
import io.github.alelk.pws.domain.core.ids.SongNumberId
import io.github.alelk.pws.server.infra.db.BookTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class SqlBookRepository : BookRepository {
  private val summariesFlow = MutableStateFlow<List<BookSummary>>(emptyList())
  private val detailsFlow = MutableStateFlow<Map<BookId, BookDetail?>>(emptyMap())

  private fun rowToSummary(r: ResultRow) = BookSummary(
    id = BookId.parse(r[BookTable.bookId]),
    version = Version.fromString(r[BookTable.version]),
    locale = Locale.of(r[BookTable.locale]),
    name = r[BookTable.name],
    displayShortName = r[BookTable.displayShortName],
    displayName = r[BookTable.displayName],
    countSongs = r[BookTable.countSongs],
    firstSongNumberId = SongNumberId.parse(r[BookTable.firstSongNumber]),
    enabled = r[BookTable.enabled],
    priority = r[BookTable.priority]
  )

  override fun observeBook(id: BookId): Flow<BookDetail?> = detailsFlow.map { it[id] }
  override fun observeBooks(query: BookQuery, sort: BookSort): Flow<List<BookSummary>> = summariesFlow.map { list ->
    val minPr = query.minPriority
    val filtered = list.filter { bs ->
      (query.locale == null || bs.locale == query.locale) &&
        (query.enabled == null || bs.enabled == query.enabled) &&
        (minPr == null || bs.priority >= minPr)
    }
    when (sort) {
      BookSort.ByPriorityDesc -> filtered.sortedByDescending { it.priority }
      else -> filtered
    }
  }

  override suspend fun get(id: BookId): BookDetail? = detailsFlow.value[id]

  fun refresh() {
    val summaries = transaction { BookTable.selectAll().map { rowToSummary(it) } }
    summariesFlow.value = summaries
    detailsFlow.value = summaries.associate { s ->
      s.id to BookDetail(
        id = s.id,
        version = s.version,
        locale = s.locale,
        name = NonEmptyString(s.name),
        displayShortName = NonEmptyString(s.displayShortName),
        displayName = NonEmptyString(s.displayName),
        releaseDate = null,
        authors = null,
        creators = null,
        reviewers = null,
        editors = null,
        description = null,
        preface = null,
        firstSongNumberId = s.firstSongNumberId,
        countSongs = s.countSongs,
        enabled = s.enabled,
        priority = s.priority
      )
    }
  }
}
