package com.potatodevs.cropsamarica.ui.main.home.components





import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.potatodevs.cropsamarica.models.weather.Condition
import com.potatodevs.cropsamarica.models.weather.DailyForecast
import com.potatodevs.cropsamarica.models.weather.HourlyForecast
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme
import com.potatodevs.cropsamarica.ui.utils.toProperTime

import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun WeatherCard(
    modifier: Modifier = Modifier,
    weather: DailyForecast? = sampleDailyForecast,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
        onClick = onClick
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = weather?.location ?: "No Location",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location"
                )
            }
            val hourNow = SimpleDateFormat("HH aa", Locale.getDefault()).format(System.currentTimeMillis())
            val currentTemp = weather?.hourly?.find {
                it.time.toProperTime() == hourNow
            }?.temp ?: weather?.currentTemp ?: "0 C"

            Text(
                text = currentTemp,
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = weather?.condition?.text ?: "No Condition",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "${weather?.highLow} ${weather?.feelsLike}",
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        top = 16.dp
                    )
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                weather?.hourly?.forEach {
                    Column(
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = it.time.toProperTime(),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                        )
                        AsyncImage(
                            model = "https:${it.condition.icon}",
                            contentDescription = "Weather Icon",
                            modifier = Modifier.size(24.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Fit
                        )
                        Text(
                            text = it.temp,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

            }
        }
    }
}



@Preview
@Composable
private fun WeatherCardPrev() {
    CropSamaricaTheme {
        WeatherCard(
            weather = sampleDailyForecast
        )
    }
}

//should return  "14:9:7"
fun toHours()  {
    val totalSeconds = System.currentTimeMillis() / 1000
    val hour = totalSeconds / 3600

}


val sampleDailyForecast = DailyForecast(
    id = "df_001",
    location = "Calintaan Mindoro",
    date = "2025-08-27",
    currentTemp = "26°C",
    condition = Condition(
        text = "Sunny",
        icon = "https://cdn.weatherapi.com/weather/64x64/night/116.png"
    ),
    feelsLike = "28°C",
    highLow = "30°C / 22°C",
    description = "Clear skies throughout the day with warm afternoon temperatures.",
    hourly = listOf(
        HourlyForecast(
            time = "08:00",
            temp = "22°C",
            condition = Condition("Sunny", "https://cdn.weatherapi.com/weather/64x64/night/116.png")
        ),
        HourlyForecast(
            time = "12:00",
            temp = "26°C",
            condition = Condition("Sunny", "https://cdn.weatherapi.com/weather/64x64/night/116.png")
        ),
        HourlyForecast(
            time = "16:00",
            temp = "30°C",
            condition = Condition("Partly Cloudy", "https://cdn.weatherapi.com/weather/64x64/night/116.png")
        ),
        HourlyForecast(
            time = "20:00",
            temp = "24°C",
            condition = Condition("Clear Night", "https://cdn.weatherapi.com/weather/64x64/night/116.png")
        )
    )
)