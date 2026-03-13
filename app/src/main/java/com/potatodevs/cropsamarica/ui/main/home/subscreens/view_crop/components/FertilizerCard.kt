package com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.models.tasks.TaskStatus
import com.potatodevs.cropsamarica.ui.theme.shimmer
import com.potatodevs.cropsamarica.utils.toDateOnly
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
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Fertilizer Application", style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            )
            tasks.forEach {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically

                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Filled.Check,
                        contentDescription = "Date",
                        tint = if (it.status == TaskStatus.COMPLETED) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.5f
                        )
                    )
                    Text("${it.startDate?.display()} ${it.type}", style = MaterialTheme.typography.bodyMedium)
                }

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