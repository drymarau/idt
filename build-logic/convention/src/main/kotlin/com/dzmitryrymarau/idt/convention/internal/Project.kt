package com.dzmitryrymarau.idt.convention.internal

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.the

internal inline fun Project.pluginManager(block: PluginManager.() -> Unit) =
  pluginManager.apply(block)

internal inline val Project.libs
  get() = the<VersionCatalogsExtension>().named("libs")

internal inline val Project.namespace: String
  get() =
    group
      .toString()
      .splitToSequence('.')
      .plus(name.splitToSequence('-'))
      .distinctUntilChanged()
      .joinToString(".")

internal val Project.isIncludeAndroidResources
  get() =
    setOf("src/test", "src/commonTest", "src/androidUnitTest").any {
      layout.projectDirectory.dir(it).asFile.exists()
    }
