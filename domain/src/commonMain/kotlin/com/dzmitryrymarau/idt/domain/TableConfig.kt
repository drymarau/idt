package com.dzmitryrymarau.idt.domain

import kotlinx.serialization.Serializable

@Serializable
public data class TableConfig(
  val rows: Int,
  val columns: Int,
) {

  init {
    require(rows in AllowedRows) { "Invalid row count: $rows." }
    require(columns in AllowedColumns) { "Invalid column count: $columns." }
  }

  public companion object {

    public val AllowedRows: IntRange = 1..1000
    public val AllowedColumns: IntRange = 1..6
  }
}
