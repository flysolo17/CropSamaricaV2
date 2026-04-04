package com.potatodevs.cropsamarica.ui.main.forecast.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.potatodevs.cropsamarica.models.weather.SevenDayForecastDay
import com.potatodevs.cropsamarica.ui.theme.shimmer
import com.potatodevs.cropsamarica.ui.utils.toDay


@Composable
fun NextSixDaysCard(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    nextSixDays: List<SevenDayForecastDay>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(16.dp)
                .shimmer(
                    shimmering = isLoading
                )
            ,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            nextSixDays.forEach {
                Column(
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {

                    Text(it.date.toDay())
                    AsyncImage(
                        model = "https:${it.day.condition.icon}",
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(40.dp),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    )
                    Text(
                        text = "${it.day.avgtemp_c}°C",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}