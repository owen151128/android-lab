package kr.owens.phase1.todo.ui.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue.EndToStart
import androidx.compose.material3.SwipeToDismissBoxValue.Settled
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kr.owens.phase1.todo.data.TodoItem
import kr.owens.phase1.todo.ui.theme.Phase2todoTheme

@Composable
private fun AddTodoDialog(
    onAddTodoButtonClicked: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var attemptedSubmit by rememberSaveable { mutableStateOf(false) }
    val todoContent = rememberTextFieldState()
    val isError = attemptedSubmit && todoContent.text.isBlank()

    AlertDialog(
        title = { Text("Todo 입력") },
        onDismissRequest = onDismiss,
        text = {
            OutlinedTextField(
                todoContent,
                label = { Text("Todo") },
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text("Todo content blank is not allowed.")
                    }
                },
            )
        },
        confirmButton = {
            TextButton({
                attemptedSubmit = true
                if (todoContent.text.isNotBlank()) {
                    onAddTodoButtonClicked(todoContent.text.toString())
                    onDismiss()
                }
            }) { Text("추가") }
        },
        dismissButton = { TextButton({ onDismiss() }) { Text("취소") } },
        modifier = modifier
    )
}

@Composable
private fun TodoListItem(
    todoItem: TodoItem,
    onToggleCheckboxClicked: (TodoItem) -> Unit,
    onDeleteButtonClicked: (TodoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(Settled)
    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        enableDismissFromStartToEnd = false,
        modifier = modifier.fillMaxSize(),
        onDismiss = {
            when (it) {
                EndToStart -> {
                    onDeleteButtonClicked(todoItem)
                }

                else -> {}
            }
        },
        backgroundContent = {
            when (swipeToDismissBoxState.dismissDirection) {
                EndToStart -> {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove todo item",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(12.dp),
                        tint = Color.White
                    )
                }

                Settled -> {}
                else -> {}
            }
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surface
                )
        ) {
            Checkbox(todoItem.isDone, { onToggleCheckboxClicked(todoItem) })
            Text(
                todoItem.text,
                textDecoration = if (todoItem.isDone) TextDecoration.LineThrough else null,
                color = if (todoItem.isDone) MaterialTheme.colorScheme.onSurfaceVariant
                else MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun LoadingUi(todoUiState: TodoUiState.Loading, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text("Loading... ${todoUiState.count}")
    }
}

@Composable
private fun TodoContent(
    todoItemList: List<TodoItem>,
    onAddDialogSubmit: (String) -> Unit,
    onToggleCheckboxClicked: (TodoItem) -> Unit,
    onDeleteButtonClicked: (TodoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    Box(modifier.fillMaxSize()) {
        if (todoItemList.isNotEmpty()) {
            LazyColumn(
                Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                items(todoItemList, key = { it.id }) { todoItem ->
                    TodoListItem(
                        todoItem,
                        onToggleCheckboxClicked,
                        onDeleteButtonClicked,
                        Modifier.animateItem(),
                    )
                }
            }
        } else {
            Text("할일 없음", Modifier.align(Alignment.Center))
        }
        FloatingActionButton(
            { showAddDialog = true },
            Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Todo 항목 추가")
        }
    }
    if (showAddDialog) {
        AddTodoDialog(onAddDialogSubmit, { showAddDialog = false })
    }
}

@Composable
fun TodoScreen(
    todoUiState: TodoUiState,
    onAddDialogSubmit: (String) -> Unit,
    onToggleCheckBoxClicked: (TodoItem) -> Unit,
    onRemoveButtonClicked: (TodoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    when (todoUiState) {
        is TodoUiState.Loading -> {
            LoadingUi(todoUiState, modifier)
        }

        is TodoUiState.Success -> {
            TodoContent(
                todoUiState.todoItemList,
                onAddDialogSubmit,
                onToggleCheckBoxClicked,
                onRemoveButtonClicked,
                modifier
            )
        }

        is TodoUiState.Error -> {
            Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
                Text("오류 : ${todoUiState.exception.message}")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TodoScreenLoadingLightPreview() {
    Phase2todoTheme(darkTheme = false) {
        TodoScreen(
            TodoUiState.Loading(999),
            {},
            {},
            {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TodoScreenLoadingDarkPreview() {
    Phase2todoTheme(darkTheme = true) {
        TodoScreen(
            TodoUiState.Loading(999),
            {},
            {},
            {},
        )
    }
}

class TodoItemProvider : PreviewParameterProvider<List<TodoItem>> {
    private fun getList(size: Int) = List(size) { TodoItem(text = "preview Item $it") }
    override val values: Sequence<List<TodoItem>>
        get() = sequenceOf(getList(5), getList(50), emptyList())
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TodoScreenSuccessLightPreview(@PreviewParameter(TodoItemProvider::class) previewItems: List<TodoItem>) {
    Phase2todoTheme(darkTheme = false) {
        TodoScreen(
            TodoUiState.Success(previewItems),
            {},
            {},
            {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TodoScreenSuccessDarkPreview(@PreviewParameter(TodoItemProvider::class) previewItems: List<TodoItem>) {
    Phase2todoTheme(darkTheme = true) {
        TodoScreen(
            TodoUiState.Success(previewItems),
            {},
            {},
            {},
            )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TodoScreenErrorLightPreview() {
    Phase2todoTheme(darkTheme = false) {
        TodoScreen(
            TodoUiState.Error(RuntimeException("error test!")),
            {},
            {},
            {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TodoScreenErrorDarkPreview() {
    Phase2todoTheme(darkTheme = true) {
        TodoScreen(
            TodoUiState.Error(RuntimeException("error test!")),
            {},
            {},
            {},
        )
    }
}
