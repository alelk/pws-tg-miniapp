package io.github.alelk.pws.di

import org.koin.core.context.startKoin

fun initKoin() {
  startKoin {
    modules(appModules)
  }
}