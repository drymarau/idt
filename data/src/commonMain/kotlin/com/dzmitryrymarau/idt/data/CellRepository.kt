package com.dzmitryrymarau.idt.data

import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface CellRepository {

  public suspend fun populate(
    rows: Int,
    columns: Int,
    content: (row: Int, column: Int) -> String,
  ): Uuid

  public suspend fun updateContent(sessionId: Uuid, row: Int, column: Int, content: String)

  public suspend fun updatedChecked(sessionId: Uuid, row: Int, column: Int, checked: Boolean)

  public suspend fun clear(sessionId: Uuid)

  public suspend fun clearAll()

  public fun <T : Any> getCells(
    sessionId: Uuid,
    mapper: (sessionId: Uuid, row: Int, column: Int, content: String, checked: Boolean) -> T,
  ): Flow<List<T>>
}
