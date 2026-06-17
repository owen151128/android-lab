package kr.owens.phase1.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue.EndToStart
import androidx.compose.material3.SwipeToDismissBoxValue.Settled
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kr.owens.phase1.todo.data.TodoItem
import kr.owens.phase1.todo.ui.theme.Phase2todoTheme
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            Phase2todoTheme(darkTheme = isDarkTheme) {
                TodoScreen(isDarkTheme, { isDarkTheme = !isDarkTheme })
            }
        }
    }
}

@Composable
fun TodoScreen(
    isDarkTheme: Boolean,
    onDarkThemeSwitchClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val todoItemList = remember { mutableStateListOf<TodoItem>() }
    var showAddTodoDialog by remember { mutableStateOf(false) }
    val todoContent = rememberTextFieldState("")
    if (showAddTodoDialog) {
        AddTodoDialog(
            todoContent,
            {
                todoItemList.add(TodoItem(text = todoContent.text.toString()))
                todoContent.clearText()
                showAddTodoDialog = false
            },
            { showAddTodoDialog = false },
        )
    }
    TodoScreen(
        todoItemList,
        isDarkTheme,
        onDarkThemeSwitchClicked,
        { showAddTodoDialog = true },
        { todoItem ->
            val index = todoItemList.indexOfFirst { it.id == todoItem.id }
            if (index != -1) {
                todoItemList[index] =
                    todoItemList[index].copy(isDone = !todoItemList[index].isDone)
            }
        },
        { todoItemList.remove(it) },
        modifier,
    )
}

@Composable
fun TodoScreen(
    todoItemList: List<TodoItem>,
    isDarkTheme: Boolean,
    onDarkThemeSwitchClicked: () -> Unit,
    onAddButtonClicked: () -> Unit,
    onToggleCheckboxClicked: (TodoItem) -> Unit,
    onRemoveButtonClicked: (TodoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = { AddTodoButton(onAddButtonClicked) },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dark Mode")
                Spacer(Modifier.width(8.dp))
                Switch(isDarkTheme, { onDarkThemeSwitchClicked() })
                Spacer(Modifier.width(8.dp))
            }
            LazyColumn(Modifier.fillMaxWidth()) {
                items(todoItemList, key = { it.id }) { todoItem ->
                    TodoListItem(
                        todoItem,
                        onToggleCheckboxClicked,
                        onRemoveButtonClicked,
                        Modifier.animateItem(),
                    )
                }
            }
        }
    }
}

@Composable
fun TodoListItem(
    todoItem: TodoItem,
    onToggleCheckboxClicked: (TodoItem) -> Unit,
    onRemoveButtonClicked: (TodoItem) -> Unit,
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
                    onRemoveButtonClicked(todoItem)
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
fun AddTodoButton(onAddButtonClick: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(onAddButtonClick, modifier) { Icon(Icons.Filled.Add, "Add Todo") }
}

@Composable
fun AddTodoDialog(
    todoContent: TextFieldState,
    onAddTodoButtonClicked: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var attemptedSubmit by remember { mutableStateOf(false) }
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
                    onAddTodoButtonClicked()
                }
            }) { Text("추가") }
        },
        dismissButton = { TextButton({ onDismiss() }) { Text("취소") } },
        modifier = modifier
    )
}

@PreviewLightDark
@Composable
private fun AddTodoDialogPreview() {
    Phase2todoTheme {
        val todoContent = rememberTextFieldState("")
        AddTodoDialog(todoContent, {}, {})
    }
}

@PreviewLightDark
@Composable
private fun TodoScreenPreview() {
    var isDarkTheme by rememberSaveable { mutableStateOf(false) }
    val todoContent = rememberTextFieldState("")
    var showAddTodoDialog by remember { mutableStateOf(false) }
    val previewItems = List(40) {
        TodoItem(UUID.randomUUID().toString(), "test$it", it % 2 == 0)
    }.toMutableStateList()
    Phase2todoTheme {
        if (showAddTodoDialog) {
            AddTodoDialog(
                todoContent,
                {
                    val userInputText = todoContent.text.toString()
                    if (userInputText.isNotBlank()) {
                        previewItems.add(TodoItem(text = userInputText))
                        todoContent.clearText()
                    }
                },
                { showAddTodoDialog = false }
            )
        }
        TodoScreen(
            previewItems,
            isDarkTheme,
            { isDarkTheme = !isDarkTheme },
            { showAddTodoDialog = true },
            { todoItem ->
                val index = previewItems.indexOfFirst { it.id == todoItem.id }
                if (index != -1) {
                    previewItems[index] =
                        previewItems[index].copy(isDone = !previewItems[index].isDone)
                }
            },
            { previewItems.remove(it) })
    }
}
