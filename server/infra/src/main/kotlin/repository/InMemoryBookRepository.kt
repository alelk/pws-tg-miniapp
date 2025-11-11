package io.github.alelk.pws.server.infra.repository

import io.github.alelk.pws.domain.book.model.BookDetail
import io.github.alelk.pws.domain.book.model.BookSummary
import io.github.alelk.pws.domain.book.query.BookQuery
import io.github.alelk.pws.domain.book.query.BookSort
import io.github.alelk.pws.domain.book.repository.BookRepository
import io.github.alelk.pws.domain.core.ids.BookId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryBookRepository : BookRepository {
  private val details = MutableStateFlow<Map<BookId, BookDetail?>>(emptyMap())
  private val summaries = MutableStateFlow<List<BookSummary>>(emptyList())

  override fun observeBook(id: BookId): Flow<BookDetail?> = details.map { it[id] }
  override fun observeBooks(query: BookQuery, sort: BookSort): Flow<List<BookSummary>> = summaries.map { applySort(sort, applyQuery(query, it)) }
  override suspend fun get(id: BookId): BookDetail? = details.value[id]

  // Helpers to seed data
  suspend fun setSummaries(list: List<BookSummary>) { summaries.value = list }
  suspend fun putDetail(detail: BookDetail?) {
    if (detail != null) details.value = details.value + (detail.id to detail)
  }

  private fun applyQuery(query: BookQuery, list: List<BookSummary>): List<BookSummary> = list.filter { bs ->
    val minPr = query.minPriority
    (query.locale == null || bs.locale == query.locale) &&
      (query.enabled == null || bs.enabled == query.enabled) &&
      (minPr == null || bs.priority >= minPr)
  }

  private fun applySort(sort: BookSort, list: List<BookSummary>): List<BookSummary> = when (sort) {
    BookSort.ByPriorityDesc -> list.sortedByDescending { it.priority }
    // fallback if enum extended later
    else -> list
  }
}
