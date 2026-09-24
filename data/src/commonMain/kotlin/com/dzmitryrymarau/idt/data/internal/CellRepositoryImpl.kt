package com.dzmitryrymarau.idt.data.internal

import com.dzmitryrymarau.idt.data.Cell
import com.dzmitryrymarau.idt.data.CellRepository
import com.dzmitryrymarau.idt.data.Database
import com.eygraber.sqldelight.androidx.driver.coroutines.asFlow
import com.eygraber.sqldelight.androidx.driver.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

internal class CellRepositoryImpl(private val database: Database) : CellRepository {

  override suspend fun populate(
    rows: Int,
    columns: Int,
    content: (row: Int, column: Int) -> String,
  ) {
    require(rows > 0) { "rows must be greater than 0." }
    require(columns > 0) { "columns must be greater than 0." }
    database.transaction {
      repeat(rows) { row ->
        repeat(columns) { column ->
          database.cellQueries.insert(row = row, column = column, content = content(row, column))
        }
      }
    }
  }

  override suspend fun updateContent(row: Int, column: Int, content: String) {
    require(row >= 0) { "row must be greater or equal to 0" }
    require(column >= 0) { "column must be greater or equal to 0" }
    require(content.isNotBlank()) { "content must not be blank." }
    database.transaction {
      database.cellQueries.updateContent(content = content, row = row, column = column)
    }
  }

  override suspend fun updatedChecked(
    row: Int,
    column: Int,
    checked: Boolean,
  ) {
    require(row >= 0) { "row must be greater or equal to 0" }
    require(column >= 0) { "column must be greater or equal to 0" }
    database.transaction {
      database.cellQueries.updateChecked(checked = checked, row = row, column = column)
    }
  }

  override suspend fun clear() {
    database.transaction {
      database.cellQueries.delete()
    }
  }

  override fun getCells(): Flow<List<Cell>> =
    database.cellQueries.select().asFlow().mapToList().distinctUntilChanged()
}
