package com.dzmitryrymarau.idt.data

import kotlin.random.Random

public class RandomStringDataSource(private val random: Random = Random) {

  public fun generate(): String =
    buildString(LENGTH) {
      repeat(LENGTH) { append(CHARS.random(random)) }
    }

  internal companion object {

    const val CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    const val LENGTH = 8
  }
}
