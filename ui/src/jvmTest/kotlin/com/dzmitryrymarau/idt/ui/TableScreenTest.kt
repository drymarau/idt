package com.dzmitryrymarau.idt.ui

import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.dzmitryrymarau.idt.design.IdtTheme
import com.dzmitryrymarau.idt.domain.TableConfig
import com.dzmitryrymarau.idt.ui.internal.TableScreen
import junit.framework.TestCase.assertFalse
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.uuid.Uuid
import kotlinx.coroutines.CompletableDeferred

class TableScreenTest {

  private lateinit var config: TableConfig
  private lateinit var cells: SnapshotStateList<Cell>

  @BeforeTest
  fun setUp() {
    config =
      TableConfig(
        rows = 3,
        columns = 2,
      )
    cells =
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

  @Test
  fun `TableScreen displays Cells`() = runComposeUiTest {
    setContent {
      IdtTheme {
        TableScreen(
          config = config,
          cells = cells,
          onCheckedChange = { cell, checked ->
            val i = cell.row * config.columns + cell.column
            cells[i] = cells[i].copy(checked = checked)
          },
          onCellDoubleClick = {},
        )
      }
    }

    repeat(config.rows) { row ->
      repeat(config.columns) { column ->
        onNodeWithTag("$row x $column")
          .assertIsDisplayed()
          .assertHasClickAction()
          .assertTextEquals("$row x $column")
      }
    }
  }

  @Test
  fun `TableScreen allows toggling a Cell`() = runComposeUiTest {
    val doubleTapTimeout = CompletableDeferred<Long>()
    setContent {
      val doubleTapTimeoutMillis = LocalViewConfiguration.current.doubleTapTimeoutMillis
      SideEffect(doubleTapTimeoutMillis) {
        doubleTapTimeout.complete(doubleTapTimeoutMillis)
      }
      IdtTheme {
        TableScreen(
          config = config,
          cells = cells,
          onCheckedChange = { cell, checked ->
            val i = cell.row * config.columns + cell.column
            cells[i] = cells[i].copy(checked = checked)
          },
          onCellDoubleClick = {},
        )
      }
    }

    // autoAdvance doesn't play well with combinedClickable, so we'll have to advance time manually
    mainClock.autoAdvance = false
    val firstCell = onNodeWithTag("0 x 0").assertHasClickAction().performClick()
    mainClock.advanceTimeBy(doubleTapTimeout.await())
    assertTrue(cells.first().checked)
    assertEquals(
      expected = ToggleableState(true),
      actual = firstCell.fetchSemanticsNode().config[SemanticsProperties.ToggleableState],
    )
    firstCell.performClick()
    mainClock.advanceTimeBy(doubleTapTimeout.await())
    assertFalse(cells.first().checked)
    assertEquals(
      expected = ToggleableState(false),
      actual = firstCell.fetchSemanticsNode().config[SemanticsProperties.ToggleableState],
    )
  }
}
