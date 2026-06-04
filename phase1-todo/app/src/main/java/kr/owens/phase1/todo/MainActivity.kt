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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.owens.phase1.todo.data.TodoItem
import kr.owens.phase1.todo.ui.theme.Phase1todoTheme
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoScreen()
        }
    }
}

@Composable
fun TodoScreen(modifier: Modifier = Modifier) {
    var isDarkTheme by rememberSaveable { mutableStateOf(false) }
    val todoItemList = remember { mutableStateListOf<TodoItem>() }
    var showAddTodoDialog by remember { mutableStateOf(false) }
    val todoContent = rememberTextFieldState("")
    if (showAddTodoDialog) {
        AddTodoDialog(
            todoContent,
            {
                todoItemList.add(
                    TodoItem(
                        UUID.randomUUID().toString(),
                        todoContent.text.toString()
                    )
                )
                todoContent.clearText()
            },
            { showAddTodoDialog = false },
            modifier
        )
    }
    Phase1todoTheme(darkTheme = isDarkTheme) {
        TodoScreen(
            todoItemList,
            isDarkTheme,
            { isDarkTheme = !isDarkTheme },
            { showAddTodoDialog = true },
            { it.isDone = !it.isDone },
            { todoItemList.remove(it) },
            modifier
        )
    }
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
            modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dark Mode")
                Spacer(Modifier.padding(horizontal = 8.dp))
                Switch(isDarkTheme, { onDarkThemeSwitchClicked() })
                Spacer(Modifier.padding(horizontal = 8.dp))
            }
            LazyColumn(Modifier.fillMaxWidth()) {
                items(todoItemList, key = { it.id }) { todoItem ->
                    TodoListItem(
                        todoItem,
                        onToggleCheckboxClicked,
                        onRemoveButtonClicked,
                        modifier.animateItem()
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
    Column(modifier) {
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
                Text(todoItem.text)
            }
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
    AlertDialog(
        title = { Text("Todo 입력") },
        onDismissRequest = onDismiss,
        text = { OutlinedTextField(todoContent, label = { Text("Todo") }) },
        confirmButton = {
            TextButton({
                onAddTodoButtonClicked()
                onDismiss()
            }) { Text("추가") }
        },
        dismissButton = { TextButton({ onDismiss() }) { Text("취소") } },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun AddTodoDialogPreview() {
    Phase1todoTheme {
        val todoContent = rememberTextFieldState("")
        AddTodoDialog(todoContent, {}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    var isDarkTheme by rememberSaveable { mutableStateOf(false) }
    val todoContent = rememberTextFieldState("")
    var showAddTodoDialog by remember { mutableStateOf(false) }
    val previewItems = List(40) {
        TodoItem(UUID.randomUUID().toString(), "test$it", false)
    }.toMutableStateList()
    Phase1todoTheme {
        if (showAddTodoDialog) {
            AddTodoDialog(
                todoContent,
                {
                    previewItems.add(
                        TodoItem(
                            UUID.randomUUID().toString(),
                            todoContent.text.toString()
                        )
                    )
                    todoContent.clearText()
                },
                { showAddTodoDialog = false }
            )
        }
        TodoScreen(
            previewItems,
            isDarkTheme,
            { isDarkTheme = !isDarkTheme },
            { showAddTodoDialog = true },
            { it.isDone = !it.isDone },
            { previewItems.remove(it) })
    }
}
