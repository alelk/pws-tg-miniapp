plugins {
  alias(libs.plugins.kotlinJvm)
  application
}

dependencies {
  implementation(libs.pws.domain)

  implementation(libs.ktor.serverCore)
  implementation(libs.ktor.serverNetty)
  implementation(libs.ktor.server.contentNegotiation)
  implementation(libs.ktor.server.resources)
  implementation(libs.ktor.server.callLogging)
  implementation(libs.ktor.serialization.kotlinx.json)
  implementation(libs.koin.ktor)
  implementation(libs.logback.classic)

  implementation(projects.api.contract)
  implementation(projects.api.mapping)
  implementation(projects.server.transport)
  implementation(projects.server.di)

  implementation(libs.exposed.core)
  implementation(libs.exposed.jdbc)
  implementation(libs.h2)
}

application {
  mainClass.set("io.github.alelk.pws.server.app.ServerKt")
}

kotlin { jvmToolchain(21) }
