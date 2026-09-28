package com.dzmitryrymarau.idt.ui

import androidx.compose.runtime.Immutable
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class Cell(
  val sessionId: Uuid,
  val row: Int,
  val column: Int,
  val content: String,
  val checked: Boolean,
)
