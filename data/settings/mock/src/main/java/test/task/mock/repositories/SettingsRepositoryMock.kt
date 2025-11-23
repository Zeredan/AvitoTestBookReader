package test.task.mock.repositories

import kotlinx.coroutines.flow.Flow
import test.task.settings.repositories.SettingsRepository
import test.task.settings.AvitoTheme
import javax.inject.Inject

class SettingsRepositoryMock @Inject constructor(
) : SettingsRepository{
    override suspend fun setAppTheme(value: AvitoTheme) {
        TODO("Not yet implemented")
    }

    override fun getAppThemeAsFlow(): Flow<AvitoTheme> {
        TODO("Not yet implemented")
    }

    override suspend fun setFontSize(value: Float) {
        TODO("Not yet implemented")
    }

    override fun getFontSizeAsFlow(): Flow<Float> {
        TODO("Not yet implemented")
    }

    override suspend fun setRowInterval(value: Float) {
        TODO("Not yet implemented")
    }

    override fun getRowIntervalAsFlow(): Flow<Float> {
        TODO("Not yet implemented")
    }

}