package com.dzmitryrymarau.idt.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dzmitryrymarau.idt.design.internal.ColorScheme
import com.dzmitryrymarau.idt.design.internal.LocalColorScheme

public object IdtTheme {

  public object Typography {

    public val Body: TextStyle =
      TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 24.sp,
      )
    public val Title: TextStyle =
      TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 28.sp,
      )
    public val Headline: TextStyle =
      TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 40.sp,
      )
  }

  internal object Shapes {

    val Medium = RoundedCornerShape(8.dp)
  }

  public val ScreenStyle: Style = Style {
    textStyle(Typography.Title)
    val colorScheme = LocalColorScheme.currentValue
    background(colorScheme.background)
    contentColor(colorScheme.onBackground)
  }

  public val HeadlineStyle: Style = Style {
    textStyle(Typography.Headline)
  }

  @Composable
  public operator fun invoke(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val colorScheme =
      remember(darkTheme) {
        if (darkTheme) ColorScheme.Dark else ColorScheme.Light
      }
    CompositionLocalProvider(
      LocalColorScheme provides colorScheme,
      LocalTextSelectionColors provides
        TextSelectionColors(
          handleColor = colorScheme.accent,
          backgroundColor = colorScheme.accent.copy(alpha = 0.4f),
        ),
      content = content,
    )
  }
}
