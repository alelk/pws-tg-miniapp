package io.github.alelk.pws.api.contract.book

import io.github.alelk.pws.api.contract.core.ids.BookIdDto
import kotlinx.serialization.Serializable

@Serializable
data class BookSummaryDto(
  val id: BookIdDto,
  val version: String,
  val locale: String,
  val name: String,
  val displayShortName: String,
  val displayName: String,
  val countSongs: Int,
  val firstSongNumberId: Long,
  val enabled: Boolean,
  val priority: Int,
)