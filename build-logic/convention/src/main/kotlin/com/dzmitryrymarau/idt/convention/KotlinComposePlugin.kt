package com.dzmitryrymarau.idt.convention

import com.android.build.api.dsl.ApplicationExtension
import com.dzmitryrymarau.idt.convention.internal.pluginManager
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class KotlinComposePlugin : Plugin<Project> {

  override fun apply(target: Project) {
    target.pluginManager {
      apply("org.jetbrains.compose")
      apply("org.jetbrains.kotlin.plugin.compose")
      withPlugin("com.android.application") {
        target.configure<ApplicationExtension> { buildFeatures.compose = true }
      }
    }
  }
}
