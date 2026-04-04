package com.potatodevs.cropsamarica.repositories.weather

import com.potatodevs.cropsamarica.models.weather.DailyForecast
import com.potatodevs.cropsamarica.models.weather.SevenDayWeatherResponse
import com.potatodevs.cropsamarica.models.weather.WeatherApiResponse
import java.util.Date

interface WeatherRepository {
    suspend fun getDailyForecast(
        location: String,
        days: Int = 1
    ) : Result<WeatherApiResponse>


    suspend fun getSevenDayWeather(
        location: String
    ) : Result<SevenDayWeatherResponse>


}