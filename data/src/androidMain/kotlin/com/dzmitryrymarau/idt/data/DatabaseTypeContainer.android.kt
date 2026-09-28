package com.dzmitryrymarau.idt.data

import android.app.Application
import com.eygraber.sqldelight.androidx.driver.AndroidxSqliteDatabaseType
import com.eygraber.sqldelight.androidx.driver.FileProvider
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@BindingContainer
@ContributesTo(scope = AppScope::class)
public actual object DatabaseTypeContainer {

  @Provides
  public fun provide(application: Application): AndroidxSqliteDatabaseType =
    AndroidxSqliteDatabaseType.FileProvider(context = application, name = "idt.db")
}
