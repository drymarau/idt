@file:OptIn(ExperimentalAtomicApi::class)

package com.dzmitryrymarau.idt.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.turbine.test
import com.dzmitryrymarau.idt.data.internal.CellRepositoryImpl
import com.dzmitryrymarau.idt.data.internal.Database
import com.eygraber.sqldelight.androidx.driver.coroutines.asFlow
import com.eygraber.sqldelight.androidx.driver.coroutines.mapToList
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.uuid.Uuid
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
    database.cellQueries._select().asFlow().mapToList().test {
      assertEquals(expected = emptyList(), actual = awaitItem())
      val sessionId1 =
        repository.populate(rows = rows, columns = columns) { row, column ->
          (row * columns + column).toString()
        }
      assertNotEquals(illegal = Uuid.NIL, actual = sessionId1)
      assertEquals(
        expected =
          listOf(
            Cell(sessionId = sessionId1, row = 0, column = 0, content = "0", checked = false),
            Cell(sessionId = sessionId1, row = 0, column = 1, content = "1", checked = false),
            Cell(sessionId = sessionId1, row = 1, column = 0, content = "2", checked = false),
            Cell(sessionId = sessionId1, row = 1, column = 1, content = "3", checked = false),
            Cell(sessionId = sessionId1, row = 2, column = 0, content = "4", checked = false),
            Cell(sessionId = sessionId1, row = 2, column = 1, content = "5", checked = false),
          ),
        actual = awaitItem(),
      )

      val sessionId2 =
        repository.populate(rows = rows, columns = columns) { row, column ->
          (row * columns + column).toString()
        }
      assertNotEquals(illegal = Uuid.NIL, actual = sessionId2)
      assertEquals(
        expected =
          listOf(
            Cell(sessionId = sessionId1, row = 0, column = 0, content = "0", checked = false),
            Cell(sessionId = sessionId1, row = 0, column = 1, content = "1", checked = false),
            Cell(sessionId = sessionId1, row = 1, column = 0, content = "2", checked = false),
            Cell(sessionId = sessionId1, row = 1, column = 1, content = "3", checked = false),
            Cell(sessionId = sessionId1, row = 2, column = 0, content = "4", checked = false),
            Cell(sessionId = sessionId1, row = 2, column = 1, content = "5", checked = false),
            Cell(sessionId = sessionId2, row = 0, column = 0, content = "0", checked = false),
            Cell(sessionId = sessionId2, row = 0, column = 1, content = "1", checked = false),
            Cell(sessionId = sessionId2, row = 1, column = 0, content = "2", checked = false),
            Cell(sessionId = sessionId2, row = 1, column = 1, content = "3", checked = false),
            Cell(sessionId = sessionId2, row = 2, column = 0, content = "4", checked = false),
            Cell(sessionId = sessionId2, row = 2, column = 1, content = "5", checked = false),
          ),
        actual = awaitItem(),
      )
    }
  }

  @Test
  fun `updateContent throws IllegalArgumentException if either row or column is less than 0`() =
    runTest {
      assertFailsWith<IllegalArgumentException> {
        repository.updateContent(sessionId = Uuid.NIL, row = -1, column = 0, content = "0")
      }
      assertFailsWith<IllegalArgumentException> {
        repository.updateContent(sessionId = Uuid.NIL, row = 0, column = -1, content = "0")
      }
    }

  @Test
  fun `updateContent throws IllegalArgumentException if content is blank`() = runTest {
    assertFailsWith<IllegalArgumentException> {
      repository.updateContent(sessionId = Uuid.NIL, row = -1, column = 0, content = " ")
    }
  }

  @Test
  fun `updateContent updates the correct cell`() = runTest {
    val rows = 1
    val columns = 1
    database.cellQueries._select().asFlow().mapToList().test {
      var cells = awaitItem()
      assertEquals(expected = emptyList(), actual = cells)
      val sessionId1 =
        repository.populate(rows = rows, columns = columns) { row, column ->
          (row * columns + column).toString()
        }
      assertNotEquals(illegal = Uuid.NIL, actual = sessionId1)
      cells = awaitItem()
      assertEquals(expected = "0", actual = cells.first { it.sessionId == sessionId1 }.content)

      val sessionId2 =
        repository.populate(rows = rows, columns = columns) { row, column ->
          (row * columns + column).toString()
        }
      assertNotEquals(illegal = Uuid.NIL, actual = sessionId1)
      cells = awaitItem()
      assertEquals(expected = "0", actual = cells.first { it.sessionId == sessionId2 }.content)

      repository.updateContent(sessionId = sessionId1, row = 0, column = 0, content = "1")
      cells = awaitItem()
      assertEquals(expected = "1", actual = cells.first { it.sessionId == sessionId1 }.content)
    }
  }

  @Test
  fun `updateChecked throws IllegalArgumentException if either row or column is less than 0`() =
    runTest {
      assertFailsWith<IllegalArgumentException> {
        repository.updatedChecked(sessionId = Uuid.NIL, row = -1, column = 0, checked = false)
      }
      assertFailsWith<IllegalArgumentException> {
        repository.updatedChecked(sessionId = Uuid.NIL, row = 0, column = -1, checked = false)
      }
    }

  @Test
  fun `updateChecked updates the correct cell`() = runTest {
    val rows = 1
    val columns = 1
    database.cellQueries._select().asFlow().mapToList().test {
      var cells = awaitItem()
      assertEquals(expected = emptyList(), actual = cells)
      val sessionId1 =
        repository.populate(rows = rows, columns = columns) { row, column ->
          (row * columns + column).toString()
        }
      assertNotEquals(illegal = Uuid.NIL, actual = sessionId1)
      cells = awaitItem()
      assertFalse(cells.first { it.sessionId == sessionId1 }.checked)

      val sessionId2 =
        repository.populate(rows = rows, columns = columns) { row, column ->
          (row * columns + column).toString()
        }
      assertNotEquals(illegal = Uuid.NIL, actual = sessionId1)
      cells = awaitItem()
      assertFalse(cells.first { it.sessionId == sessionId2 }.checked)

      repository.updatedChecked(sessionId = sessionId1, row = 0, column = 0, checked = true)
      cells = awaitItem()
      assertTrue(cells.first { it.sessionId == sessionId1 }.checked)
    }
  }

  @Test
  fun `clear deletes the contents of the table`() = runTest {
    val rows = 1
    val columns = 1
    database.cellQueries._select().asFlow().mapToList().test {
      var cells = awaitItem()
      assertEquals(expected = 0, actual = cells.size)

      val sessionId1 =
        repository.populate(rows = rows, columns = columns) { row, column ->
          (row * columns + column).toString()
        }
      assertNotEquals(illegal = Uuid.NIL, actual = sessionId1)
      cells = awaitItem()
      assertEquals(expected = 1, actual = cells.size)
      assertEquals(expected = 1, actual = cells.count { it.sessionId == sessionId1 })

      val sessionId2 =
        repository.populate(rows = rows, columns = columns) { row, column ->
          (row * columns + column).toString()
        }
      assertNotEquals(illegal = Uuid.NIL, actual = sessionId2)
      cells = awaitItem()
      assertEquals(expected = 2, actual = cells.size)
      assertEquals(expected = 1, actual = cells.count { it.sessionId == sessionId2 })

      repository.clear(sessionId1)
      cells = awaitItem()
      assertEquals(expected = 1, actual = cells.size)
      assertEquals(expected = 1, actual = cells.count { it.sessionId == sessionId2 })

      repository.clear(sessionId2)
      cells = awaitItem()
      assertEquals(expected = 0, actual = cells.size)
    }
  }

  @Test
  fun `getCells emits a list of Cell objects`() = runTest {
    val rows = 1
    val columns = 1
    val sessionId1 =
      repository.populate(rows = rows, columns = columns) { row, column ->
        (row * columns + column).toString()
      }
    assertNotEquals(illegal = Uuid.NIL, actual = sessionId1)
    val sessionId2 =
      repository.populate(rows = rows, columns = columns) { row, column ->
        (row * columns + column).toString()
      }
    assertNotEquals(illegal = Uuid.NIL, actual = sessionId2)
    repository.getCells(sessionId1, ::Cell).test {
      assertEquals(
        expected =
          listOf(Cell(sessionId = sessionId1, row = 0, column = 0, content = "0", checked = false)),
        actual = awaitItem(),
      )
      // Set checked to true
      repository.updatedChecked(sessionId = sessionId1, row = 0, column = 0, checked = true)
      assertEquals(
        expected =
          listOf(Cell(sessionId = sessionId1, row = 0, column = 0, content = "0", checked = true)),
        actual = awaitItem(),
      )
      // Set checked to true on the same cell
      repository.updatedChecked(sessionId = sessionId1, row = 0, column = 0, checked = true)
      expectNoEvents()
      // Set checked on a cell that's not in the table
      repository.updatedChecked(sessionId = sessionId1, row = 1, column = 0, checked = true)
      expectNoEvents()
      // Clear the table
      repository.clear(sessionId1)
      assertEquals(expected = emptyList(), actual = awaitItem())
    }
  }
}
