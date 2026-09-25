package com.dzmitryrymarau.idt.domain

import com.dzmitryrymarau.idt.data.CellRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public class GetCellsUseCase(private val cellRepository: CellRepository) {

  public operator fun <T : Any> invoke(
    sessionId: Uuid,
    mapper: (sessionId: Uuid, row: Int, column: Int, content: String, checked: Boolean) -> T,
  ): Flow<List<T>> = cellRepository.getCells(sessionId, mapper)
}
