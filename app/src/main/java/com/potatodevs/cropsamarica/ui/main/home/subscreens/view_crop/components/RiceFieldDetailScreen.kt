package com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.ui.main.home.utils.RiceFieldDateTimeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RiceFieldDetailScreen(riceField: RiceField, type: String) {
    val dateFormatter = remember {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    }

    val riceFieldDateTimeConverter = remember {
        RiceFieldDateTimeConverter(
            plantedDate = riceField.plantedDate,
            expectedHarvestDate = riceField.expectedHarvestDate
                ?: System.currentTimeMillis()
        )
    }
    val details = listOf(
        "Variety" to type,
        "Stage" to riceField.stage.name,
        "Status" to riceField.status.name,
        "Area Size" to "${riceField.areaSize} hectares",
        "Location" to riceField.location,
        "Planted Date" to dateFormatter.format(Date(riceField.plantedDate)),
    ) + listOfNotNull(
        riceField.expectedHarvestDate?.let {
            "Expected Harvest" to dateFormatter.format(Date(it))
        },
        "Days" to "${riceFieldDateTimeConverter.displayDate()}"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = riceField.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            details.forEach { (label, value) ->
                DetailItem(label = label, value = value,modifier = Modifier.weight(1f))
            }
        }
    }
}
