package com.dzmitryrymarau.idt.ui.internal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzmitryrymarau.idt.domain.ClearTableUseCase
import com.dzmitryrymarau.idt.domain.GetCellsUseCase
import com.dzmitryrymarau.idt.domain.PopulateTableUseCase
import com.dzmitryrymarau.idt.domain.TableConfig
import com.dzmitryrymarau.idt.domain.UpdateCheckedUseCase
import com.dzmitryrymarau.idt.ui.Cell
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Named
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlin.uuid.Uuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@AssistedInject
public class TableViewModel(
  @Assisted private val handle: SavedStateHandle,
  @Assisted private val tableConfig: TableConfig,
  @Named("application") private val coroutineScope: CoroutineScope,
  private val populateTableUseCase: PopulateTableUseCase,
  private val getCellsUseCase: GetCellsUseCase,
  private val updateCheckedUseCase: UpdateCheckedUseCase,
  private val clearTableUseCase: ClearTableUseCase,
) : ViewModel() {

  private val sessionId = handle.getMutableStateFlow<Uuid?>(key = "sessionId", initialValue = null)

  internal val cells =
    sessionId
      .filterNotNull()
      .flatMapLatest { getCellsUseCase(sessionId = it, mapper = ::Cell) }
      .stateIn(scope = viewModelScope, started = SharingStarted.Lazily, initialValue = emptyList())

  init {
    viewModelScope.launch {
      sessionId.update { it ?: populateTableUseCase(tableConfig) }
    }
  }

  override fun onCleared() {
    coroutineScope.launch {
      sessionId.value?.let { clearTableUseCase(it) }
    }
  }

  internal fun updateChecked(cell: Cell, checked: Boolean) {
    viewModelScope.launch {
      updateCheckedUseCase(
        sessionId = cell.sessionId,
        row = cell.row,
        column = cell.column,
        checked = checked,
      )
    }
  }

  @AssistedFactory
  @ContributesIntoMap(AppScope::class)
  @ManualViewModelAssistedFactoryKey
  public fun interface Factory : ManualViewModelAssistedFactory {

    public fun create(
      @Assisted handle: SavedStateHandle,
      @Assisted tableConfig: TableConfig,
    ): TableViewModel
  }
}
