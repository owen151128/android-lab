package kr.owens.phase2.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.owens.phase2.todo.ui.App
import kr.owens.phase2.todo.ui.settings.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel =
                viewModel(factory = SettingsViewModel.Factory)
            val isDarkTheme by settingsViewModel.isDarkMode.collectAsStateWithLifecycle()
            App(isDarkTheme, settingsViewModel::setIsDarkMode)
        }
    }
}
