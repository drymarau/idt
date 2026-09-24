package com.dzmitryrymarau.idt.data

import kotlinx.coroutines.flow.Flow

public interface CellRepository {

  public suspend fun populate(rows: Int, columns: Int, content: (row: Int, column: Int) -> String)

  public suspend fun updatedChecked(row: Int, column: Int, checked: Boolean)

  public suspend fun clear()

  public suspend fun getCells(): Flow<List<Cell>>
}
