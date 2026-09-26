plugins {
  alias(libs.plugins.kotlin.multiplatform.library)
  alias(libs.plugins.kotlin.compose)
}

kotlin {
  android.androidResources.enable = true
  compilerOptions.optIn.addAll("androidx.compose.foundation.style.ExperimentalFoundationStyleApi")
  sourceSets {
    commonMain.dependencies {
      api(libs.compose.foundation)

      implementation(libs.compose.ui.tooling.preview)
    }
    androidMain.dependencies {
      api(libs.androidx.appcompat)
      api(libs.androidx.core.splashscreen)
    }
  }
}

dependencies { androidRuntimeClasspath(libs.compose.ui.tooling) }
