package com.dzmitryrymarau.idt.ui.internal

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.dzmitryrymarau.idt.domain.TableConfig
import com.dzmitryrymarau.idt.ui.Cell
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
internal sealed interface Screen : NavKey {

  @Serializable data object Home : Screen

  @Serializable @JvmInline value class Table(val config: TableConfig) : Screen

  @Serializable @JvmInline value class Edit(val cell: Cell) : Screen

  companion object {

    @OptIn(ExperimentalSerializationApi::class)
    val Configuration = SavedStateConfiguration {
      serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
          subclassesOfSealed(Screen.serializer())
        }
      }
    }
  }
}
