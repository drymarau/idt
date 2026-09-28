package com.dzmitryrymarau.idt

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dzmitryrymarau.idt.ui.Idt
import dev.zacsweers.metro.createGraph

@OptIn(ExperimentalFoundationApi::class)
fun main() = application {
  val graph = createGraph<IdtGraph>()
  ComposeFoundationFlags.isInheritedTextStyleEnabled = true
  Window(onCloseRequest = ::exitApplication, title = "IDT", resizable = false) {
    Idt(viewModelFactory = graph.viewModelFactory)
  }
}
