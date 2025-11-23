package test.task.impl.repositories

import kotlinx.coroutines.flow.Flow
import test.task.datasources.SettingsDatasource
import test.task.settings.repositories.SettingsRepository
import test.task.settings.AvitoTheme
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDatasource: SettingsDatasource
) : SettingsRepository{
    override suspend fun setAppTheme(value: AvitoTheme) {
        settingsDatasource.setAppTheme(value)
    }

    override fun getAppThemeAsFlow(): Flow<AvitoTheme> {
        return settingsDatasource.getAppThemeAsFlow()
    }

    override suspend fun setFontSize(value: Float) {
        settingsDatasource.setFontSize(value)
    }

    override fun getFontSizeAsFlow(): Flow<Float> {
        return settingsDatasource.getFontSizeAsFlow()
    }

    override suspend fun setRowInterval(value: Float) {
        settingsDatasource.setRowInterval(value)
    }

    override fun getRowIntervalAsFlow(): Flow<Float> {
        return settingsDatasource.getRowIntervalAsFlow()
    }

}