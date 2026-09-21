package com.dzmitryrymarau.idt.convention.internal

import java.time.Clock
import java.time.LocalDateTime
import java.time.temporal.WeekFields
import kotlin.math.roundToInt

internal data class BuildMetadata(
  val gitSha: String,
  val weekBasedYear: Int,
  val weekOfYear: Int,
  val dayOfWeek: Int,
  val hour: Int,
  val minute: Int,
) {

  private val beats = ((hour * 60 + minute) / 1.44).roundToInt()

  val versionCode = weekBasedYear * 1000000 + weekOfYear * 10000 + dayOfWeek * 1000 + beats
  val versionName = buildString {
    append(weekBasedYear)
    append("-W")
    if (weekOfYear < 10) append('0')
    append(weekOfYear)
    append('-')
    append(dayOfWeek)
    append('T')
    if (hour < 10) append('0')
    append(hour)
    append(':')
    if (minute < 10) append('0')
    append(minute)
  }

  constructor() : this(gitSha = "0123456789abcdef", dateTime = LocalDateTime.of(1970, 1, 1, 0, 0))

  constructor(
    gitSha: String
  ) : this(gitSha = gitSha, dateTime = LocalDateTime.now(Clock.systemUTC()))

  constructor(
    gitSha: String,
    dateTime: LocalDateTime,
  ) : this(
    gitSha = gitSha,
    weekBasedYear = dateTime[WeekFields.ISO.weekBasedYear()],
    weekOfYear = dateTime[WeekFields.ISO.weekOfWeekBasedYear()],
    dayOfWeek = dateTime[WeekFields.ISO.dayOfWeek()],
    hour = dateTime.hour,
    minute = dateTime.minute,
  )
}
