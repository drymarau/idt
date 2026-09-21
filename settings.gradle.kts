@file:Suppress("UnstableApiUsage")
rootProject.name = "IDT"

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
  }
  includeBuild("build-logic")
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google()
  }
}

plugins { id("com.dzmitryrymarau.idt.settings") }

include(":app-android")
