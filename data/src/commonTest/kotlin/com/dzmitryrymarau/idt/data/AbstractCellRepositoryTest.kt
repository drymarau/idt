package com.dzmitryrymarau.idt.data

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.db.SqlDriver
import app.cash.turbine.test
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest

abstract class AbstractCellRepositoryTest {

  private lateinit var driver: SqlDriver
  private lateinit var database: Database
  private lateinit var repository: CellRepository

  abstract fun createDriver(): SqlDriver

  @BeforeTest
  fun setUp() {
    val cellAdapter =
      Cell.Adapter(
        rowAdapter = IntColumnAdapter,
        columnAdapter = IntColumnAdapter,
      )
    driver = createDriver()
    database = Database(driver, cellAdapter)
    repository =
      CellRepositoryImpl(
        database = database,
        dataSource = RandomStringDataSource(random = Random(0)),
      )
  }

  @AfterTest
  fun tearDown() {
    driver.close()
  }

  @Test
  fun `populate throws IllegalArgumentException if either rows or columns is 0 or less`() =
    runTest {
      assertFailsWith<IllegalArgumentException> { repository.populate(rows = 0, columns = 1) }
      assertFailsWith<IllegalArgumentException> { repository.populate(rows = 1, columns = 0) }
    }

  @Test
  fun `populate fills the table with expected values`() = runTest {
    repository.populate(rows = 3, columns = 2)
    assertEquals(
      expected =
        listOf(
          Cell(row = 0, column = 0, content = "0qbNCJxn", checked = false),
          Cell(row = 0, column = 1, content = "OPAgguFM", checked = false),
          Cell(row = 1, column = 0, content = "Ixvc5t0i", checked = false),
          Cell(row = 1, column = 1, content = "aHziLhGc", checked = false),
          Cell(row = 2, column = 0, content = "YlosHFVR", checked = false),
          Cell(row = 2, column = 1, content = "h8PFloHV", checked = false),
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
    repository.populate(rows = 1, columns = 1)
    assertEquals(
      expected = listOf(Cell(row = 0, column = 0, content = "0qbNCJxn", checked = false)),
      actual = database.cellQueries.select().awaitAsList(),
    )
    repository.updatedChecked(row = 0, column = 0, checked = true)
    assertEquals(
      expected = listOf(Cell(row = 0, column = 0, content = "0qbNCJxn", checked = true)),
      actual = database.cellQueries.select().awaitAsList(),
    )
  }

  @Test
  fun `clear deletes the contents of the table`() = runTest {
    repository.populate(rows = 1, columns = 1)
    assertEquals(
      expected = listOf(Cell(row = 0, column = 0, content = "0qbNCJxn", checked = false)),
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
      repository.populate(rows = 1, columns = 1)
      assertEquals(
        expected = listOf(Cell(row = 0, column = 0, content = "0qbNCJxn", checked = false)),
        actual = awaitItem(),
      )
      // Set checked to true
      repository.updatedChecked(row = 0, column = 0, checked = true)
      assertEquals(
        expected = listOf(Cell(row = 0, column = 0, content = "0qbNCJxn", checked = true)),
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
