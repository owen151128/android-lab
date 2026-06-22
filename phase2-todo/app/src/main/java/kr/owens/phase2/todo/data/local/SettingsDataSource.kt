package kr.owens.phase2.todo.data.local

import kotlinx.coroutines.flow.Flow

interface SettingsDataSource {
    val isDarkMode: Flow<Boolean>
    suspend fun setIsDarkMode(isDarkMode: Boolean)
}
