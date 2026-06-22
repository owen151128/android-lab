package kr.owens.phase1.todo.ui.todo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TodoRoute(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = viewModel(),
) {
    val todoUiState by viewModel.uiState.collectAsStateWithLifecycle()
    TodoScreen(
        todoUiState,
        viewModel::addTodoItem,
        viewModel::toggleTodoIsDone,
        viewModel::deleteTodoItem,
        modifier
    )
}
