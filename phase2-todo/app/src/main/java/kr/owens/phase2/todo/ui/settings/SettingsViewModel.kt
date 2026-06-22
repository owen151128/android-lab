package kr.owens.phase2.todo.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kr.owens.phase2.todo.TodoApp
import kr.owens.phase2.todo.domain.SettingsRepository

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {
    val isDarkMode = settingsRepository.darkMode.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        false
    )

    fun setIsDarkMode(isDarkMode: Boolean) = viewModelScope.launch {
        settingsRepository.setIsDarkMode(isDarkMode)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TodoApp
                SettingsViewModel(app.container.settingsRepository)
            }
        }
    }
}
