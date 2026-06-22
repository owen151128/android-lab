package kr.owens.phase2.todo.ui.todo

import kr.owens.phase2.todo.data.TodoItem

sealed interface TodoUiState {
    data class Loading(val count: Int) : TodoUiState
    data class Success(val todoItemList: List<TodoItem>) : TodoUiState
    data class Error(val exception: Exception) : TodoUiState
}
