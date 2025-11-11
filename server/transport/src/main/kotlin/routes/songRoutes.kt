package io.github.alelk.pws.server.transport.routes

import io.github.alelk.pws.api.contract.song.Songs
import io.github.alelk.pws.api.mapping.song.toDto
import io.github.alelk.pws.domain.core.ids.SongId
import io.github.alelk.pws.domain.song.repository.SongRepository
import io.ktor.server.application.Application
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.routing

fun Application.songRoutes(songRepository: SongRepository) {
  routing {
    get<Songs.ById> { res ->
      val detail = songRepository.get(SongId(res.id.value))
      if (detail == null) call.respond(status = io.ktor.http.HttpStatusCode.NotFound, mapOf("error" to "Song not found"))
      else call.respond(detail.toDto())
    }
  }
}

