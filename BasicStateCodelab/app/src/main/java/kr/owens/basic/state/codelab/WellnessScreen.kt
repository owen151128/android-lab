package kr.owens.basic.state.codelab

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WellnessScreen(
    modifier: Modifier = Modifier,
    wellnessViewModel: WellnessViewModel = viewModel()
) {
    Column(modifier) {
        StatefulCounter(modifier)

        WellnessTasksList(
            wellnessViewModel.tasks,
            { task, checked ->
                wellnessViewModel.changeTaskChecked(task, checked)
            },
            { task -> wellnessViewModel.remove(task) }
        )
    }
}
