plugins { alias(libs.plugins.kotlinJvm) }

dependencies {
  implementation(libs.koin.ktor)
  implementation(libs.pws.domain)
  implementation(projects.server.infra)
}

kotlin { jvmToolchain(21) }

