package kr.owens.phase2.todo.domain

import kr.owens.phase2.todo.data.local.SettingsDataSource

class SettingsRepositoryImpl(private val dataSource: SettingsDataSource) : SettingsRepository {
    override val darkMode = dataSource.isDarkMode

    override suspend fun setIsDarkMode(isDarkMode: Boolean) {
        dataSource.setIsDarkMode(isDarkMode)
    }
}
