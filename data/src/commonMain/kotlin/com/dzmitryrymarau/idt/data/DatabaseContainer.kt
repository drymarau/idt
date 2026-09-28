package com.dzmitryrymarau.idt.data

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.dzmitryrymarau.idt.data.internal.Database
import com.eygraber.sqldelight.androidx.driver.AndroidxSqliteDatabaseType
import com.eygraber.sqldelight.androidx.driver.AndroidxSqliteDriver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
@ContributesTo(AppScope::class)
public object DatabaseContainer {

  @Provides
  @SingleIn(AppScope::class)
  public fun provide(type: AndroidxSqliteDatabaseType): Database {
    val driver =
      AndroidxSqliteDriver(
        driver = BundledSQLiteDriver(),
        databaseType = type,
        schema = Database.Schema,
      )
    return Database(driver)
  }
}
