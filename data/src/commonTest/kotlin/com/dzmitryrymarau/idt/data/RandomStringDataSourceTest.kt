package com.dzmitryrymarau.idt.data

import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RandomStringDataSourceTest {

  private lateinit var dataSource: RandomStringDataSource

  @BeforeTest
  fun setUp() {
    // Use a seeded Random for reproducible values
    dataSource = RandomStringDataSource(random = Random(0))
  }

  @Test
  fun `generate returns a random String`() {
    assertEquals(
      expected =
        listOf(
          "0qbNCJxn",
          "OPAgguFM",
          "Ixvc5t0i",
        ),
      actual = List(3) { dataSource.generate() },
    )
  }
}
