import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.composeHotReload)
}

kotlin {

  jvm()

  js(IR) {
    browser {}
    binaries.executable()
  }

  sourceSets {
    commonMain.dependencies {

      implementation(projects.features)
      implementation(projects.core.navigation)

      implementation(libs.pws.domain)

      implementation(libs.voyager.koin)
      implementation(libs.koin.compose)


      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.ui)
      implementation(compose.components.resources)
      implementation(compose.components.uiToolingPreview)
      implementation(libs.androidx.lifecycle.viewmodelCompose)
      implementation(libs.androidx.lifecycle.runtimeCompose)
      implementation(projects.shared)
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
    }
    jvmMain.dependencies {
      implementation(compose.desktop.currentOs)
      implementation(libs.kotlinx.coroutinesSwing)
    }
    jsMain {
      dependencies {
        implementation("io.github.kirillNay:tg-mini-app:1.2.2")
      }
    }
  }
}

compose.desktop {
  application {
    mainClass = "io.github.alelk.pws.MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "io.github.alelk.pws"
      packageVersion = "1.0.0"
    }
  }
}

compose.experimental {
  web.application {}
}
