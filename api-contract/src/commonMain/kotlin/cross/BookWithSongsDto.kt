package io.github.alelk.pws.api.contract.cross

import io.github.alelk.pws.api.contract.book.BookDetailDto
import io.github.alelk.pws.api.contract.song.SongSummaryDto

data class BookWithSongsDto(
  val book: BookDetailDto,
  val songs: Map<Int, SongSummaryDto>
)