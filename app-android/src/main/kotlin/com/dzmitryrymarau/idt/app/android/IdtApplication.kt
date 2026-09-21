package com.dzmitryrymarau.idt.app.android

import android.app.Application
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.android.MetroApplication

class IdtApplication : Application(), MetroApplication {

  private val idtGraph by lazy { createGraphFactory<IdtGraph.Factory>().create(this) }

  override val appComponentProviders: MetroAppComponentProviders
    get() = idtGraph
}
