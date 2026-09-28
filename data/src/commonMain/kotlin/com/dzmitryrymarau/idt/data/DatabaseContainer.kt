package com.dzmitryrymarau.idt.data

import app.cash.sqldelight.db.SqlDriver
import com.dzmitryrymarau.idt.data.internal.Database
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

@BindingContainer
@ContributesTo(AppScope::class)
public object DatabaseContainer {

  @Provides
  @SingleIn(AppScope::class)
  public fun provideDatabase(driver: SqlDriver): Database = Database(driver)

  @Provides
  @SingleIn(AppScope::class)
  @Named("database")
  public fun provideCoroutineContext(): CoroutineContext = Dispatchers.IO.limitedParallelism(1)
}
