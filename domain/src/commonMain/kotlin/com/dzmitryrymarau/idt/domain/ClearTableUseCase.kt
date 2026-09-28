package com.dzmitryrymarau.idt.domain

import com.dzmitryrymarau.idt.data.CellRepository
import dev.zacsweers.metro.Inject
import kotlin.uuid.Uuid

@Inject
public class ClearTableUseCase(private val cellRepository: CellRepository) {

  public suspend operator fun invoke(sessionId: Uuid) {
    cellRepository.clear(sessionId)
  }
}
