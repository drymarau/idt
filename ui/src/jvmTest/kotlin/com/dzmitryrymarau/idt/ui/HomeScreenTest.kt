package com.dzmitryrymarau.idt.ui

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.v2.runComposeUiTest
import com.dzmitryrymarau.idt.design.IdtTheme
import com.dzmitryrymarau.idt.domain.TableConfig
import com.dzmitryrymarau.idt.ui.internal.HomeScreen
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail
import kotlinx.coroutines.CompletableDeferred

class HomeScreenTest {

  @Test
  fun `HomeScreen rejects rows outside of allowed range`() = runComposeUiTest {
    setContent {
      IdtTheme {
        HomeScreen(onCreateTableClick = { fail() })
      }
    }

    onNodeWithTag(testTag = "rows", useUnmergedTree = true).assertTextField(TableConfig.AllowedRows)
  }

  @Test
  fun `HomeScreen rejects columns outside of allowed range`() = runComposeUiTest {
    setContent {
      IdtTheme {
        HomeScreen(onCreateTableClick = { fail() })
      }
    }

    onNodeWithTag(testTag = "columns", useUnmergedTree = true)
      .assertTextField(TableConfig.AllowedColumns)
  }

  @Test
  fun `HomeScreen disables Create button if either rows or columns field is empty`() =
    runComposeUiTest {
      setContent {
        IdtTheme {
          HomeScreen(onCreateTableClick = { fail() })
        }
      }

      val button =
        onNodeWithTag("create").assertIsDisplayed().assertIsEnabled().assertHasClickAction()

      val rows = onNodeWithTag(testTag = "rows", useUnmergedTree = true)
      rows.performTextClearance()
      button.assertIsNotEnabled()

      rows.performTextReplacement(TableConfig.AllowedRows.first.toString())
      button.assertIsEnabled()

      val columns = onNodeWithTag(testTag = "columns", useUnmergedTree = true)
      columns.performTextClearance()
      button.assertIsNotEnabled()

      columns.performTextReplacement(TableConfig.AllowedColumns.first.toString())
      button.assertIsEnabled()
    }

  @Test
  fun `HomeScreen calls onCreateTableClick with correct TableConfig`() = runComposeUiTest {
    val config = CompletableDeferred<TableConfig>()
    setContent {
      IdtTheme {
        HomeScreen(onCreateTableClick = config::complete)
      }
    }

    val rows = TableConfig.AllowedRows.random()
    onNodeWithTag(testTag = "rows", useUnmergedTree = true).performTextReplacement(rows.toString())

    val columns = TableConfig.AllowedColumns.random()
    onNodeWithTag(testTag = "columns", useUnmergedTree = true)
      .performTextReplacement(columns.toString())

    onNodeWithTag("create").performClick()

    assertEquals(expected = TableConfig(rows = rows, columns = columns), actual = config.await())
  }

  private fun SemanticsNodeInteraction.assertTextField(range: IntRange) {
    assertIsDisplayed().assertTextEquals(range.last.toString())

    performTextReplacement((range.first - 1).toString())
    assertTextEquals(range.last.toString())

    performTextReplacement((range.last + 1).toString())
    assertTextEquals(range.last.toString())

    performTextReplacement("Lorem ipsum")
    assertTextEquals(range.last.toString())

    performTextReplacement("0${range.last}")
    assertTextEquals(range.last.toString())

    performTextReplacement("+${range.last}")
    assertTextEquals(range.last.toString())
  }
}
