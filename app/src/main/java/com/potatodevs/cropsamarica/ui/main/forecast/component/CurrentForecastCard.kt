package com.potatodevs.cropsamarica.ui.main.forecast.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.potatodevs.cropsamarica.R
import com.potatodevs.cropsamarica.models.weather.SevenDayCurrent
import com.potatodevs.cropsamarica.models.weather.SevenDayLocation
import com.potatodevs.cropsamarica.ui.theme.shimmer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun CurrentForecastCard(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    location: SevenDayLocation?,
    current: SevenDayCurrent?
) {
    Card(
        modifier = Modifier.fillMaxWidth().shimmer(
            shimmering = isLoading
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Back"
                    )
                    Text(
                        text = "${location?.name} ${location?.region}",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Text(
                    text = location?.localtime?.toFormattedDate() ?: "",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${current?.temp_c}°C",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "${current?.condition?.text}",
                    style = MaterialTheme.typography.labelSmall
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.wind),
                            contentDescription = "Wind Speed",
                            modifier = Modifier.size(16.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Text(
                            text = "${current?.wind_kph} km/h",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.humidity),
                            contentDescription = "Wind Speed",
                            modifier = Modifier.size(16.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Text(
                            text = "${current?.humidity}%",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }

            }
//            AsyncImage(
//                model = "https:${current?.condition?.icon}",
//                contentDescription = "Weather Icon",
//                modifier = Modifier.size(80.dp),
//                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
//                placeholder = painterResource(id = R.drawable.cloudy),
//                error = painterResource(id = R.drawable.cloudy)
//            )
        }
    }

}

/***
 * Converts a string date like "2025-09-20 23:10"
 * into a formatted string like "Saturday, Sep 20".
 *
 * @receiver String - e.g. "2025-09-20 23:10"
 * @return String - formatted as "Saturday, Sep 20"
 */
fun String.toFormattedDate(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault())
        val date = inputFormat.parse(this)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        this // fallback: return original string if parsing fails
    }
}