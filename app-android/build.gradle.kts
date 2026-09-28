plugins {
  alias(libs.plugins.kotlin.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.metro)
}

android.defaultConfig.applicationId = "com.dzmitryrymarau.idt"

dependencies {
  implementation(project(":data"))
  implementation(project(":domain"))
  implementation(project(":ui"))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.core.ktx)
  implementation(libs.metrox.android)
  implementation(libs.metrox.viewmodel.compose)
}
