plugins {
  alias(libs.plugins.kotlinJvm)
}


dependencies {
  implementation(libs.ktor.serverCore)
  implementation(libs.ktor.server.contentNegotiation)
  implementation(libs.ktor.server.resources)
  implementation(libs.ktor.server.callLogging)
  implementation(libs.pws.domain)
  implementation(projects.api.contract)
  implementation(projects.api.mapping)
  implementation(projects.server.di)
  implementation(libs.koin.ktor)
  implementation(libs.ktor.serialization.kotlinx.json)
  implementation(libs.ktor.server.statusPages)
}

kotlin { jvmToolchain(21) }
