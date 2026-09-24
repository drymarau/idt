package com.dzmitryrymarau.idt.data

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.cash.sqldelight.db.SqlDriver
import com.eygraber.sqldelight.androidx.driver.AndroidxSqliteDatabaseType
import com.eygraber.sqldelight.androidx.driver.AndroidxSqliteDriver

actual class CellRepositoryTest : AbstractCellRepositoryTest() {

  actual override fun createDriver(): SqlDriver =
    AndroidxSqliteDriver(
      driver = BundledSQLiteDriver(),
      databaseType = AndroidxSqliteDatabaseType.Memory,
      schema = Database.Schema,
    )
}
