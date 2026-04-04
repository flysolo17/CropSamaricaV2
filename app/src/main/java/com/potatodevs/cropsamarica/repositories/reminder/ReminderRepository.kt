package com.potatodevs.cropsamarica.repositories.reminder

import com.potatodevs.cropsamarica.models.reminder.Reminder
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.weather.DailyForecast
import com.potatodevs.cropsamarica.models.weather.SevenDayWeatherResponse
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    suspend fun generateReminder(
        forecast: SevenDayWeatherResponse,
        riceField : RiceFieldWithRiceType
    ) : Result<List<Reminder>>
    suspend fun createReminders(
        reminder: Reminder
    ) : Result<String>

     fun getRemindersToday(id : String) : Flow<List<Reminder>>


}