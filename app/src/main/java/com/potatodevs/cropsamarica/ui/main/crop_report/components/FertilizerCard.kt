package com.potatodevs.cropsamarica.ui.main.crop_report.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.models.tasks.TaskStatus
import com.potatodevs.cropsamarica.ui.theme.shimmer
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun FertilizerCard(
    modifier: Modifier = Modifier,
    tasks : List<Task>,
    isLoading : Boolean
) {
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Fertilizer Application", style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            )
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tasks found", style = MaterialTheme.typography.bodyMedium)
                }
            }
            tasks.forEach {
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent,

                    ),
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Date",
                            tint = if (it.status == TaskStatus.COMPLETED) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.5f
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    headlineContent = {
                        Text(
                            text = it.type.toString(),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )
                    },
                    supportingContent = {
                        Text(it.amount ?: "N/A", style = MaterialTheme.typography.bodyMedium)
                    },
                    trailingContent = {
                        Text(it.startDate.display(), style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ))
                    }
                )
            }

        }
    }
}
/**
 * Formats a Date into "MMM dd" (e.g., Jan 04).
 * Returns an empty string or "N/A" if the Date is null.
 */
fun Date?.display(): String {
    if (this == null) return ""
    val formatter = SimpleDateFormat("MMM dd", java.util.Locale.getDefault())
    return formatter.format(this)
}

