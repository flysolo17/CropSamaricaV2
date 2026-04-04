package com.potatodevs.cropsamarica.repositories.weather

import android.util.Log
import com.potatodevs.cropsamarica.models.weather.DailyForecast
import com.potatodevs.cropsamarica.models.weather.SevenDayWeatherResponse
import com.potatodevs.cropsamarica.models.weather.WeatherApiResponse
import com.potatodevs.cropsamarica.models.weather.toDailyForecastUI
import com.potatodevs.cropsamarica.service.WeatherApiService
import java.util.Date
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService
): WeatherRepository {
    override suspend fun getDailyForecast(
        location: String,
        days: Int
    ): Result<WeatherApiResponse> {
        return try {
            val response = weatherApiService.getWeather(location + ", Mindoro", days = days)

            Log.d(TAG, "getDailyForecast: $response")
            if (response.isSuccessful) {
                val weather = response.body()

                if (weather != null) {
                    Log.d(TAG, "getDailyForecast: $weather")
                    Result.success(weather)
                } else {
                    Log.d(TAG, "getDailyForecast: Weather data is null")
                    Result.failure(Exception("Weather data is null"))
                }
            } else {
                Log.d(TAG, "getDailyForecast: Failed to fetch weather data")
                Result.failure(Exception("Failed to fetch weather data"))
            }
        } catch (e: Exception) {

            Log.d(TAG, "getDailyForecast: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getSevenDayWeather(location: String): Result<SevenDayWeatherResponse> {
        return try {
            val response = weatherApiService.getSevenDayWeatherForecast(location)
            if (response.isSuccessful) {
                val weather = response.body()
                if (weather != null) {
                    Result.success(weather)
                } else {
                    Result.failure(Exception("Weather data is null"))
                }
            } else {
                Result.failure(Exception("Failed to fetch weather data"))
            }
        }
        catch (e : Exception) {
            Result.failure(e)
        }


    }


    companion object {
        const val TAG = "Weather"
    }
}