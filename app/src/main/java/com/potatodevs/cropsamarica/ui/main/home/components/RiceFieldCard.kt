package com.potatodevs.cropsamarica.ui.main.home.components





import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.ui.main.home.utils.RiceFieldDateTimeConverter
import com.potatodevs.cropsamarica.ui.utils.getIcon


@Composable
fun RiceFieldCard(
    modifier: Modifier = Modifier,
    data: RiceFieldWithRiceType?,
    onClick: () -> Unit = {}
) {
    val riceField = data?.riceField
    if (riceField == null) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            onClick = onClick
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "No Rice Field",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "No Rice Field",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "No Rice Field",
                    style = MaterialTheme.typography.bodyMedium
                )
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {

                }
            }
        }
    } else {
        val riceFieldDateTimeConverter = remember {
            RiceFieldDateTimeConverter(
                plantedDate = riceField.plantedDate,
                expectedHarvestDate = riceField.expectedHarvestDate
                    ?: System.currentTimeMillis()
            )
        }
        Card(
            modifier = modifier,
            shape = MaterialTheme.shapes.large,
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = riceField.stage.displayName,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "${riceField.areaSize} ha , ${data.riceType?.name ?: "Unknown"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = riceFieldDateTimeConverter.displayDate(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Image(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(
                        id = riceField.stage.getIcon()
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Rice Field Image"
                )
            }

        }

    }

}

