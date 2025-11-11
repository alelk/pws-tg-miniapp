package io.github.alelk.pws.server.infra.repository

import io.github.alelk.pws.domain.core.ids.BookId
import io.github.alelk.pws.domain.core.ids.SongId
import io.github.alelk.pws.domain.song.model.SongDetail
import io.github.alelk.pws.domain.song.model.SongSummary
import io.github.alelk.pws.domain.song.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemorySongRepository : SongRepository {
  private val songs = MutableStateFlow<Map<SongId, SongDetail>>(emptyMap())
  private val bookSongs = MutableStateFlow<Map<BookId, Map<Int, SongSummary>>>(emptyMap())

  override fun observe(id: SongId): Flow<SongDetail?> = songs.map { it[id] }
  override fun observeAllInBook(bookId: BookId): Flow<Map<Int, SongSummary>> = bookSongs.map { it[bookId] ?: emptyMap() }
  override suspend fun get(id: SongId): SongDetail? = songs.value[id]
  override suspend fun delete(id: SongId) { songs.value = songs.value - id }

  // Seed helpers
  suspend fun put(detail: SongDetail) { songs.value = songs.value + (detail.id to detail) }
  suspend fun putBookSongs(bookId: BookId, map: Map<Int, SongSummary>) { bookSongs.value = bookSongs.value + (bookId to map) }
}

