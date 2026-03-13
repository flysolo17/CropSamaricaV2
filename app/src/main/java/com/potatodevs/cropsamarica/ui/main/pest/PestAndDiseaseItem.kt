package com.potatodevs.cropsamarica.ui.main.pest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.potatodevs.cropsamarica.R
import com.potatodevs.cropsamarica.models.pests.PestAndDisease
import com.potatodevs.cropsamarica.ui.theme.shimmer

@Composable
fun PestAndDiseaseItem(
    modifier: Modifier = Modifier,
    pestAndDisease: PestAndDisease,
    isLoading: Boolean = false,
    language: String = "en",
    onClick: () -> Unit
) {

    val info = if (language == "en") {
        pestAndDisease.information.en
    } else {
        pestAndDisease.information.tl
    }
    Card(
        modifier = modifier.size(220.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            AsyncImage(
                model = pestAndDisease.images.first(),
                placeholder = painterResource(
                    id = R.drawable.profile_bg
                ),
                error = painterResource(
                    id = R.drawable.profile_bg
                ),
                modifier = Modifier.weight(1f).shimmer(shimmering = isLoading,shape = MaterialTheme.shapes.medium),
                contentDescription = info.title,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                val firstWord = info.title.substringBefore("–").trim()
                val lastWord = info.title.substringAfter("–").trim()

                Text(text = firstWord, style = MaterialTheme.typography.titleMedium, modifier = Modifier.shimmer(shimmering = isLoading))
                Text(text = lastWord, style = MaterialTheme.typography.titleSmall.copy(

                    color= MaterialTheme.colorScheme.outline
                ) ,maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.shimmer(shimmering = isLoading)
                )
            }
        }
    }
}