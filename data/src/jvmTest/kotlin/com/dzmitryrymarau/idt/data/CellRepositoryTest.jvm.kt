package com.dzmitryrymarau.idt.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

actual class CellRepositoryTest : AbstractCellRepositoryTest() {

  actual override fun createDriver(): SqlDriver =
    JdbcSqliteDriver(
      url = JdbcSqliteDriver.IN_MEMORY,
      schema = Database.Schema,
    )
}
