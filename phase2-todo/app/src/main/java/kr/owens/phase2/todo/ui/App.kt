package kr.owens.phase2.todo.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.owens.phase2.todo.ui.theme.Phase2todoTheme
import kr.owens.phase2.todo.ui.todo.TodoRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(initialIsDarkTheme: Boolean, modifier: Modifier = Modifier) {
    var isDarkTheme by rememberSaveable { mutableStateOf(initialIsDarkTheme) }
    Phase2todoTheme(darkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                TopAppBar({ Text("Todo app") }, actions = {
                    Text("Dark Mode")
                    Spacer(Modifier.width(8.dp))
                    Switch(isDarkTheme, { isDarkTheme = it })
                    Spacer(Modifier.width(8.dp))
                })
            },
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            TodoRoute(Modifier.padding(innerPadding))
        }
    }
}
