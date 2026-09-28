package com.dzmitryrymarau.idt.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
@ContributesTo(scope = AppScope::class)
public actual object SqlDriverContainer {

  @Provides
  @SingleIn(AppScope::class)
  public fun provide(): SqlDriver =
    JdbcSqliteDriver(url = JdbcSqliteDriver.IN_MEMORY, schema = Database.Schema)
}
