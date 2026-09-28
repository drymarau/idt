package com.dzmitryrymarau.idt.app.android

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.dzmitryrymarau.idt.data.CellRepository
import com.dzmitryrymarau.idt.ui.Idt
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ContributesIntoMap(AppScope::class, binding<Activity>())
@ActivityKey
class IdtActivity(
  @Named("application") private val coroutineScope: CoroutineScope,
  private val cellRepository: CellRepository,
  private val viewModelFactory: MetroViewModelFactory,
) : ComponentActivity() {

  override val defaultViewModelProviderFactory: ViewModelProvider.Factory
    get() = viewModelFactory

  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      coroutineScope.launch {
        cellRepository.clearAll()
      }
    }
    setContent {
      Idt(viewModelFactory = viewModelFactory)
    }
  }
}
