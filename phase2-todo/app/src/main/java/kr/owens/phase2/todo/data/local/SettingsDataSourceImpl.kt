package kr.owens.phase2.todo.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SettingsDataSourceImpl(private val dataStore: DataStore<Preferences>) : SettingsDataSource {
    private companion object {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    override val isDarkMode: Flow<Boolean> =
        dataStore.data.catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
            .map { it[IS_DARK_MODE] ?: false }

    override suspend fun setIsDarkMode(isDarkMode: Boolean) {
        dataStore.edit { it[IS_DARK_MODE] = isDarkMode }
    }
}
