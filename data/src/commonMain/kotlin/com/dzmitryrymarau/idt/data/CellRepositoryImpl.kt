package com.dzmitryrymarau.idt.data

import com.eygraber.sqldelight.androidx.driver.coroutines.asFlow
import com.eygraber.sqldelight.androidx.driver.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

public class CellRepositoryImpl(
  private val database: Database,
  private val dataSource: RandomStringDataSource,
) : CellRepository {

  override suspend fun populate(rows: Int, columns: Int) {
    require(rows > 0) { "rows must be greater than 0." }
    require(columns > 0) { "columns must be greater than 0." }
    database.transaction {
      repeat(rows) { row ->
        repeat(columns) { column ->
          database.cellQueries.insert(row = row, column = column, content = dataSource.generate())
        }
      }
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

  override suspend fun getCells(): Flow<List<Cell>> =
    database.cellQueries.select().asFlow().mapToList().distinctUntilChanged()
}
