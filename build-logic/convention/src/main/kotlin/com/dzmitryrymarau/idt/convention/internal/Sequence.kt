package com.dzmitryrymarau.idt.convention.internal

internal fun <T : Any> Sequence<T>.distinctUntilChanged() = sequence {
  var previous: T? = null
  for (element in this@distinctUntilChanged) {
    if (element == previous) continue
    previous = element
    yield(element)
  }
}
