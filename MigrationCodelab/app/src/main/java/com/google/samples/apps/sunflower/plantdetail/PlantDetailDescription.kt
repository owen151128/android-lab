/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.plantdetail

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel

@Composable
private fun PlantName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.margin_small))
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
private fun PlantWatering(wateringInterval: Int) {
    Column(Modifier.fillMaxWidth()) {
        val centerWithPaddingModifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.margin_small))
            .align(Alignment.CenterHorizontally)
        val normalPadding = dimensionResource(R.dimen.margin_normal)
        Text(
            stringResource(R.string.watering_needs_prefix),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = centerWithPaddingModifier.padding(top = normalPadding)
        )
        val wateringIntervalText = pluralStringResource(
            R.plurals.watering_needs_suffix,
            wateringInterval,
            wateringInterval
        )
        Text(wateringIntervalText, centerWithPaddingModifier.padding(bottom = normalPadding))
    }
}

@Composable
fun PlantDetailDescription(platDetailViewModel: PlantDetailViewModel) {
    val plant by platDetailViewModel.plant.observeAsState()
    plant?.let { PlantDetailContent(it) }
}

/**
 * Compose는 현재 Spanned 클래스를 지원하지 않으며 HTML 형식 텍스트도 표시하지 않습니다.
 * 따라서 이 제한을 우회하려면 Compose 코드에서 뷰 시스템의 TextView를 사용해야 합니다.
 *
 * Compose는 아직 HTML 코드를 렌더링할 수 없으므로 프로그래매틱 방식으로 TextView를 만들어 AndroidView API를 사용하여 정확히 렌더링을 해야 합니다.
 * AndroidView를 사용하면 factory 람다에 View를 구성할 수 있습니다. 또한 뷰가 확장되었을 때 및 후속 재구성 시 호출될 때 update 람다를 제공합니다.
 * 참고: AndroidView를 사용하면 프로그래매틱 방식으로 뷰를 만들 수 있습니다.
 * XML 파일에서 뷰를 확장하려는 경우 androidx.compose.ui:ui-viewbinding 라이브러리의 AndroidViewBinding API와 함께 뷰 결합을 사용하면 됩니다.
 */
@Composable
private fun PlantDescription(description: String) {
    val htmlDescription = remember(description) {
        HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    AndroidView({ context ->
        TextView(context).apply { movementMethod = LinkMovementMethod.getInstance() }
    }, update = {
        it.text = htmlDescription
    })
}

@Composable
fun PlantDetailContent(plant: Plant) {
    Surface {
        Column(Modifier.padding(dimensionResource(R.dimen.margin_normal))) {
            PlantName(plant.name)
            PlantWatering(plant.wateringInterval)
            PlantDescription(plant.description)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0x00FFFFFF)
@Composable
private fun PlantNamePreview() {
    MaterialTheme {
        PlantName("Apple")
    }
}

@Preview(showBackground = true, backgroundColor = 0x00FFFFFF)
@Composable
private fun PlantWateringPreview() {
    MaterialTheme {
        PlantWatering(7)
    }
}

@Preview(showBackground = true, backgroundColor = 0x00FFFFFF)
@Composable
private fun PlantDescriptionPreview() {
    MaterialTheme {
        PlantDescription("HTML<br><br>description")
    }
}

@Preview
@Composable
private fun PlantDetailContentPreview() {
    MaterialTheme {
        PlantDetailContent(
            Plant(
                "id",
                "Apple",
                "Apple description",
                1,
                7
            )
        )
    }
}
