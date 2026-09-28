package com.dzmitryrymarau.idt.domain

import com.dzmitryrymarau.idt.data.CellRepository
import com.dzmitryrymarau.idt.data.ContentRepository
import dev.zacsweers.metro.Inject
import kotlin.uuid.Uuid

@Inject
public class PopulateTableUseCase(
  private val cellRepository: CellRepository,
  private val contentRepository: ContentRepository,
  private val contentLength: Int = 8,
) {

  public suspend operator fun invoke(config: TableConfig): Uuid =
    cellRepository.populate(rows = config.rows, columns = config.columns) { _, _ ->
      contentRepository.generate(contentLength)
    }
}
