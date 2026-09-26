package com.dzmitryrymarau.idt.design

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider

@Preview(name = "Tablet - Landscape", device = "id:pixel_tablet")
@PreviewWrapper(IdtThemePreviewWrapperProvider::class)
public annotation class PreviewTablet

private class IdtThemePreviewWrapperProvider : PreviewWrapperProvider {

  @OptIn(ExperimentalFoundationApi::class)
  @Composable
  override fun Wrap(content: @Composable (() -> Unit)) {
    SideEffect(ComposeFoundationFlags) {
      ComposeFoundationFlags.isInheritedTextStyleEnabled = true
    }
    IdtTheme(content)
  }
}
