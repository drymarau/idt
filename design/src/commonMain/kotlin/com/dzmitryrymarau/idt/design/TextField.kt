package com.dzmitryrymarau.idt.design

import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.animate
import androidx.compose.foundation.style.contentPadding
import androidx.compose.foundation.style.focused
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.dzmitryrymarau.idt.design.internal.LocalColorScheme

@Composable
public fun TextField(
  state: TextFieldState,
  label: String,
  modifier: Modifier = Modifier,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  inputTransformation: InputTransformation? = null,
) {
  val interactionSource = remember { MutableInteractionSource() }
  val styleState = rememberUpdatedStyleState(interactionSource)
  val colorScheme = LocalColorScheme.current
  BasicTextField(
    state = state,
    keyboardOptions = keyboardOptions,
    inputTransformation = inputTransformation,
    textStyle = IdtTheme.Typography.Title.merge(color = colorScheme.onBackground),
    cursorBrush = SolidColor(colorScheme.accent),
    interactionSource = interactionSource,
    decorator = {
      Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.width(IntrinsicSize.Max),
      ) {
        BasicText(
          text = label,
          modifier = Modifier.fillMaxWidth().styleable(style = LabelStyle),
        )
        Box(
          contentAlignment = Alignment.CenterStart,
          modifier =
            Modifier.fillMaxWidth().styleable(styleState = styleState, style = TextFieldStyle),
        ) {
          it()
        }
      }
    },
    modifier = modifier,
  )
}

private val LabelStyle = Style {
  contentPaddingStart(4.dp)
  textStyle(IdtTheme.Typography.Body)
  contentColor(LocalColorScheme.currentValue.onBackground)
}

private val TextFieldStyle = Style {
  shape(IdtTheme.Shapes.Medium)
  contentPadding(horizontal = 16.dp, vertical = 1.dp)
  minHeight(56.dp)
  borderWidth(1.dp)
  val colorScheme = LocalColorScheme.currentValue
  borderColor(colorScheme.onBackground)
  contentColor(colorScheme.onBackground)
  focused {
    animate(tween()) {
      contentPadding(horizontal = 15.dp, vertical = 0.dp)
      borderWidth(2.dp)
      borderColor(colorScheme.accent)
    }
  }
}

@PreviewTablet
@Composable
private fun TextFieldPreview() {
  val state = rememberTextFieldState("1")
  TextField(state = state, label = "Label", modifier = Modifier.width(192.dp))
}
