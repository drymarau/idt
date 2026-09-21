package com.dzmitryrymarau.idt.convention

import com.android.build.api.dsl.SettingsExtension
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.resolve.RepositoriesMode
import org.gradle.kotlin.dsl.configure

class SettingsPlugin : Plugin<Settings> {

  @Suppress("UnstableApiUsage")
  override fun apply(target: Settings) {
    target.pluginManagement {
      repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
      }
    }
    target.dependencyResolutionManagement {
      repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
      repositories {
        mavenCentral()
        google()
      }
    }
    target.plugins.apply("com.android.settings")
    target.configure<SettingsExtension> {
      minSdk = 29
      targetSdk = 37
      compileSdk = 37
    }
  }
}
