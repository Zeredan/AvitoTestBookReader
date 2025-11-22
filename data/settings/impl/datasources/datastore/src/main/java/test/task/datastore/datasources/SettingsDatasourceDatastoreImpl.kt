package test.task.datastore.datasources

import android.content.Context
import android.content.res.Configuration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import test.task.datasources.SettingsDatasource
import test.task.settings.AvitoTheme
import javax.inject.Inject

class SettingsDatasourceDatastoreImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
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
            val stored = preferences[PreferencesKeys.APP_THEME]

            if (stored != null) {
                return@map AvitoTheme.valueOf(stored)
            }

            val nightMode = appContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            return@map when (nightMode) {
                Configuration.UI_MODE_NIGHT_YES -> AvitoTheme.DARK
                Configuration.UI_MODE_NIGHT_NO -> AvitoTheme.LIGHT
                Configuration.UI_MODE_NIGHT_UNDEFINED -> AvitoTheme.DARK
                else -> AvitoTheme.DARK //я за темную тему
            }
        }
    }
}