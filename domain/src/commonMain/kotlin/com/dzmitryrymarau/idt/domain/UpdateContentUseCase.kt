package com.dzmitryrymarau.idt.domain

import com.dzmitryrymarau.idt.data.CellRepository
import kotlin.uuid.Uuid

public class UpdateContentUseCase(private val cellRepository: CellRepository) {

  public suspend operator fun invoke(sessionId: Uuid, row: Int, column: Int, content: String) {
    cellRepository.updateContent(
      sessionId = sessionId,
      row = row,
      column = column,
      content = content,
    )
  }
}
