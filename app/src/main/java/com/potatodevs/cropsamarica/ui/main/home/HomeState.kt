package com.potatodevs.cropsamarica.ui.main.home

import com.potatodevs.cropsamarica.models.Notifications
import com.potatodevs.cropsamarica.models.announcement.Announcement
import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.models.weather.DailyForecast

data class TaskState(
    val isLoading: Boolean = false,
    val tasks : List<Task> = emptyList(),

)
data class WeatherState(
    val isLoading: Boolean = false,
    val weather: DailyForecast? = null,

)
data class HomeState(
    val isLoading : Boolean = false,
    val selected : String = "",
    val riceField: RiceField ? = null,
    val weather : WeatherState = WeatherState(),
    val tasks : TaskState = TaskState(),
    val announcement : AnnouncementState = AnnouncementState(),
    val notifications : List<Notifications> = emptyList(),
    val language : String = "en"
)

data class AnnouncementState(
    val isLoading : Boolean = false,
    val announcement : Announcement? = null
)