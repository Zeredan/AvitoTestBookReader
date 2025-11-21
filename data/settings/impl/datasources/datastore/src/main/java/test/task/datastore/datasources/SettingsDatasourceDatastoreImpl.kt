package test.task.datastore.datasources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import test.task.datasources.SettingsDatasource
import test.task.settings.AvitoTheme
import javax.inject.Inject

class SettingsDatasourceDatastoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsDatasource{
    private object PreferencesKeys {
        val APP_THEME = androidx.datastore.preferences.core.stringPreferencesKey("app_theme")
    }

    override suspend fun setAppTheme(value: AvitoTheme) {
        dataStore.edit {
            it[PreferencesKeys.APP_THEME] = value.name
        }
    }
    override fun getAppThemeAsFlow(): Flow<AvitoTheme> {
        return dataStore.data.map { preferences ->
            val themeString = preferences[PreferencesKeys.APP_THEME] ?: AvitoTheme.DARK.name
            AvitoTheme.valueOf(themeString)
        }
    }
}