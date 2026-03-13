package com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.R

import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.ui.theme.shimmer
import com.potatodevs.cropsamarica.ui.utils.toDate
import com.potatodevs.cropsamarica.utils.displayDate


@Composable
fun FarmInformationCard(
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
                .padding(16.dp)
        ) {
            Text("Crop Information", style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            ))
            Row(
                modifier = Modifier.fillMaxWidth().padding(
                    top = 8.dp
                ),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.booting),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Location"
                )
                Text(
                    text = riceField?.name.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(
                    top = 8.dp
                ),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.booting),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Location"
                )
                Text(
                    text = type?.name.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(
                    top = 8.dp
                ),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.LocationOn,
                    contentDescription = "Location"
                )
                Text(
                    text = riceField?.location.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(
                    top = 8.dp
                ),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Filled.AreaChart,
                    contentDescription = "Location"
                )
                Text(
                    text = "${riceField?.areaSize} Hectares",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(
                    top = 8.dp
                ),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Filled.DateRange,
                    contentDescription = "Location"
                )
                riceField?.plantedDate?.displayDate().orEmpty().let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }


        }
    }

}