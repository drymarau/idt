package com.dzmitryrymarau.idt.data

import app.cash.sqldelight.db.SqlDriver

expect class CellRepositoryTest : AbstractCellRepositoryTest {

  override fun createDriver(): SqlDriver
}
