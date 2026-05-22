package kr.owens.basic.state.codelab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WaterCounter(modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        var count = 0
        Text("You' ve had $count classes.", modifier.padding(16.dp))
        Button({ count++ }, Modifier.padding(top = 8.dp)) {
            Text("Add one")
        }
    }
}
