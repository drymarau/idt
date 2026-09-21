package com.dzmitryrymarau.idt.app.android

import android.app.Application
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.android.MetroAppComponentProviders

@DependencyGraph(AppScope::class)
interface IdtGraph : MetroAppComponentProviders {

  @DependencyGraph.Factory
  fun interface Factory {

    fun create(@Provides application: Application): IdtGraph
  }
}
