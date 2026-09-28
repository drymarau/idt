package com.dzmitryrymarau.idt.ui.internal

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AllowedRangeInputTransformationTest {

  private lateinit var range: IntRange
  private lateinit var transformation: InputTransformation

  @BeforeTest
  fun setUp() {
    range = 1..19
    transformation = AllowedRangeInputTransformation(range)
  }

  @Test
  fun InputTransformation_disallows_values_outside_of_specified_range() {
    val state = TextFieldState("")
    with(transformation) {
      // Lower than allowed
      state.edit {
        append((range.first - 1).toString())
        transformInput()
        assertEquals(expected = "", actual = asCharSequence().toString())
      }
      // Higher than allowed
      state.edit {
        append((range.last + 1).toString())
        transformInput()
        assertEquals(expected = "", actual = asCharSequence().toString())
      }
      state.edit {
        append("1")
        transformInput()
        assertEquals(expected = "1", actual = asCharSequence().toString())
      }
      state.edit {
        append("0")
        transformInput()
        assertEquals(expected = "10", actual = asCharSequence().toString())
      }
      state.edit {
        replace(start = 0, end = length, text = (range.last + 1).toString())
        transformInput()
        assertEquals(expected = "10", actual = asCharSequence().toString())
      }
      state.edit {
        replace(start = 0, end = length, text = "Lorem ipsum")
        transformInput()
        assertEquals(expected = "10", actual = asCharSequence().toString())
      }
      state.edit {
        replace(start = 0, end = length, text = "0${range.last}")
        transformInput()
        assertEquals(expected = "10", actual = asCharSequence().toString())
      }
      state.edit {
        replace(start = 0, end = length, text = "+${range.last}")
        transformInput()
        assertEquals(expected = "10", actual = asCharSequence().toString())
      }
    }
  }
}
