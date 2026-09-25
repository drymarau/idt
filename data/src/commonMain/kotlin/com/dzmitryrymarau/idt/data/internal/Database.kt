package com.dzmitryrymarau.idt.data.internal

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.dzmitryrymarau.idt.data.Cell
import com.dzmitryrymarau.idt.data.Database
import kotlin.uuid.Uuid

internal fun Database(driver: SqlDriver) =
  Database(
    driver = driver,
    cellAdapter =
      Cell.Adapter(
        sessionIdAdapter =
          object : ColumnAdapter<Uuid, String> {
            override fun decode(databaseValue: String): Uuid = Uuid.parseHexDash(databaseValue)

            override fun encode(value: Uuid): String = value.toHexDashString()
          },
        rowAdapter = IntColumnAdapter,
        columnAdapter = IntColumnAdapter,
      ),
  )
