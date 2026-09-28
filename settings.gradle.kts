@file:Suppress("UnstableApiUsage")
rootProject.name = "IDT"

pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    gradlePluginPortal()
    mavenCentral()
  }
  includeBuild("build-logic")
}

dependencyResolutionManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
  }
}

plugins { id("com.dzmitryrymarau.idt.settings") }

include(":app-android")

include(":app-desktop")

include(":data")

include(":design")

include(":domain")

include(":ui")
