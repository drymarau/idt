package com.dzmitryrymarau.idt.domain

import com.dzmitryrymarau.idt.data.CellRepository
import kotlin.test.fail
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

abstract class StubCellRepository : CellRepository {

  override suspend fun populate(
    rows: Int,
    columns: Int,
    content: (row: Int, column: Int) -> String,
  ): Uuid = fail()

  override suspend fun updateContent(
    sessionId: Uuid,
    row: Int,
    column: Int,
    content: String,
  ): Unit = fail()

  override suspend fun updatedChecked(
    sessionId: Uuid,
    row: Int,
    column: Int,
    checked: Boolean,
  ): Unit = fail()

  override suspend fun clear(sessionId: Uuid): Unit = fail()

  override suspend fun clearAll(): Unit = fail()

  override fun <T : Any> getCells(
    sessionId: Uuid,
    mapper: (sessionId: Uuid, row: Int, column: Int, content: String, checked: Boolean) -> T,
  ): Flow<List<T>> = fail()
}
