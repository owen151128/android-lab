package kr.owens.phase1.todo.data

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.UUID

data class TodoItem(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    var initialIsDone: Boolean = false,
) {
    var isDone by mutableStateOf(initialIsDone)
}
