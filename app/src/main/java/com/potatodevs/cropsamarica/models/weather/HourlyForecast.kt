package com.potatodevs.cropsamarica.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class HourlyForecast(
    val time: String = "",
    val temp: String = "",
    val condition: Condition  = Condition(),
    val iconUrl: String = "",
    val chanceOfRain: Int = 0
)