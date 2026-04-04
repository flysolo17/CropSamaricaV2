package com.potatodevs.cropsamarica.ui.main.forecast

import com.potatodevs.cropsamarica.models.reminder.Reminder
import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.weather.SevenDayForecast
import com.potatodevs.cropsamarica.models.weather.SevenDayWeatherResponse


sealed interface ForecastEvents  {

    data class OnGetRiceField(
        val  id : String
    ) : ForecastEvents

    data class OnGetWeather(
        val location : String
    ) : ForecastEvents

    data class OnGenerateReminder(
        val riceField : RiceFieldWithRiceType,
        val weather : SevenDayWeatherResponse
    ) : ForecastEvents

    data class GetRemindersToday(
        val id : String
    ) : ForecastEvents

    data class OnNotify(
        val reminder : Reminder
    ) : ForecastEvents
}
