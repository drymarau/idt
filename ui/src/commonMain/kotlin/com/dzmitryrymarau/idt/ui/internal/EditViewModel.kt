package com.dzmitryrymarau.idt.ui.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzmitryrymarau.idt.domain.UpdateContentUseCase
import com.dzmitryrymarau.idt.ui.Cell
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.launch

@AssistedInject
public class EditViewModel(
  @Assisted private val cell: Cell,
  private val updateContentUseCase: UpdateContentUseCase,
) : ViewModel() {

  internal fun updateContent(content: String) {
    viewModelScope.launch {
      updateContentUseCase(
        sessionId = cell.sessionId,
        row = cell.row,
        column = cell.column,
        content = content,
      )
    }
  }

  @AssistedFactory
  @ContributesIntoMap(AppScope::class)
  @ManualViewModelAssistedFactoryKey
  public fun interface Factory : ManualViewModelAssistedFactory {

    public fun create(cell: Cell): EditViewModel
  }
}
