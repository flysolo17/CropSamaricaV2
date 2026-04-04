package com.potatodevs.cropsamarica.ui.main.forecast

import com.potatodevs.cropsamarica.models.reminder.Reminder
import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.weather.SevenDayWeatherResponse


data class ForecastState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val weatherState: WeatherState  = WeatherState(),
    val riceFieldState: RiceFieldState = RiceFieldState(),
    val reminderState: ReminderState = ReminderState(),
    val aiReminderState: AiReminderState = AiReminderState()
)

data class AiReminderState(
    val isLoading: Boolean = false,
    val reminders : List<Reminder> = emptyList(),
    val error: String? = null,
)
data class RiceFieldState(
    val isLoading: Boolean = false,
    val riceField: RiceFieldWithRiceType? = null
)

data class ReminderState(
    val isLoading: Boolean = false,
    val reminders: List<Reminder> = emptyList()
)
data class WeatherState(
    val isLoading: Boolean = false,
    val weather: SevenDayWeatherResponse? = null
)