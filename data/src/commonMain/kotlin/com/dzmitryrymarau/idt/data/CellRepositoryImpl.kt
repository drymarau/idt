package com.dzmitryrymarau.idt.data

import com.eygraber.sqldelight.androidx.driver.coroutines.asFlow
import com.eygraber.sqldelight.androidx.driver.coroutines.mapToList
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
public class CellRepositoryImpl(private val database: Database) : CellRepository {

  override suspend fun populate(
    rows: Int,
    columns: Int,
    content: (row: Int, column: Int) -> String,
  ): Uuid {
    require(rows > 0) { "rows must be greater than 0." }
    require(columns > 0) { "columns must be greater than 0." }
    val sessionId = Uuid.random()
    database.transaction {
      repeat(rows) { row ->
        repeat(columns) { column ->
          database.cellQueries.insert(
            sessionId = sessionId,
            row = row,
            column = column,
            content = content(row, column),
          )
        }
      }
    }
    return sessionId
  }

  override suspend fun updateContent(sessionId: Uuid, row: Int, column: Int, content: String) {
    require(row >= 0) { "row must be greater or equal to 0" }
    require(column >= 0) { "column must be greater or equal to 0" }
    require(content.isNotBlank()) { "content must not be blank." }
    database.transaction {
      database.cellQueries.updateContent(
        sessionId = sessionId,
        content = content,
        row = row,
        column = column,
      )
    }
  }

  override suspend fun updatedChecked(
    sessionId: Uuid,
    row: Int,
    column: Int,
    checked: Boolean,
  ) {
    require(row >= 0) { "row must be greater or equal to 0" }
    require(column >= 0) { "column must be greater or equal to 0" }
    database.transaction {
      database.cellQueries.updateChecked(
        sessionId = sessionId,
        checked = checked,
        row = row,
        column = column,
      )
    }
  }

  override suspend fun clear(sessionId: Uuid) {
    database.transaction {
      database.cellQueries.delete(sessionId)
    }
  }

  override suspend fun clearAll() {
    database.transaction {
      database.cellQueries.deleteAll()
    }
  }

  override fun <T : Any> getCells(
    sessionId: Uuid,
    mapper: (sessionId: Uuid, row: Int, column: Int, content: String, checked: Boolean) -> T,
  ): Flow<List<T>> =
    database.cellQueries.select(sessionId, mapper).asFlow().mapToList().distinctUntilChanged()
}
