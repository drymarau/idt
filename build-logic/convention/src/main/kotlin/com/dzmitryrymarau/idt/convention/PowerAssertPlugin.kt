package com.dzmitryrymarau.idt.convention

import com.dzmitryrymarau.idt.convention.internal.pluginManager
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension

class PowerAssertPlugin : Plugin<Project> {

  @OptIn(ExperimentalKotlinGradlePluginApi::class)
  override fun apply(target: Project) {
    target.pluginManager { apply("org.jetbrains.kotlin.plugin.power-assert") }
    target.configure<PowerAssertGradleExtension> {
      functions.addAll(
        "kotlin.test.assertTrue",
        "kotlin.test.assertFalse",
        "kotlin.test.assertNull",
        "kotlin.test.assertNotNull",
      )
    }
    target.tasks.withType<Test> { testLogging.exceptionFormat = TestExceptionFormat.FULL }
  }
}
