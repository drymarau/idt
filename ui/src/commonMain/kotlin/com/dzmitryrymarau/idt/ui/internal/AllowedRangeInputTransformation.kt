package com.dzmitryrymarau.idt.ui.internal

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer

internal class AllowedRangeInputTransformation(private val range: IntRange) : InputTransformation {

  override fun TextFieldBuffer.transformInput() {
    if (length == 0) return
    val s = asCharSequence().toString()
    if (s.startsWith('0') || s.startsWith('+')) {
      revertAllChanges()
      return
    }
    val i = s.toIntOrNull()
    if (i == null || i !in range) {
      revertAllChanges()
    }
  }
}
