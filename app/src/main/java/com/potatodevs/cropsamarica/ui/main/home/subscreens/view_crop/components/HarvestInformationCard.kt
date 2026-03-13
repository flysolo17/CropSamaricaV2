package com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.ui.theme.shimmer
import com.potatodevs.cropsamarica.utils.displayDate


@Composable
fun HarverInformationCard(
    modifier: Modifier = Modifier,
    crop : RiceFieldWithRiceType,
    isLoading : Boolean
) {
    val riceField = crop.riceField
    val type = crop.riceType
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(
                shimmering = isLoading,
                shape = MaterialTheme.shapes.medium
            ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Harvest Information", style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            )
            val bodyMedium = MaterialTheme.typography.bodyMedium

            Text("Expect Harvest Date : ${riceField?.expectedHarvestDate?.displayDate()}", style = bodyMedium)
            Text("Ave Yield : ${type?.yieldPerHectare} tons / Hectare ", style = bodyMedium)
            val total = type?.yieldPerHectare?.times(riceField?.areaSize ?: 0.0) ?: 0.0
            Text("Estimated Total Harvest : ${total} tons" ,style = bodyMedium)
            val sacks = total * 20
            Text("Approx Total Sacks : ${sacks} sacks", style = bodyMedium)
        }
    }

}