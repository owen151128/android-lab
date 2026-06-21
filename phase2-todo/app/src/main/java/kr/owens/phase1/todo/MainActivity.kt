package kr.owens.phase1.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kr.owens.phase1.todo.ui.theme.Phase2todoTheme
import kr.owens.phase1.todo.ui.todo.TodoRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            Phase2todoTheme(darkTheme = isDarkTheme) {
                TodoRoute(isDarkTheme, { isDarkTheme = !isDarkTheme })
            }
        }
    }
}
