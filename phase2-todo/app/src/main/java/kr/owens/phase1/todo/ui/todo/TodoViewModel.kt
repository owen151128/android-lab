package kr.owens.phase1.todo.ui.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.owens.phase1.todo.data.TodoItem

class TodoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<TodoUiState>(TodoUiState.Loading(0))

    val uiState = _uiState.asStateFlow()

    init {
        loadTodoItemList()
    }

    private fun updateTodoItemList(transform: (List<TodoItem>) -> List<TodoItem>) =
        _uiState.update { state ->
            if (state is TodoUiState.Success) {
                state.copy(todoItemList = transform(state.todoItemList))
            } else state
        }

    private fun loadTodoItemList() = viewModelScope.launch {
        repeat(5) { count ->
            _uiState.update { TodoUiState.Loading(count + 1) }
            delay(500L)
        }
        _uiState.update {
            TodoUiState.Success(emptyList())
        }
    }

    fun addTodoItem(text: String) = updateTodoItemList { it + TodoItem(text = text) }

    fun toggleTodoIsDone(todoItem: TodoItem) = updateTodoItemList { list ->
        list.map { if (it.id == todoItem.id) it.copy(isDone = !todoItem.isDone) else it }
    }

    fun deleteTodoItem(todoItem: TodoItem) = updateTodoItemList { list ->
        list.filterNot { it.id == todoItem.id }
    }
}
