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
      implementation(libs.sqldelight.extensions.coroutines)
    }
    commonTest.dependencies {
      implementation(libs.kotlinx.coroutines.test)
      implementation(libs.turbine)
    }
    androidMain.dependencies {
      implementation(libs.sqldelight.driver.android)
    }
    androidHostTest.dependencies {
      implementation(libs.sqldelight.driver.sqlite)
    }
    jvmMain.dependencies {
      implementation(libs.sqldelight.driver.sqlite)
    }
  }
}

sqldelight {
  databases {
    register("Database") {
      packageName = "com.dzmitryrymarau.idt.data"
    }
  }
}
