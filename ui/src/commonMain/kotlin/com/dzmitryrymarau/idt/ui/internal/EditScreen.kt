package com.dzmitryrymarau.idt.ui.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.dzmitryrymarau.idt.design.Button
import com.dzmitryrymarau.idt.design.IdtTheme
import com.dzmitryrymarau.idt.design.PreviewTablet
import com.dzmitryrymarau.idt.design.TextField
import idt.ui.generated.resources.Res
import idt.ui.generated.resources.edit_cancel
import idt.ui.generated.resources.edit_confirm
import idt.ui.generated.resources.edit_label
import idt.ui.generated.resources.edit_title
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun EditScreen(
  content: String,
  onConfirmClick: (String) -> Unit,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier =
      modifier.fillMaxWidth().width(IntrinsicSize.Max).styleable(style = IdtTheme.DialogStyle),
  ) {
    BasicText(
      text = stringResource(Res.string.edit_title),
      modifier = Modifier.styleable(style = IdtTheme.HeadlineStyle),
    )
    val state = rememberTextFieldState(content)
    TextField(
      state = state,
      label = stringResource(Res.string.edit_label),
      modifier = Modifier.fillMaxWidth().testTag("content"),
    )
    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.End),
      modifier = Modifier.fillMaxWidth(),
    ) {
      Button(onClick = onBackClick, modifier = Modifier.testTag("back")) {
        BasicText(stringResource(Res.string.edit_cancel))
      }
      Button(
        onClick = { onConfirmClick(state.text.toString()) },
        modifier = Modifier.testTag("confirm"),
      ) {
        BasicText(stringResource(Res.string.edit_confirm))
      }
    }
  }
}

@PreviewTablet
@Composable
private fun EditScreenPreview() {
  EditScreen(
    content = "Content",
    onConfirmClick = {},
    onBackClick = {},
  )
}
