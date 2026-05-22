package kr.owens.basic.state.codelab

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WaterCounter(modifier: Modifier = Modifier) {
    val count = 0
    Text("You' ve had $count classes.", modifier.padding(16.dp))
}
