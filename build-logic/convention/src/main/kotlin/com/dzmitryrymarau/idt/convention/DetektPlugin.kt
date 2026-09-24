package com.dzmitryrymarau.idt.convention

import com.dzmitryrymarau.idt.convention.internal.JvmTarget
import com.dzmitryrymarau.idt.convention.internal.libs
import com.dzmitryrymarau.idt.convention.internal.pluginManager
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.DetektCreateBaselineTask
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class DetektPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    target.pluginManager {
      apply("dev.detekt")
      withPlugin("org.jetbrains.kotlin.plugin.compose") {
        target.dependencies { "detektPlugins"(target.libs.findLibrary("detekt-compose").get()) }
      }
    }
    target.configure<DetektExtension> {
      source.setFrom(
        "src/main/kotlin",
        "src/test/kotlin",
        "src/commonMain/kotlin",
        "src/commonTest/kotlin",
        "src/androidMain/kotlin",
        "src/androidHostTest/kotlin",
      )
      config.setFrom("../config/detekt/config.yml")
      parallel.set(true)
      buildUponDefaultConfig.set(true)
    }
    target.tasks.withType<Detekt> { jvmTarget.set(JvmTarget.target) }
    target.tasks.withType<DetektCreateBaselineTask> { jvmTarget.set(JvmTarget.target) }
  }
}
