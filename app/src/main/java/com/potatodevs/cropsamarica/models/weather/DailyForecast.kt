package com.potatodevs.cropsamarica.models.weather

import kotlinx.serialization.Serializable


@Serializable
data class DailyForecast(
    var id: String = "",
    var location: String = "",
    val date: String = "",
    val currentTemp: String = "",
    val condition: Condition = Condition(),
    val feelsLike: String = "",
    val highLow: String = "",
    val description: String = "",
    val hourly: List<HourlyForecast> = emptyList()
)

