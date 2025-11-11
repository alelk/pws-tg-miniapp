package io.github.alelk.pws.server.transport.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
  install(ContentNegotiation) {
    json(Json {
      ignoreUnknownKeys = true
      explicitNulls = false
      encodeDefaults = true
    })
  }
}
