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

}