package com.potatodevs.cropsamarica.repositories.reminder

import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.weather.DailyForecast

interface ReminderRepository {
    suspend fun generateReminder(
        forecast: DailyForecast,
        riceField : RiceFieldWithRiceType
    )
}