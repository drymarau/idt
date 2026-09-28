package com.dzmitryrymarau.idt.design

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.checked
import androidx.compose.foundation.style.contentPaddingHorizontal
import androidx.compose.foundation.style.pressed
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.dzmitryrymarau.idt.design.internal.LocalColorScheme

@Composable
public fun Cell(
  text: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  onDoubleClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
) {
  val interactionSource = remember { MutableInteractionSource() }
  val styleState =
    rememberUpdatedStyleState(interactionSource) {
      it.isEnabled = enabled
      it.isChecked = checked
    }
  Box(
    contentAlignment = Alignment.Center,
    modifier =
      modifier
        .semantics {
          toggleableState = ToggleableState(checked)
        }
        .combinedClickable(
          enabled = enabled,
          onClick = { onCheckedChange(!checked) },
          onDoubleClick = onDoubleClick,
          interactionSource = interactionSource,
          indication = null,
        )
        .styleable(styleState, CellStyle),
  ) {
    BasicText(text = text, maxLines = 1, style = IdtTheme.Typography.Title)
  }
}

private val CellStyle = Style {
  textStyle(IdtTheme.Typography.Title)
  minHeight(56.dp)
  contentPaddingHorizontal(8.dp)
  borderWidth(0.dp)
  val colorScheme = LocalColorScheme.currentValue
  borderColor(colorScheme.outline)
  contentColor(colorScheme.onBackground)
  pressed {
    borderColor(Color.Transparent)
    foreground(colorScheme.selected.copy(alpha = 0.54f))
  }
  checked {
    background(colorScheme.selected)
    borderColor(Color.Transparent)
    contentColor(colorScheme.onSelected)
    pressed {
      borderColor(Color.Transparent)
      foreground(colorScheme.onSelected.copy(alpha = 0.54f))
    }
  }
}

@PreviewTablet
@Composable
private fun CellPreview() {
  var checked by remember { mutableStateOf(false) }
  Cell(text = "Cell", checked = checked, onCheckedChange = { checked = it }, onDoubleClick = {})
}
