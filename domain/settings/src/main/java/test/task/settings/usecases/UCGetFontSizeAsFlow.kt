package test.task.settings.usecases

import test.task.settings.repositories.SettingsRepository
import javax.inject.Inject

class UCGetFontSizeAsFlow @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke() = settingsRepository.getFontSizeAsFlow()
}