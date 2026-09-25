package com.dzmitryrymarau.idt.domain

import kotlin.properties.Delegates
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TableConfigTest {

  private var validRows: Int by Delegates.notNull()
  private var validColumns: Int by Delegates.notNull()

  @BeforeTest
  fun setUp() {
    validRows = TableConfig.AllowedRows.random()
    validColumns = TableConfig.AllowedColumns.random()
  }

  @Test
  fun `creates TableConfig with valid row and column`() {
    val config = TableConfig(rows = validRows, columns = validColumns)
    assertEquals(expected = validRows, actual = config.rows)
    assertEquals(expected = validColumns, actual = config.columns)
  }

  @Test
  fun `throws IllegalArgumentException if rows is invalid`() {
    var rows = Random.nextInt(Int.MIN_VALUE..<TableConfig.AllowedRows.first)
    assertFailsWith<IllegalArgumentException> {
      TableConfig(rows = rows, columns = validColumns)
    }
    rows = Random.nextInt(TableConfig.AllowedRows.last + 1..Int.MAX_VALUE)
    assertFailsWith<IllegalArgumentException> {
      TableConfig(rows = rows, columns = validColumns)
    }
  }

  @Test
  fun `throws IllegalArgumentException if columns is invalid`() {
    var columns = Random.nextInt(Int.MIN_VALUE..<TableConfig.AllowedColumns.first)
    assertFailsWith<IllegalArgumentException> {
      TableConfig(rows = validRows, columns = columns)
    }
    columns = Random.nextInt(TableConfig.AllowedColumns.last + 1..Int.MAX_VALUE)
    assertFailsWith<IllegalArgumentException> {
      TableConfig(rows = validRows, columns = columns)
    }
  }
}
