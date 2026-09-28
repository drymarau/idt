package com.dzmitryrymarau.idt.convention

import app.cash.licensee.LicenseeExtension
import app.cash.licensee.SpdxId
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.impl.capitalizeFirstChar
import com.dzmitryrymarau.idt.convention.internal.BuildMetadata
import com.dzmitryrymarau.idt.convention.internal.isIncludeAndroidResources
import com.dzmitryrymarau.idt.convention.internal.namespace
import com.dzmitryrymarau.idt.convention.internal.pluginManager
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class KotlinAndroidApplicationPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    target.pluginManager {
      apply("com.android.application")
      apply("org.jetbrains.kotlin.plugin.parcelize")
      apply("app.cash.licensee")
      apply(DetektPlugin::class)
      apply(PowerAssertPlugin::class)
    }
    target.configure<ApplicationExtension> {
      namespace = target.namespace
      buildFeatures.buildConfig = true
      defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
      signingConfigs {
        val storeFileName = System.getenv("STORE_FILE") ?: return@signingConfigs
        register("release") {
          storeFile = target.rootProject.file(storeFileName)
          storePassword = System.getenv("STORE_PASSWORD")
          keyAlias = System.getenv("KEY_ALIAS")
          keyPassword = System.getenv("KEY_PASSWORD")
        }
      }
      buildTypes {
        release {
          isMinifyEnabled = true
          proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro",
          )
          signingConfig = signingConfigs.findByName(name)
        }
      }
      compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
      }
      lint {
        warningsAsErrors = true
        checkDependencies = true
        ignoreTestSources = true
        disable +=
          setOf(
            "Instantiatable",
            "AndroidGradlePluginVersion",
            "NewerVersionAvailable",
            "GradleDependency",
          )
      }
      testOptions.unitTests {
        isReturnDefaultValues = true
        isIncludeAndroidResources = target.isIncludeAndroidResources
      }
    }
    target.configure<ApplicationAndroidComponentsExtension> {
      val metadata =
        target.providers
          .environmentVariable("GIT_SHA")
          .map(::BuildMetadata)
          .orElse(target.provider(::BuildMetadata))
      onVariants { variant ->
        val versionCode = metadata.map(BuildMetadata::versionCode)
        val flavors = variant.productFlavors.map { it.second }
        val buildType = variant.buildType
        val versionName = metadata.map {
          buildString {
            append(it.versionName)
            append('+')
            val components = sequence {
              yield("g${it.gitSha.take(9)}")
              yieldAll(flavors)
              if (buildType != null) {
                yield(buildType)
              }
            }
            append(components.joinToString("."))
          }
        }
        variant.outputs.forEach { output ->
          output.versionCode.set(versionCode)
          output.versionName.set(versionName)
        }
        val variantName = variant.name.capitalizeFirstChar()
        target.tasks.register("print${variantName}Version") {
          doLast {
            println("versionCode: ${versionCode.get()}")
            println("versionName: ${versionName.get()}")
          }
        }
      }
    }
    target.configure<KotlinAndroidProjectExtension> {
      compilerOptions.optIn.addAll("kotlin.uuid.ExperimentalUuidApi")
      compilerOptions.progressiveMode.set(true)
      compilerOptions.freeCompilerArgs.addAll("-Xname-based-destructuring=complete")
    }
    target.configure<LicenseeExtension> {
      bundleAndroidAsset.set(true)
      allow(SpdxId.Apache_20)
      allow(SpdxId.MIT)
    }
    target.dependencies {
      val module = kotlin("test-junit")
      "testImplementation"(module)
      "androidTestImplementation"(module)
    }
  }
}
