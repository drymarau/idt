package com.dzmitryrymarau.idt.domain

import app.cash.turbine.Turbine
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.Uuid
import kotlinx.coroutines.test.runTest

class ClearTableUseCaseTest {

  private lateinit var sessionId: Uuid
  private lateinit var repository: FakeCellRepository
  private lateinit var useCase: ClearTableUseCase

  @BeforeTest
  fun setUp() {
    sessionId = Uuid.random()
    repository = FakeCellRepository()
    useCase = ClearTableUseCase(repository)
  }

  @AfterTest
  fun tearDown() {
    repository.close()
  }

  @Test
  fun `useCase correctly passes sessionId to CellRepository`() = runTest {
    useCase(sessionId)
    assertEquals(expected = sessionId, actual = repository.sessionId.awaitItem())
  }

  private class FakeCellRepository : StubCellRepository(), AutoCloseable {

    val sessionId = Turbine<Uuid>(name = "sessionId")

    override suspend fun clear(sessionId: Uuid) = this.sessionId.add(sessionId)

    override fun close() = sessionId.close()
  }
}
