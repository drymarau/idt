package com.dzmitryrymarau.idt.design

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.contentPaddingHorizontal
import androidx.compose.foundation.style.disabled
import androidx.compose.foundation.style.pressed
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.dzmitryrymarau.idt.design.internal.LocalColorScheme

@Composable
public fun Button(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  content: @Composable RowScope.() -> Unit,
) {
  val interactionSource = remember { MutableInteractionSource() }
  val styleState =
    rememberUpdatedStyleState(interactionSource) {
      it.isEnabled = enabled
    }
  Row(
    verticalAlignment = Alignment.CenterVertically,
    content = content,
    modifier =
      modifier
        .clickable(
          role = Role.Button,
          onClick = onClick,
          enabled = enabled,
          indication = null,
          interactionSource = interactionSource,
        )
        .styleable(styleState, ButtonStyle),
  )
}

private val ButtonStyle: Style = Style {
  shape(IdtTheme.Shapes.Medium)
  textStyle(IdtTheme.Typography.Body)
  minHeight(56.dp)
  contentPaddingHorizontal(24.dp)
  val colorScheme = LocalColorScheme.currentValue
  background(colorScheme.accent)
  contentColor(colorScheme.onAccent)
  pressed {
    foreground(colorScheme.onAccent.copy(alpha = 0.12f))
  }
  disabled {
    background(colorScheme.accent.copy(alpha = 0.54f))
    contentColor(colorScheme.onAccent.copy(alpha = 0.54f))
  }
}

@PreviewTablet
@Composable
private fun ButtonPreview() {
  Button(onClick = {}) {
    BasicText(text = "Button")
  }
}
