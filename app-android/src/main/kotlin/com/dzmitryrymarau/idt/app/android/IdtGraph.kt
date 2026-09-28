package com.dzmitryrymarau.idt.app.android

import android.app.Application
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@DependencyGraph(AppScope::class)
interface IdtGraph : MetroAppComponentProviders, ViewModelGraph {

  @Provides
  @SingleIn(AppScope::class)
  @Named("application")
  fun provideCoroutineScope(): CoroutineScope =
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

  @DependencyGraph.Factory
  fun interface Factory {

    fun create(@Provides application: Application): IdtGraph
  }
}
