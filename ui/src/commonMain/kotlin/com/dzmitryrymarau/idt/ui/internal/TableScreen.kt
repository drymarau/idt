package com.dzmitryrymarau.idt.ui.internal

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.style.styleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.dzmitryrymarau.idt.design.Cell
import com.dzmitryrymarau.idt.design.IdtTheme
import com.dzmitryrymarau.idt.design.PreviewTablet
import com.dzmitryrymarau.idt.domain.TableConfig
import com.dzmitryrymarau.idt.ui.Cell
import kotlin.uuid.Uuid

@Composable
internal fun TableScreen(
  config: TableConfig,
  cells: List<Cell>,
  onCheckedChange: (cell: Cell, checked: Boolean) -> Unit,
  onCellDoubleClick: (cell: Cell) -> Unit,
  modifier: Modifier = Modifier,
) {
  val windowInsets = WindowInsets.safeContent
  LazyVerticalGrid(
    columns = GridCells.Fixed(config.columns),
    contentPadding = windowInsets.asPaddingValues() + ExtraContentPadding,
    modifier =
      modifier
        .fillMaxSize()
        .styleable(style = IdtTheme.ScreenStyle)
        .consumeWindowInsets(windowInsets)
        .testTag("grid"),
  ) {
    items(cells) { cell ->
      Cell(
        text = cell.content,
        checked = cell.checked,
        onCheckedChange = { onCheckedChange(cell, it) },
        onDoubleClick = { onCellDoubleClick(cell) },
        modifier = Modifier.animateItem().testTag(cell.content),
      )
    }
  }
}

private val ExtraContentPadding = PaddingValues(vertical = 8.dp)

@PreviewTablet
@Composable
private fun TableScreenPreview() {
  val config = remember { TableConfig(rows = 10, columns = 6) }
  val cells =
    remember(config) {
      SnapshotStateList(config.rows * config.columns) {
        val row = it / config.columns
        val column = it - row * config.columns
        Cell(
          sessionId = Uuid.NIL,
          row = row,
          column = column,
          content = "$row x $column",
          checked = false,
        )
      }
    }

  TableScreen(
    config = config,
    cells = cells,
    onCheckedChange = { cell, checked ->
      val i = cell.row * config.columns + cell.column
      cells[i] = cells[i].copy(checked = checked)
    },
    onCellDoubleClick = { _ -> },
  )
}
