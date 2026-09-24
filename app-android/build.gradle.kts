plugins {
  alias(libs.plugins.kotlin.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.metro)
}

android.defaultConfig.applicationId = "com.dzmitryrymarau.idt"

dependencies {
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.core.splashscreen)
  implementation(libs.metrox.android)
}
