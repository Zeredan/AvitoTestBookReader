package test.task.settings.usecases

import test.task.settings.AvitoTheme
import test.task.settings.repositories.SettingsRepository
import javax.inject.Inject

class UCSetAppThemeAsFlow @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(value: AvitoTheme) = settingsRepository.setAppTheme(value)
}