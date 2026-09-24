package com.dzmitryrymarau.idt.data

import com.dzmitryrymarau.idt.data.internal.ContentRepositoryImpl
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ContentRepositoryTest {

  private lateinit var repository: ContentRepository

  @BeforeTest
  fun setUp() {
    repository = ContentRepositoryImpl(Random(0))
  }

  @Test
  fun `generate throws IllegalArgumentException if length is 0 or less`() {
    assertFailsWith<IllegalArgumentException> { repository.generate(0) }
    assertFailsWith<IllegalArgumentException> { repository.generate(Int.MIN_VALUE) }
  }

  @Test
  fun `generate returns a random String`() {
    assertEquals(
      expected =
        listOf(
          "0qbNCJxn",
          "OPAgguFMI",
          "xvc5t0iaHz",
        ),
      actual = List(3) { repository.generate(length = 8 + it) },
    )
  }
}
