package com.potatodevs.cropsamarica.ui.main.forecast.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.models.reminder.Reminder
import com.potatodevs.cropsamarica.ui.theme.shimmer
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ReminderCard(
    reminder: Reminder,
    modifier: Modifier = Modifier,
    isNotified : Boolean = true,
    isLoading: Boolean = false,
    onNotify: () -> Unit = {}
) {
    val dateFormatter = remember { SimpleDateFormat("MMM dd", Locale.getDefault()) }
    val dayFormatter = remember { SimpleDateFormat("EEEE", Locale.getDefault()) }

    val date = dateFormatter.format(reminder.reminderDate)
    val day = dayFormatter.format(reminder.reminderDate)
    OutlinedCard(
        modifier = modifier.fillMaxWidth().shimmer(
            shimmering = isLoading

        ),
        onClick = onNotify
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text("${date}", style = MaterialTheme.typography.titleMedium)
                Text("${day}", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = reminder.message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                ),
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(
                        rememberScrollState()
                    ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val time = reminder.bestApplicationTime
                time.forEach {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = it.condition.color,
                                    shape = CircleShape
                                ),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Icon(
                                imageVector = it.condition.image,
                                contentDescription = it.condition.name,
                                modifier = Modifier.size(16.dp),
                            )

                        }
                        Text(
                            text = it.time,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            if (!isNotified) {
                TextButton(
                    onClick = onNotify,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Back"
                        )
                        Text(
                            text = "Notify me",
                            style = MaterialTheme.typography.bodySmall
                        )

                    }
                }
            }
        }
    }

}


