package com.potatodevs.cropsamarica.service


import com.potatodevs.cropsamarica.BuildConfig
import com.potatodevs.cropsamarica.models.weather.BulkLocation
import com.potatodevs.cropsamarica.models.weather.SevenDayWeatherResponse
import com.potatodevs.cropsamarica.models.weather.WeatherApiResponse
import com.potatodevs.cropsamarica.models.weather.WeatherBulkApiResponse
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface WeatherApiService {

    @GET("/v1/forecast.json")
    suspend fun getWeather(
        @Query("q") location: String,
        @Query("days") days: Int = 7,
        @Query("key") key: String = BuildConfig.WEATHER_SECRET
    ): Response<WeatherApiResponse>


    @POST("/v1/forecast.json")
    suspend fun getBulk(
        @Query("q") location: String = "BULK",
        @Query("key") key: String = BuildConfig.WEATHER_SECRET,
        @Body request: BulkLocation
    ) : Response<WeatherBulkApiResponse>

    @GET("/v1/forecast.json")
    suspend fun getSevenDayWeatherForecast(
        @Query("q") location: String,
        @Query("days") days: Int = 9,
        @Query("key") key: String = BuildConfig.WEATHER_SECRET
    ): Response<SevenDayWeatherResponse>
}