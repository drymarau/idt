package com.dzmitryrymarau.idt.convention

import app.cash.licensee.LicenseeExtension
import app.cash.licensee.SpdxId
import com.dzmitryrymarau.idt.convention.internal.pluginManager
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class LicenseePlugin : Plugin<Project> {

  override fun apply(target: Project) {
    target.pluginManager {
      apply("app.cash.licensee")
    }
    target.configure<LicenseeExtension> {
      bundleAndroidAsset.set(true)
      allow(SpdxId.Apache_20)
      allow(SpdxId.MIT)
    }
  }
}
