package kr.owens.phase2.todo.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kr.owens.phase2.todo.data.local.SettingsDataSourceImpl
import kr.owens.phase2.todo.domain.SettingsRepository
import kr.owens.phase2.todo.domain.SettingsRepositoryImpl

private const val PREFERENCES_NAME = "settings"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCES_NAME)

interface AppContainer {
    val settingsRepository: SettingsRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val dataSource = SettingsDataSourceImpl(context.dataStore)

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(dataSource)
    }
}
