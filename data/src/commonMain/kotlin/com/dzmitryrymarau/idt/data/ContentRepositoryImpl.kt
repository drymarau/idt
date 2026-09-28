package com.dzmitryrymarau.idt.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlin.random.Random

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
public class ContentRepositoryImpl(private val random: Random = Random) : ContentRepository {

  override fun generate(length: Int): String {
    require(length > 0) { "length is less than 1." }
    return buildString(length) {
      repeat(length) {
        append(CHARS.random(random))
      }
    }
  }

  private companion object {

    const val CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
  }
}
