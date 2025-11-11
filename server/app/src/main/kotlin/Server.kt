package io.github.alelk.pws.server.app

import io.github.alelk.pws.domain.book.repository.BookRepository
import io.github.alelk.pws.domain.song.repository.SongRepository
import io.github.alelk.pws.server.di.appModule
import io.github.alelk.pws.server.transport.plugins.configureMonitoring
import io.github.alelk.pws.server.transport.plugins.configureResources
import io.github.alelk.pws.server.transport.plugins.configureSerialization
import io.github.alelk.pws.server.transport.plugins.configureStatusPages
import io.github.alelk.pws.server.transport.routes.bookRoutes
import io.github.alelk.pws.server.transport.routes.songRoutes
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import org.koin.ktor.plugin.Koin
import org.koin.ktor.ext.get

fun Application.module() {
  install(Koin) { modules(appModule) }
  configureMonitoring()
  configureResources()
  configureSerialization()
  configureStatusPages()

  bookRoutes(get<BookRepository>())
  songRoutes(get<SongRepository>())
}

fun main() {
  embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

