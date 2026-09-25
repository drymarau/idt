package com.dzmitryrymarau.idt.domain

import app.cash.turbine.Turbine
import com.dzmitryrymarau.idt.data.ContentRepository
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement
import kotlin.properties.Delegates
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.Uuid
import kotlinx.coroutines.test.runTest

class PopulateTableUseCaseTest {

  private var contentLength: Int by Delegates.notNull()

  private lateinit var sessionId: Uuid
  private lateinit var cellRepository: FakeCellRepository
  private lateinit var contentRepository: FakeContentRepository
  private lateinit var useCase: PopulateTableUseCase

  @BeforeTest
  fun setUp() {
    contentLength = Random.nextInt(1..4)
    sessionId = Uuid.random()
    cellRepository = FakeCellRepository(sessionId)
    contentRepository = FakeContentRepository()
    useCase =
      PopulateTableUseCase(
        cellRepository = cellRepository,
        contentRepository = contentRepository,
        contentLength = contentLength,
      )
  }

  @AfterTest
  fun tearDown() {
    cellRepository.close()
  }

  @Test
  fun `useCase correctly populates CellRepository with data from ContentRepository`() = runTest {
    val config = TableConfig(rows = 2, columns = 3)
    assertEquals(expected = sessionId, actual = useCase(config))
    assertEquals(expected = config.rows, actual = cellRepository.rows.awaitItem())
    assertEquals(expected = config.columns, actual = cellRepository.columns.awaitItem())
    repeat(config.rows * config.columns) {
      assertEquals(
        expected = it.toStartPaddedString(contentLength),
        actual = cellRepository.cells.awaitItem(),
      )
    }
  }

  private class FakeCellRepository(private val sessionId: Uuid) :
    StubCellRepository(), AutoCloseable {

    val rows = Turbine<Int>(name = "rows")
    val columns = Turbine<Int>(name = "columns")
    val cells = Turbine<String>(name = "cells")

    override suspend fun populate(
      rows: Int,
      columns: Int,
      content: (row: Int, column: Int) -> String,
    ): Uuid {
      this.rows.add(rows)
      this.columns.add(columns)
      repeat(rows) { row ->
        repeat(columns) { column ->
          this.cells.add(content(row, column))
        }
      }
      return sessionId
    }

    override fun close() {
      rows.close()
      columns.close()
      cells.close()
    }
  }

  @OptIn(ExperimentalAtomicApi::class)
  private class FakeContentRepository : ContentRepository {

    private val value = AtomicInt(0)

    override fun generate(length: Int): String =
      value.fetchAndIncrement().toStartPaddedString(length)
  }

  companion object {

    private fun Int.toStartPaddedString(length: Int) =
      toString().padStart(length = length, padChar = '0')
  }
}
