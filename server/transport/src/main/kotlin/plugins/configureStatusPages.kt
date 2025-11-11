package io.github.alelk.pws.server.transport.plugins

import io.github.alelk.pws.api.contract.core.error.ErrorDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.configureStatusPages() {
  install(StatusPages) {
    exception<Throwable> { call, cause ->
      call.respond(HttpStatusCode.InternalServerError, ErrorDto(code = "INTERNAL", message = cause.message ?: "Unknown error"))
    }
  }
}
