plugins { `kotlin-dsl` }

gradlePlugin {
  plugins {
    register("kotlinAndroidApplication") {
      id = "com.dzmitryrymarau.idt.kotlin.android.application"
      implementationClass = "com.dzmitryrymarau.idt.convention.KotlinAndroidApplicationPlugin"
    }
    register("kotlinMultiplatformLibrary") {
      id = "com.dzmitryrymarau.idt.kotlin.multiplatform.library"
      implementationClass = "com.dzmitryrymarau.idt.convention.KotlinMultiplatformLibraryPlugin"
    }
    register("kotlinCompose") {
      id = "com.dzmitryrymarau.idt.kotlin.compose"
      implementationClass = "com.dzmitryrymarau.idt.convention.KotlinComposePlugin"
    }
    register("settings") {
      id = "com.dzmitryrymarau.idt.settings"
      implementationClass = "com.dzmitryrymarau.idt.convention.SettingsPlugin"
    }
  }
}

dependencies {
  implementation(libs.plugin.android)
  implementation(libs.plugin.android.settings)
  implementation(libs.plugin.compose)
  implementation(libs.plugin.detekt)
  implementation(libs.plugin.kotlin)
  implementation(libs.plugin.kotlin.compose)
  implementation(libs.plugin.kotlin.powerAssert)
  implementation(libs.plugin.licensee)
}
