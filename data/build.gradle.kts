import com.android.build.api.variant.HasUnitTest

plugins {
  alias(libs.plugins.kotlin.multiplatform.library)
  alias(libs.plugins.sqldelight)
  alias(libs.plugins.metro)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(libs.kotlinx.coroutines.core)

      implementation(libs.androidx.sqlite.bundled)
      implementation(libs.sqldelight.adapters.primitive)
      implementation(libs.sqldelight.driver.androidx)
      implementation(libs.sqldelight.extensions.coroutines)
    }
    commonTest.dependencies {
      implementation(libs.kotlinx.coroutines.test)
      implementation(libs.turbine)
    }
  }
}

sqldelight {
  databases {
    register("Database") {
      generateAsync = true
      packageName = "com.dzmitryrymarau.idt.data"
    }
  }
}

// Workaround for https://issuetracker.google.com/issues/341381075
androidComponents {
  onVariants { variant ->
    (variant as HasUnitTest).unitTest?.let { unitTest ->
      with(unitTest.runtimeConfiguration.resolutionStrategy.dependencySubstitution) {
        val version = libs.versions.androidx.sqlite.get()
        substitute(module("androidx.sqlite:sqlite-bundled:$version"))
          .using(module("androidx.sqlite:sqlite-bundled-jvm:$version"))
      }
    }
  }
}
