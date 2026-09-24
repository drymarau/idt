package com.dzmitryrymarau.idt.data.internal

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.dzmitryrymarau.idt.data.Cell
import com.dzmitryrymarau.idt.data.Database

internal fun Database(driver: SqlDriver) =
  Database(
    driver = driver,
    cellAdapter = Cell.Adapter(rowAdapter = IntColumnAdapter, columnAdapter = IntColumnAdapter),
  )
