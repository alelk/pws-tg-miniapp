package io.github.alelk.pws.server.infra.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object BookTable : IntIdTable("books") {
  val bookId = varchar("book_id", 64).uniqueIndex()
  val name = varchar("name", 200)
  val displayShortName = varchar("display_short_name", 50)
  val displayName = varchar("display_name", 200)
  val priority = integer("priority")
  val enabled = bool("enabled")
  val countSongs = integer("count_songs")
  val firstSongNumber = varchar("first_song_number", 32)
  val locale = varchar("locale", 16)
  val version = varchar("version", 32)
}

object SongTable : IntIdTable("songs") {
  val songId = long("song_id").uniqueIndex()
  val bookId = varchar("book_id", 64).index()
  val numberInBook = integer("number_in_book").index()
  val name = varchar("name", 200)
  val edited = bool("edited")
  val locale = varchar("locale", 16)
  val version = varchar("version", 32)
}

