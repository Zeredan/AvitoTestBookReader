package test.task.impl.repositories

import kotlinx.coroutines.flow.Flow
import test.task.datastore.SettingsDatasourceDatastore
import test.task.settings.repositories.SettingsRepository
import test.task.settings.AvitoTheme
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDatasourceDatastore: SettingsDatasourceDatastore
) : SettingsRepository{
    override suspend fun setAppTheme(value: AvitoTheme) {
        settingsDatasourceDatastore.setAppTheme(value)
    }

    override fun getAppThemeAsFlow(): Flow<AvitoTheme> {
        return settingsDatasourceDatastore.getAppThemeAsFlow()
    }

}