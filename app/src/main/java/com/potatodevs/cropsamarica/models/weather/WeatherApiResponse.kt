package com.potatodevs.cropsamarica.models.weather

import com.potatodevs.cropsamarica.utils.toDateOnly
import kotlinx.serialization.Serializable

@Serializable
data class WeatherApiResponse(
    val location: Location,
    val current: Current,
    val forecast: Forecast
)

fun WeatherApiResponse.toDailyForecastUI(): DailyForecast {
    val forecastDay = forecast.forecastday.firstOrNull()

    return DailyForecast(
        location = location.getLocation(),
        date = this.location.localtime.toDateOnly(),
        currentTemp = "${current.temp_c}°",
        condition = Condition(
            text = current.condition.text,
            icon = current.condition.icon
        ),
        feelsLike = "Feels like ${current.feelslike_c}°",
        highLow = "${forecastDay?.day?.maxtemp_c}° / ${forecastDay?.day?.mintemp_c}°",
        description = forecastDay?.day?.condition?.text ?: "",
        hourly = forecastDay?.hour?.map { hour ->
            HourlyForecast(
                time = hour.time.substringAfter(" "),
                temp = "${hour.temp_c}°",
                condition = Condition(
                    text = hour.condition.text,
                    icon = hour.condition.icon
                ),
                chanceOfRain = hour.chance_of_rain
            )
        } ?: emptyList()
    )
}

fun WeatherBulkApiResponse.toDailyForecastUI(): List<DailyForecast> {
    val dailyForecasts = mutableListOf<DailyForecast>()
    this.bulk.map {
        val location = it.query.location
        val current = it.query.current
        val forecast = it.query.forecast
        val weatherApiResponse = WeatherApiResponse(
            location = location,
            current = current,
            forecast = forecast
        )
        dailyForecasts.add(
            weatherApiResponse.toDailyForecastUI()
        )
    }
    return dailyForecasts
}

fun Location.getLocation() : String {
    return "${this.name}, ${this.region}"
}