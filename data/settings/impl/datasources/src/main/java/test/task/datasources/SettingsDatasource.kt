package test.task.datasources

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import test.task.settings.AvitoTheme

interface SettingsDatasource {

    suspend fun setAppTheme(value: AvitoTheme)
    fun getAppThemeAsFlow(): Flow<AvitoTheme>

    suspend fun setFontSize(value: Float)
    fun getFontSizeAsFlow() : Flow<Float>

    suspend fun setRowInterval(value: Float)
    fun getRowIntervalAsFlow() : Flow<Float>
}