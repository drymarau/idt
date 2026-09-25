package com.dzmitryrymarau.idt.domain

import app.cash.turbine.Turbine
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.Uuid
import kotlinx.coroutines.test.runTest

class UpdateCheckedUseCaseTest {

  private lateinit var sessionId: Uuid
  private lateinit var repository: FakeCellRepository
  private lateinit var useCase: UpdateCheckedUseCase

  @BeforeTest
  fun setUp() {
    sessionId = Uuid.random()
    repository = FakeCellRepository()
    useCase = UpdateCheckedUseCase(repository)
  }

  @AfterTest
  fun tearDown() {
    repository.close()
  }

  @Test
  fun `useCase correctly passes parameters to CellRepository`() = runTest {
    val row = Random.nextInt(Int.MAX_VALUE)
    val column = Random.nextInt(Int.MAX_VALUE)
    val checked = Random.nextBoolean()
    useCase(sessionId, row, column, checked)
    assertEquals(expected = sessionId, actual = repository.sessionId.awaitItem())
    assertEquals(expected = row, actual = repository.row.awaitItem())
    assertEquals(expected = column, actual = repository.column.awaitItem())
    assertEquals(expected = checked, actual = repository.checked.awaitItem())
  }

  private class FakeCellRepository : StubCellRepository(), AutoCloseable {

    val sessionId = Turbine<Uuid>(name = "sessionId")
    val row = Turbine<Int>(name = "row")
    val column = Turbine<Int>(name = "column")
    val checked = Turbine<Boolean>(name = "checked")

    override suspend fun updatedChecked(sessionId: Uuid, row: Int, column: Int, checked: Boolean) {
      this.sessionId.add(sessionId)
      this.row.add(row)
      this.column.add(column)
      this.checked.add(checked)
    }

    override fun close() {
      sessionId.close()
      row.close()
      column.close()
      checked.close()
    }
  }
}
