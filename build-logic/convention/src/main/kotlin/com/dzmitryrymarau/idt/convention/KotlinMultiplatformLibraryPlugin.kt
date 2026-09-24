package com.dzmitryrymarau.idt.convention

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.dzmitryrymarau.idt.convention.internal.JvmTarget
import com.dzmitryrymarau.idt.convention.internal.isIncludeAndroidResources
import com.dzmitryrymarau.idt.convention.internal.namespace
import com.dzmitryrymarau.idt.convention.internal.pluginManager
import dev.detekt.gradle.plugin.DetektPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformLibraryPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    target.pluginManager {
      apply("com.android.kotlin.multiplatform.library")
      apply("org.jetbrains.kotlin.multiplatform")
      apply("org.jetbrains.kotlin.plugin.parcelize")
      apply(DetektPlugin::class)
      apply(PowerAssertPlugin::class)
    }
    target.configure<KotlinMultiplatformExtension> {
      explicitApi = ExplicitApiMode.Strict
      compilerOptions.optIn.addAll(
        "androidx.compose.foundation.style.ExperimentalFoundationStyleApi"
      )
      compilerOptions.progressiveMode.set(true)
      compilerOptions.freeCompilerArgs.addAll(
        "-Xexpect-actual-classes",
        "-Xname-based-destructuring=complete",
      )
      targets.named<KotlinMultiplatformAndroidLibraryTarget>("android") {
        namespace = target.namespace
        compilerOptions.jvmTarget.set(JvmTarget)
        withHostTest {
          isReturnDefaultValues = true
          isIncludeAndroidResources = target.isIncludeAndroidResources
        }
      }
      sourceSets.commonTest.dependencies { implementation(kotlin("test")) }
    }
  }
}
