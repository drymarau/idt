package com.dzmitryrymarau.idt.ui.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dzmitryrymarau.idt.design.Button
import com.dzmitryrymarau.idt.design.IdtTheme
import com.dzmitryrymarau.idt.design.PreviewTablet
import com.dzmitryrymarau.idt.design.TextField
import com.dzmitryrymarau.idt.domain.TableConfig
import idt.ui.generated.resources.Res
import idt.ui.generated.resources.home_button_title
import idt.ui.generated.resources.home_columns_label
import idt.ui.generated.resources.home_rows_label
import idt.ui.generated.resources.home_title
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun HomeScreen(onCreateTableClick: (TableConfig) -> Unit) {
  val windowInsetsPadding = WindowInsets.safeContent.asPaddingValues()
  Box(
    contentAlignment = Alignment.Center,
    modifier =
      Modifier.fillMaxSize()
        .styleable(style = IdtTheme.ScreenStyle)
        .padding(windowInsetsPadding)
        .consumeWindowInsets(windowInsetsPadding),
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.width(IntrinsicSize.Max),
    ) {
      BasicText(
        text = stringResource(Res.string.home_title),
        modifier = Modifier.fillMaxWidth().styleable(style = IdtTheme.HeadlineStyle),
      )
      val rowsState = rememberTextFieldState(TableConfig.AllowedRows.last.toString())
      val columnsState = rememberTextFieldState(TableConfig.AllowedColumns.last.toString())
      val keyboardOptions = remember { KeyboardOptions(keyboardType = KeyboardType.Number) }
      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        TextField(
          state = rowsState,
          label = stringResource(Res.string.home_rows_label),
          keyboardOptions = keyboardOptions,
          inputTransformation = AllowedRowsInputTransformation,
          modifier = Modifier.weight(1f).testTag("rows"),
        )
        TextField(
          state = columnsState,
          label = stringResource(Res.string.home_columns_label),
          keyboardOptions = keyboardOptions,
          inputTransformation = AllowedColumnsInputTransformation,
          modifier = Modifier.weight(1f).testTag("columns"),
        )
      }
      Button(
        onClick = {
          val config =
            TableConfig(
              rows = rowsState.text.toString().toInt(),
              columns = columnsState.text.toString().toInt(),
            )
          onCreateTableClick(config)
        },
        enabled = rowsState.text.isNotEmpty() && columnsState.text.isNotEmpty(),
        modifier = Modifier.fillMaxWidth().testTag("create"),
      ) {
        BasicText(text = stringResource(Res.string.home_button_title))
      }
    }
  }
}

private val AllowedRowsInputTransformation =
  AllowedRangeInputTransformation(TableConfig.AllowedRows)

private val AllowedColumnsInputTransformation =
  AllowedRangeInputTransformation(TableConfig.AllowedColumns)

@PreviewTablet
@Composable
private fun HomeScreenPreview() {
  HomeScreen(onCreateTableClick = {})
}
