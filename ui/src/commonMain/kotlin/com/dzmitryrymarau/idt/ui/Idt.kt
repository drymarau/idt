package com.dzmitryrymarau.idt.ui

import androidx.annotation.CheckResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.dzmitryrymarau.idt.design.IdtTheme
import com.dzmitryrymarau.idt.ui.internal.EditScreen
import com.dzmitryrymarau.idt.ui.internal.EditViewModel
import com.dzmitryrymarau.idt.ui.internal.HomeScreen
import com.dzmitryrymarau.idt.ui.internal.Screen
import com.dzmitryrymarau.idt.ui.internal.TableScreen
import com.dzmitryrymarau.idt.ui.internal.TableViewModel
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
public fun Idt(
  viewModelFactory: MetroViewModelFactory,
  modifier: Modifier = Modifier,
) {
  CompositionLocalProvider(LocalMetroViewModelFactory provides viewModelFactory) {
    IdtTheme {
      Idt(modifier = modifier)
    }
  }
}

@Composable
private fun Idt(modifier: Modifier = Modifier) {
  val backStack = rememberNavBackStack(Screen.Configuration, Screen.Home)
  val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }
  NavDisplay(
    backStack = backStack,
    sceneStrategies = listOf(dialogStrategy),
    entryDecorators =
      listOf(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator(),
      ),
    entryProvider =
      entryProvider {
        entry<Screen.Home> {
          HomeScreen(
            onCreateTableClick =
              dropUnlessResumed {
                backStack += Screen.Table(it)
              }
          )
        }
        entry<Screen.Table> { key ->
          val viewModel =
            assistedMetroViewModel<TableViewModel, TableViewModel.Factory> {
              create(handle = it.createSavedStateHandle(), tableConfig = key.config)
            }
          val cells by viewModel.cells.collectAsStateWithLifecycle()
          TableScreen(
            config = key.config,
            cells = cells,
            onCheckedChange = viewModel::updateChecked,
            onCellDoubleClick =
              dropUnlessResumed {
                backStack += Screen.Edit(it)
              },
          )
        }
        entry<Screen.Edit>(metadata = DialogSceneStrategy.dialog()) { key ->
          val viewModel =
            assistedMetroViewModel<EditViewModel, EditViewModel.Factory> {
              create(key.cell)
            }
          EditScreen(
            content = key.cell.content,
            onConfirmClick = {
              viewModel.updateContent(it)
              backStack.removeLastOrNull()
            },
            onBackClick =
              androidx.lifecycle.compose.dropUnlessResumed {
                backStack.removeLastOrNull()
              },
          )
        }
      },
    modifier = modifier,
  )
}

// Temporary implementation until https://issuetracker.google.com/issues/482060889 is available
@CheckResult
@Composable
private fun <T> dropUnlessResumed(
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  block: (T) -> Unit,
): (T) -> Unit = {
  if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
    block(it)
  }
}
