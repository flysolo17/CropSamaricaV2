package com.potatodevs.cropsamarica.ui.main.home

import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.rice.RiceStage
import com.potatodevs.cropsamarica.models.weather.DailyForecast


sealed interface HomeEvents {
    data class OnGetWeather(
        val location : String
    ) : HomeEvents
    data class OnGetTasks(
        val id : String,
        val stage : RiceStage
    ) : HomeEvents

    data class OnGetAnnouncemnt(
        val riceFieldWithRiceType: RiceFieldWithRiceType,
        val weather: DailyForecast
    ) : HomeEvents

    data class OnGetMyNotification(
        val uid : String
    ) : HomeEvents

}