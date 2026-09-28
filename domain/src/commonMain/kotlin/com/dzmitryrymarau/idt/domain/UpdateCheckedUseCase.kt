package com.dzmitryrymarau.idt.domain

import com.dzmitryrymarau.idt.data.CellRepository
import dev.zacsweers.metro.Inject
import kotlin.uuid.Uuid

@Inject
public class UpdateCheckedUseCase(private val cellRepository: CellRepository) {

  public suspend operator fun invoke(sessionId: Uuid, row: Int, column: Int, checked: Boolean) {
    cellRepository.updatedChecked(
      sessionId = sessionId,
      row = row,
      column = column,
      checked = checked,
    )
  }
}
