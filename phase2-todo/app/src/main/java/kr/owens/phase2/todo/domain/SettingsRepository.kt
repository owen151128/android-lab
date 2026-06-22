package kr.owens.phase2.todo.domain

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val darkMode: Flow<Boolean>
    suspend fun setIsDarkMode(isDarkMode: Boolean)
}
