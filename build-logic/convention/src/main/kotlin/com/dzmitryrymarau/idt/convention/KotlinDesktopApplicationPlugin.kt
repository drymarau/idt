package com.dzmitryrymarau.idt.convention

import com.dzmitryrymarau.idt.convention.internal.pluginManager
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class KotlinDesktopApplicationPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    target.pluginManager {
      apply("org.jetbrains.kotlin.jvm")
      apply(LicenseePlugin::class)
      apply(DetektPlugin::class)
      apply(PowerAssertPlugin::class)
    }
  }
}
