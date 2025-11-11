plugins { alias(libs.plugins.kotlinJvm) }

dependencies {
  implementation(libs.koin.ktor)
  implementation(libs.pws.domain)
  implementation(projects.server.infra)
  implementation(libs.exposed.core)
  implementation(libs.exposed.dao)
  implementation(libs.exposed.jdbc)
  implementation(libs.exposed.kotlin.datetime)
  implementation(libs.h2)
  implementation(libs.kotlinx.coroutinesCore)
}

kotlin { jvmToolchain(21) }
