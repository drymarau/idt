package com.dzmitryrymarau.idt.design.internal

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

internal data class ColorScheme(
  val background: Color,
  val onBackground: Color,
  val primary: Color,
  val onPrimary: Color,
  val accent: Color,
  val onAccent: Color,
  val selected: Color,
  val onSelected: Color,
  val outline: Color,
) {

  companion object {

    private val Gray = Color(0xff333333)
    private val Yellow = Color(0xffffcc00)
    private val Green = Color(0xff00ff4d)

    val Light =
      ColorScheme(
        background = Color.White,
        onBackground = Gray,
        primary = Gray,
        onPrimary = Color.White,
        accent = Yellow,
        onAccent = Gray,
        selected = Green,
        onSelected = Gray,
        outline = Gray.copy(alpha = 0.32f),
      )

    val Dark =
      ColorScheme(
        background = Gray,
        onBackground = Color.White,
        primary = Color.White,
        onPrimary = Gray,
        accent = Yellow,
        onAccent = Gray,
        selected = Green,
        onSelected = Gray,
        outline = Color.White.copy(alpha = 0.32f),
      )
  }
}

internal val LocalColorScheme = compositionLocalOf<ColorScheme> { error("ColorScheme is not set.") }
