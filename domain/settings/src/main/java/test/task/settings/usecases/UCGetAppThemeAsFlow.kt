package test.task.settings.usecases

import test.task.settings.repositories.SettingsRepository
import javax.inject.Inject

class UCGetAppThemeAsFlow @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke() = settingsRepository.getAppThemeAsFlow()
}