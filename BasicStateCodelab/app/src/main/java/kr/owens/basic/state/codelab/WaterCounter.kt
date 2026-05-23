package kr.owens.basic.state.codelab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WaterCounter(modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        var count by remember { mutableStateOf(0) }
        if (count > 0) {
            var showTask by remember { mutableStateOf(true) }
            if (showTask) {
                WellnessTaskItem(
                    "Have you taken your 15 minute walk today?",
                    { showTask = false }
                )
            }
            Text("You've had $count glasses.", Modifier.padding(16.dp))
        }
        Row(Modifier.padding(top = 8.dp)) {
            Button({ count++ }, enabled = count < 10) {
                Text("Add one")
            }
            Button({ count = 0 }, Modifier.padding(start = 8.dp)) {
                Text("Clear water count")
            }
        }
    }
}
