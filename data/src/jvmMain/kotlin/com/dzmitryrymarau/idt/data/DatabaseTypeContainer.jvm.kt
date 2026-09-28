package com.dzmitryrymarau.idt.data

import com.eygraber.sqldelight.androidx.driver.AndroidxSqliteDatabaseType
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@BindingContainer
@ContributesTo(scope = AppScope::class)
public actual object DatabaseTypeContainer {

  @Provides
  public fun provide(): AndroidxSqliteDatabaseType = AndroidxSqliteDatabaseType.File("idt.db")
}
