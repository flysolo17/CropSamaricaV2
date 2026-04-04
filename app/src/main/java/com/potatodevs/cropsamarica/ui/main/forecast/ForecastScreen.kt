package com.potatodevs.cropsamarica.ui.main.forecast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.potatodevs.cropsamarica.models.reminder.Reminder
import com.potatodevs.cropsamarica.models.weather.Current
import com.potatodevs.cropsamarica.ui.main.forecast.component.CurrentForecastCard
import com.potatodevs.cropsamarica.ui.main.forecast.component.NextSixDaysCard
import com.potatodevs.cropsamarica.ui.main.forecast.component.ReminderCard
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme
import com.potatodevs.cropsamarica.ui.theme.shimmer
import com.potatodevs.cropsamarica.ui.utils.showToast

@Composable
fun ForecastScreen(
    modifier: Modifier = Modifier,
    id: String,
    onBack: () -> Unit,
    viewModel: ForecastViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    val events = viewModel::events
    val riceField = state.riceFieldState.riceField
    val location = riceField?.riceField?.location
    val weather = state.weatherState.weather
    val context = LocalContext.current
    val reminders = state.reminderState.reminders
    LaunchedEffect(key1 = id) {
        if (id.isNotEmpty()) {
            events(ForecastEvents.OnGetRiceField(id))
           events(ForecastEvents.GetRemindersToday(id))
        }
    }
    LaunchedEffect(location) {
        riceField?.riceField?.location?.let {
            events(ForecastEvents.OnGetWeather(it))
        }
    }
    LaunchedEffect(riceField, weather) {
        if (riceField != null && weather != null && reminders.isEmpty()) {
            viewModel.events(
                ForecastEvents.OnGenerateReminder(riceField, weather)
            )
        }
    }
    ForecastScreen(
        modifier = modifier,
        onBack = onBack,
        weather = state.weatherState,
        reminder = state.reminderState,
        id = id,
        aiReminder = state.aiReminderState,
        onNotify = {
            viewModel.events(ForecastEvents.OnNotify(it))
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    modifier: Modifier = Modifier,
    onBack : () -> Unit,
    weather: WeatherState,
    id: String,
    reminder: ReminderState,
    aiReminder: AiReminderState,
    onNotify : (reminder : Reminder) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Weather Forecast")
                },

                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.shimmer(shimmering = false)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            item {
                CurrentForecastCard(
                    isLoading = weather.isLoading,
                    current = weather.weather?.current,
                    location = weather.weather?.location
                )
            }
            item {
                Text(
                    text = "Reminders",
                    modifier = Modifier.shimmer(
                        shimmering = reminder.isLoading
                    ),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            items(reminder.reminders, key = {it.id}) {
                ReminderCard(
                    reminder = it,
                    isNotified = true
                )
            }
            if (!reminder.isLoading && reminder.reminders.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No reminders found",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            item {
                Text(
                    text = "Next 6 days",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.shimmer(
                        shimmering = weather.isLoading
                    )
                )
            }
            val nextSixDays = weather.weather?.forecast?.forecastday?.takeLast(2) ?: emptyList()
            item { 
                NextSixDaysCard(
                    isLoading = weather.isLoading,
                    nextSixDays = nextSixDays
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Aya Reminders")
                    if (aiReminder.isLoading) {
                        Text("Generating...",style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray
                        ))
                    }

                }

            }

            val aira = aiReminder.reminders.filter { data ->
                reminder.reminders.none { it.message == data.message }
            }

            items(aira, key = {it.id}) { it ->
                ReminderCard(
                    reminder = it,
                    isNotified = false,
                    onNotify = {
                        onNotify(it)
                    })
            }
            if (aiReminder.error != null && !aiReminder.isLoading) {
                item {
                    Text(
                        text = aiReminder.error
                    )
                }
            }


        }
    }
}

@Preview
@Composable
private fun ForecastScreenPrev() {
    CropSamaricaTheme {
        ForecastScreen(
            id = "1",
            onBack = {}
        )
    }
}