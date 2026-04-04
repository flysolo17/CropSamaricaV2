package com.potatodevs.cropsamarica.ui.main.common

import android.provider.CalendarContract
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.tasks.Tasks
import com.potatodevs.cropsamarica.models.tasks.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationDialog(
    tasks: List<Task>, // Your already converted fertilizer tasks
    onDismiss: () -> Unit,
    onCreateTask: (List<Task>) -> Unit
) {
    // Start empty so the user explicitly chooses what to add
    val fertilizerTasks = tasks.filter { it.fertilizer == true }
    var selectedTasks by remember { mutableStateOf(fertilizerTasks.toSet()) }

    Dialog(
        onDismissRequest = { /* No-op */ },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                ,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Select Fertilizer Tasks", style = MaterialTheme.typography.titleLarge)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    tasks.forEach { task ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = task.title, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    text = task.description, // Showing the "purpose" as description
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                            Checkbox(
                                checked = selectedTasks.contains(task),
                                onCheckedChange = { isChecked ->
                                    selectedTasks = if (isChecked) {
                                        selectedTasks + task
                                    } else {
                                        selectedTasks - task
                                    }
                                }
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Dismiss")
                    }

                    Button(
                        onClick = {

                            onCreateTask(selectedTasks.toList())
                        },
                        modifier = Modifier.weight(1f),
                        enabled = selectedTasks.isNotEmpty()
                    ) {
                        Text("Create Task(s)")
                    }
                }
            }
        }
    }
}


