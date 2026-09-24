@file:OptIn(ExperimentalAtomicApi::class)

package com.dzmitryrymarau.idt.data

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.db.SqlDriver
import app.cash.turbine.test
import com.dzmitryrymarau.idt.data.internal.CellRepositoryImpl
import com.dzmitryrymarau.idt.data.internal.Database
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.fail
import kotlinx.coroutines.test.runTest

abstract class AbstractCellRepositoryTest {

  private lateinit var driver: SqlDriver
  private lateinit var database: Database
  private lateinit var repository: CellRepository

  abstract fun createDriver(): SqlDriver

  @BeforeTest
  fun setUp() {
    driver = createDriver()
    database = Database(driver)
    repository = CellRepositoryImpl(database)
  }

  @AfterTest
  fun tearDown() {
    driver.close()
  }

  @Test
  fun `populate throws IllegalArgumentException if either rows or columns is 0 or less`() =
    runTest {
      assertFailsWith<IllegalArgumentException> {
        repository.populate(rows = 0, columns = 1) { _, _ -> fail("Should not be called") }
      }
      assertFailsWith<IllegalArgumentException> {
        repository.populate(rows = 1, columns = 0) { _, _ -> fail("Should not be called") }
      }
    }

  @Test
  fun `populate fills the table with expected values`() = runTest {
    val rows = 3
    val columns = 2
    repository.populate(rows = rows, columns = columns) { row, column ->
      (row * columns + column).toString()
    }
    assertEquals(
      expected =
        listOf(
          Cell(row = 0, column = 0, content = "0", checked = false),
          Cell(row = 0, column = 1, content = "1", checked = false),
          Cell(row = 1, column = 0, content = "2", checked = false),
          Cell(row = 1, column = 1, content = "3", checked = false),
          Cell(row = 2, column = 0, content = "4", checked = false),
          Cell(row = 2, column = 1, content = "5", checked = false),
        ),
      actual = database.cellQueries.select().awaitAsList(),
    )
  }

  @Test
  fun `updateChecked throws IllegalArgument exception if either row or column is less than 0`() =
    runTest {
      assertFailsWith<IllegalArgumentException> {
        repository.updatedChecked(row = -1, column = 0, checked = false)
      }
      assertFailsWith<IllegalArgumentException> {
        repository.updatedChecked(row = 0, column = -1, checked = false)
      }
    }

  @Test
  fun `updateChecked updates the correct cell`() = runTest {
    val rows = 1
    val columns = 1
    repository.populate(rows = rows, columns = columns) { row, column ->
      (row * columns + column).toString()
    }
    assertEquals(
      expected = listOf(Cell(row = 0, column = 0, content = "0", checked = false)),
      actual = database.cellQueries.select().awaitAsList(),
    )
    repository.updatedChecked(row = 0, column = 0, checked = true)
    assertEquals(
      expected = listOf(Cell(row = 0, column = 0, content = "0", checked = true)),
      actual = database.cellQueries.select().awaitAsList(),
    )
  }

  @Test
  fun `clear deletes the contents of the table`() = runTest {
    val rows = 1
    val columns = 1
    repository.populate(rows = rows, columns = columns) { row, column ->
      (row * columns + column).toString()
    }
    assertEquals(
      expected = listOf(Cell(row = 0, column = 0, content = "0", checked = false)),
      actual = database.cellQueries.select().awaitAsList(),
    )
    repository.clear()
    assertEquals(
      expected = emptyList(),
      actual = database.cellQueries.select().awaitAsList(),
    )
  }

  @Test
  fun `getCells emits a list of Cell objects`() = runTest {
    repository.getCells().test {
      // Initially empty
      assertEquals(expected = emptyList(), actual = awaitItem())
      // Single row, single column
      val rows = 1
      val columns = 1
      repository.populate(rows = rows, columns = columns) { row, column ->
        (row * columns + column).toString()
      }
      assertEquals(
        expected = listOf(Cell(row = 0, column = 0, content = "0", checked = false)),
        actual = awaitItem(),
      )
      // Set checked to true
      repository.updatedChecked(row = 0, column = 0, checked = true)
      assertEquals(
        expected = listOf(Cell(row = 0, column = 0, content = "0", checked = true)),
        actual = awaitItem(),
      )
      // Set checked to true on the same cell
      repository.updatedChecked(row = 0, column = 0, checked = true)
      expectNoEvents()
      // Set checked on a cell that's not in the table
      repository.updatedChecked(row = 1, column = 0, checked = true)
      expectNoEvents()
      // Clear the table
      repository.clear()
      assertEquals(expected = emptyList(), actual = awaitItem())
    }
  }
}
