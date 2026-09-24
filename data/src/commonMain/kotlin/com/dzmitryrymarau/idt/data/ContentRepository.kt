package com.dzmitryrymarau.idt.data

public interface ContentRepository {

  public fun generate(length: Int = 8): String
}
