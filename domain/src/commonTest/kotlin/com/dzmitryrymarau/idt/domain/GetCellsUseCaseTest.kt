package com.dzmitryrymarau.idt.domain

import app.cash.turbine.Turbine
import app.cash.turbine.test
import com.dzmitryrymarau.idt.data.Cell
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.Uuid
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest

class GetCellsUseCaseTest {

  private lateinit var sessionId: Uuid
  private lateinit var repository: FakeCellRepository
  private lateinit var useCase: GetCellsUseCase

  @BeforeTest
  fun setUp() {
    sessionId = Uuid.random()
    repository =
      FakeCellRepository(
        List(10) {
          Cell(
            sessionId = sessionId,
            row = Random.nextInt(Int.MAX_VALUE),
            column = Random.nextInt(Int.MAX_VALUE),
            content = Random.nextInt(Int.MAX_VALUE).toString(),
            checked = Random.nextBoolean(),
          )
        }
      )
    useCase = GetCellsUseCase(repository)
  }

  @AfterTest
  fun tearDown() {
    repository.close()
  }

  @Test
  fun `useCase correctly passes sessionId to CellRepository`() = runTest {
    useCase(sessionId, ::Cell).test {
      assertEquals(
        expected = repository.cells,
        actual = awaitItem(),
      )
    }
    assertEquals(expected = sessionId, actual = repository.sessionId.awaitItem())
  }

  private class FakeCellRepository(val cells: List<Cell>) : StubCellRepository(), AutoCloseable {

    val sessionId = Turbine<Uuid>(name = "sessionId")

    override fun <T : Any> getCells(
      sessionId: Uuid,
      mapper: (sessionId: Uuid, row: Int, column: Int, content: String, checked: Boolean) -> T,
    ): Flow<List<T>> {
      this.sessionId.add(sessionId)
      return flow {
        emit(cells.map { mapper(it.sessionId, it.row, it.column, it.content, it.checked) })
        awaitCancellation()
      }
    }

    override fun close() = sessionId.close()
  }
}
