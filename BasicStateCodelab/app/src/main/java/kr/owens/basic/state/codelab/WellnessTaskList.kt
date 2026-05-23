package kr.owens.basic.state.codelab

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WellnessTasksList(
    list: List<WellnessTask>,
    onCheckedTask: (WellnessTask, Boolean) -> Unit,
    onCloseTask: (WellnessTask) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        items(list, { task -> task.id }) { task ->
            WellnessTaskItem(
                taskName = task.label,
                task.checked,
                { checked -> onCheckedTask(task, checked) },
                { onCloseTask(task) })
        }
    }
}
