package io.github.alelk.pws.app

import io.github.alelk.pws.domain.book.model.BookDetail
import io.github.alelk.pws.domain.book.model.BookSummary
import io.github.alelk.pws.domain.book.query.BookQuery
import io.github.alelk.pws.domain.book.query.BookSort
import io.github.alelk.pws.domain.book.repository.BookRepository
import io.github.alelk.pws.domain.book.usecase.ObserveBooksUseCase
import io.github.alelk.pws.domain.core.BibleRef
import io.github.alelk.pws.domain.core.Locale
import io.github.alelk.pws.domain.core.NonEmptyString
import io.github.alelk.pws.domain.core.Version
import io.github.alelk.pws.domain.core.Year
import io.github.alelk.pws.domain.core.ids.BookId
import io.github.alelk.pws.domain.core.ids.SongId
import io.github.alelk.pws.domain.core.ids.SongNumberId
import io.github.alelk.pws.domain.cross.usecase.ObserveBookWithSongsUseCase
import io.github.alelk.pws.domain.person.Person
import io.github.alelk.pws.domain.song.lyric.Chorus
import io.github.alelk.pws.domain.song.lyric.Lyric
import io.github.alelk.pws.domain.song.lyric.Verse
import io.github.alelk.pws.domain.song.model.*
import io.github.alelk.pws.domain.song.repository.SongRepository
import io.github.alelk.pws.domain.song.usecase.ObserveSongUseCase
import io.github.alelk.pws.domain.tonality.Tonality
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.koin.dsl.module

// Mock data
private val bookIds = listOf(BookId.parse("B1"), BookId.parse("B2"))
private val booksSummary = bookIds.mapIndexed { idx, id ->
  BookSummary(
    id = id,
    version = Version(1, 0),
    locale = Locale.RU,
    name = "Сборник ${idx + 1}",
    displayShortName = "Сб${idx + 1}",
    displayName = "Сборник ${idx + 1}",
    countSongs = if (idx == 0) 3 else 2,
    firstSongNumberId = SongNumberId.parse("B1/1"),
    enabled = true,
    priority = 100 - (if (idx == 0) 3 else 2)
  )
}
private val booksDetail = booksSummary.map {
  BookDetail(
    id = it.id,
    version = Version(1, 0),
    locale = Locale.RU,
    name = NonEmptyString(it.name),
    displayShortName = NonEmptyString(it.displayShortName),
    displayName = NonEmptyString(it.displayName),
    countSongs = it.countSongs,
    firstSongNumberId = SongNumberId.parse("B1/1"),
    enabled = it.enabled,
    priority = 100 - it.countSongs
  )
}
private val booksFlow = MutableStateFlow(booksSummary)
private val bookDetailMap = booksDetail.associateBy { it.id }

private val songsMap: Map<BookId, Map<Int, SongSummary>> = mapOf(
  bookIds[0] to mapOf(
    1 to SongSummary(SongId(1), Version(1, 0), Locale.RU, NonEmptyString("Песня 1"), edited = true),
    2 to SongSummary(SongId(2), Version(1, 0), Locale.RU, NonEmptyString("Песня 2")),
    3 to SongSummary(SongId(3), Version(1, 0), Locale.RU, NonEmptyString("Песня 3"))
  ),
  bookIds[1] to mapOf(
    1 to SongSummary(SongId(4), Version(1, 0), Locale.RU, NonEmptyString("Песня 4")),
    2 to SongSummary(SongId(5), Version(1, 0), Locale.RU, NonEmptyString("Песня 5"))
  )
)

// Mock repositories
private class MockBookRepository : BookRepository {
  override fun observeBook(id: BookId): Flow<BookDetail?> = booksFlow.map { bookDetailMap[id] }
  override fun observeBooks(query: BookQuery, sort: BookSort): Flow<List<BookSummary>> = booksFlow
  override suspend fun get(id: BookId): BookDetail? = bookDetailMap[id]
}

private class MockSongRepository : SongRepository {
  override fun observe(id: SongId): Flow<SongDetail?> = MutableStateFlow(
    SongDetail(
      id = id,
      version = Version(1, 0),
      locale = Locale.RU,
      name = NonEmptyString("Детали ${id.value}"),
      lyric = Lyric(
        listOf(
          Verse(setOf(1), "Куплет 1 текст"),
          Chorus(setOf(1), "Припев текст"),
          Verse(setOf(2), "Куплет 2 текст")
        )
      ),
      author = Person("Автор"),
      translator = Person("Перевод"),
      composer = Person("Композитор"),
      tonalities = listOf(Tonality.A_FLAT_MAJOR, Tonality.B_FLAT_MAJOR),
      year = Year(2024),
      bibleRef = BibleRef("John 3:16"),
      edited = true
    )
  )

  override fun observeAllInBook(bookId: BookId): Flow<Map<Int, SongSummary>> = MutableStateFlow(songsMap[bookId] ?: emptyMap())
  override suspend fun get(id: SongId): SongDetail? = null
  override suspend fun delete(id: SongId) {}
}

val mockDataModule = module {
  single<BookRepository> { MockBookRepository() }
  single<SongRepository> { MockSongRepository() }
  single { ObserveBooksUseCase(get()) }
  single { ObserveBookWithSongsUseCase(get(), get()) }
  single { ObserveSongUseCase(get()) }
}

