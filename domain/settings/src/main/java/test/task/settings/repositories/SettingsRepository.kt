package test.task.settings.repositories

import kotlinx.coroutines.flow.Flow
import test.task.settings.AvitoTheme

interface SettingsRepository {

    suspend fun setAppTheme(value: AvitoTheme)
    fun getAppThemeAsFlow() : Flow<AvitoTheme>
}