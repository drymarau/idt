plugins {
  alias(libs.plugins.kotlin.multiplatform.library)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.metro)
}

kotlin {
  compilerOptions.optIn.addAll(
    "androidx.compose.foundation.style.ExperimentalFoundationStyleApi",
    "androidx.compose.ui.test.ExperimentalTestApi",
  )
  android.androidResources.enable = true
  sourceSets {
    commonMain.dependencies {
      api(project(":design"))

      implementation(project(":domain"))
      implementation(libs.androidx.lifecycle.viewmodel.navigation3)
      implementation(libs.androidx.navigation3.ui)
      implementation(libs.compose.components.resources)
      implementation(libs.compose.ui.tooling.preview)
      implementation(libs.kotlinx.serialization.core)
      implementation(libs.metrox.viewmodel.compose)
    }
    commonTest.dependencies {
      implementation(libs.compose.ui.test)
    }
    jvmTest.dependencies {
      implementation(compose.desktop.currentOs)
    }
  }
}

dependencies { androidRuntimeClasspath(libs.compose.ui.tooling) }
