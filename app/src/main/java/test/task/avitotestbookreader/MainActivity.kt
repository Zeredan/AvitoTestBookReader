package test.task.avitotestbookreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import test.task.avitotestbookreader.ui.MainNavigationRoot
import test.task.settings.AvitoTheme
import test.task.settings.usecases.UCGetAppThemeAsFlow
import test.task.ui.themes.AvitoColorScheme
import test.task.ui.themes.AvitoThemeManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var ucGetAppThemeAsFlow: UCGetAppThemeAsFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Привязываю тему из domain-слоя, чтобы всегда в :core:ui была актуальная и реактивная тема
        lifecycleScope.launch {
            ucGetAppThemeAsFlow().collect { appTheme ->
                AvitoThemeManager.colorScheme.value = when(appTheme){
                    AvitoTheme.DARK -> AvitoColorScheme.DARK
                    AvitoTheme.LIGHT -> AvitoColorScheme.LIGHT
                }
                AvitoThemeManager.colorScheme.value = when(appTheme){
                    AvitoTheme.DARK -> AvitoColorScheme.DARK
                    AvitoTheme.LIGHT -> AvitoColorScheme.LIGHT
                }
                AvitoThemeManager.isInitialized.value = true
            }
        }
        // Жду инициализацию темы, лишь потом привязываю контент
        lifecycleScope.launch {
            AvitoThemeManager.isInitialized.first { it }
            setContent {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavigationRoot(
                        modifier = Modifier.padding(innerPadding),
                        deferredsToWait = listOf()
                    )
                }
            }
        }
    }
}