package com.dzmitryrymarau.idt.data.internal

import com.dzmitryrymarau.idt.data.ContentRepository
import kotlin.random.Random

internal class ContentRepositoryImpl(private val random: Random = Random) : ContentRepository {

  override fun generate(length: Int): String {
    require(length > 0) { "length is less than 1." }
    return buildString(length) {
      repeat(length) {
        append(CHARS.random(random))
      }
    }
  }

  companion object {

    const val CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
  }
}
