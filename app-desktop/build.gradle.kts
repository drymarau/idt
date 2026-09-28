plugins {
  alias(libs.plugins.kotlin.desktop.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.metro)
}

dependencies {
  implementation(project(":data"))
  implementation(project(":domain"))
  implementation(project(":ui"))
  implementation(compose.desktop.currentOs)
  implementation(libs.kotlinx.coroutines.swing)
  implementation(libs.metrox.viewmodel.compose)
}

compose.desktop {
  application {
    mainClass = "com.dzmitryrymarau.idt.MainKt"
  }
}
