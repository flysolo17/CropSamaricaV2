package com.potatodevs.cropsamarica.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.models.tasks.TaskStatus
import com.potatodevs.cropsamarica.ui.theme.shimmer
import com.potatodevs.cropsamarica.ui.utils.getBackgroundColor
import com.potatodevs.cropsamarica.ui.utils.getStartAndDue
import com.potatodevs.cropsamarica.ui.utils.getTextColor

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    task: Task,
    isLoading : Boolean = false,
    enableBorder : Boolean = true,
    onClick: () -> Unit = {}
) {
    val isDone = task.status == TaskStatus.COMPLETED
    Card(
        modifier = modifier.shimmer(
            shimmering = isLoading,
            shape = MaterialTheme.shapes.medium
        ),
        onClick = {
            onClick()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 24.dp,
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                VerticalDivider(
                    thickness = 4.dp,
                    modifier = Modifier.height(24.dp),
                    color = task.status.getTextColor()
                )


                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                    )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp
                    ),
            ) {
                val date = task.getStartAndDue()

                date?.let {
                    Text(
                        modifier = Modifier
                            .background(
                                color = task.getBackgroundColor(),
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(
                                4.dp
                            ),
                        text = it,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color= task.getTextColor()
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (task.description.isNotEmpty()) {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray,
                            textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                        )
                    )
                }
            }


        }
    }

}