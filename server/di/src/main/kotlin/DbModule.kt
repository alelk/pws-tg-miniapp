package io.github.alelk.pws.server.di

import io.github.alelk.pws.server.infra.db.DbConfig
import io.github.alelk.pws.server.infra.db.DatabaseFactory
import org.koin.dsl.module

val dbModule = module {
  single { DbConfig() }
  single { DatabaseFactory.initAndMigrate(get()) }
}

