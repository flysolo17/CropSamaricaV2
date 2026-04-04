package com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.models.rice.RiceStage
import com.potatodevs.cropsamarica.ui.utils.getIcon

@Composable
fun StageItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    isLast : Boolean = false,
    isCurrentStage :Boolean = false,
    stage: RiceStage,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(
                if (!selected) 100.dp else Dp.Unspecified
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isCurrentStage) {
                Image(
                    painter = painterResource(stage.getIcon()),
                    contentDescription = stage.name,
                    modifier = Modifier.size(48.dp).clickable {
                        onClick()
                    },
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(stage.getIcon()),
                    contentDescription = stage.name,
                    modifier = Modifier.size(48.dp).clickable {
                        onClick()
                    },
                )
            }
            if (!isLast) {
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    color = if (isCurrentStage) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stage.displayName,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = if (isCurrentStage) MaterialTheme.colorScheme.primary else Color.Unspecified
                ),
            )
            Text("${stage.daysRange.first} - ${stage.daysRange.last} days", style = MaterialTheme.typography.bodySmall)
            if (selected) {
                Text(
                    text = stage.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray,
                    ),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.CheckBox,
                        contentDescription = "Check",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text("Things you should do", style = MaterialTheme.typography.titleSmall)
                }
                stage.thingsToDo.forEach {
                    Text(
                        text = "- $it",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray
                        ),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Check",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                    Text("Things to look out for", style = MaterialTheme.typography.titleSmall)
                }
                stage.thingsToLookOutFor.forEach {
                    Text(
                        text = "- $it",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray
                            ),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }


        }
    }

}
