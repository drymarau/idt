package com.dzmitryrymarau.idt.ui

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.v2.runComposeUiTest
import com.dzmitryrymarau.idt.design.IdtTheme
import com.dzmitryrymarau.idt.ui.internal.EditScreen
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job

class EditScreenTest {

  private lateinit var content: String

  @BeforeTest
  fun setUp() {
    content = Random.nextInt(Int.MAX_VALUE).toString()
  }

  @Test
  fun `EditScreen displays correct content`() = runComposeUiTest {
    setContent {
      IdtTheme {
        EditScreen(
          content = content,
          onConfirmClick = {},
          onBackClick = {},
        )
      }
    }

    onNodeWithTag(testTag = "content", useUnmergedTree = true).assertTextEquals(content)
  }

  @Test
  fun `EditScreen calls onBackClick when clicking on Cancel`() = runComposeUiTest {
    val onBackClick = Job()
    setContent {
      IdtTheme {
        EditScreen(
          content = content,
          onConfirmClick = {},
          onBackClick = onBackClick::complete,
        )
      }
    }

    onNodeWithTag(testTag = "back", useUnmergedTree = true)
      .assertIsEnabled()
      .assertHasClickAction()
      .performClick()
    onBackClick.join()
  }

  @Test
  fun `EditScreen calls onConfirmClick when clicking on Confirm`() = runComposeUiTest {
    val onConfirmClick = CompletableDeferred<String>()
    setContent {
      IdtTheme {
        EditScreen(
          content = content,
          onConfirmClick = onConfirmClick::complete,
          onBackClick = {},
        )
      }
    }

    val newContent = Random.nextInt(Int.MAX_VALUE).toString()
    onNodeWithTag(testTag = "content", useUnmergedTree = true).performTextReplacement(newContent)
    onNodeWithTag(testTag = "confirm", useUnmergedTree = true)
      .assertIsEnabled()
      .assertHasClickAction()
      .performClick()
    assertEquals(expected = newContent, actual = onConfirmClick.await())
  }
}
