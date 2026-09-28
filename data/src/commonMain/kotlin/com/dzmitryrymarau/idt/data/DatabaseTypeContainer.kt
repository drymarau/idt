package com.dzmitryrymarau.idt.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo

@BindingContainer @ContributesTo(AppScope::class) public expect object DatabaseTypeContainer
