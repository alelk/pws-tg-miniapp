package io.github.alelk.pws.server.infra.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

data class DbConfig(
  val url: String = "jdbc:h2:mem:pws;DB_CLOSE_DELAY=-1",
  val driver: String = "org.h2.Driver",
  val user: String = "sa",
  val password: String = ""
)

object DatabaseFactory {
  fun init(config: DbConfig): Database {
    val db = Database.connect(
      url = config.url,
      driver = config.driver,
      user = config.user,
      password = config.password
    )
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ
    return db
  }

  fun initAndMigrate(config: DbConfig): Database {
    val db = init(config)
    transaction(db) {
      SchemaUtils.createMissingTablesAndColumns(BookTable, SongTable)
    }
    return db
  }
}
