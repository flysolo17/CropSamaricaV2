package com.potatodevs.cropsamarica.ui.main.pest.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.potatodevs.cropsamarica.R
import com.potatodevs.cropsamarica.models.pests.GOLDEN_APPLE_SNAIL
import com.potatodevs.cropsamarica.models.pests.PestAndDisease
import com.potatodevs.cropsamarica.models.pests.toNew
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme
import com.potatodevs.cropsamarica.ui.theme.shimmer


@Composable
fun PestAndDiseaseDetailScreen(
    modifier: Modifier = Modifier,
    id: String,

    onBack: () -> Unit,

    viewModel: PestAndDiseaseDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events= viewModel::events
    LaunchedEffect(id) {
        if (id.isNotEmpty()) {
            events(PestAndDiseaseEvents.OnGetPestAndDisease(id))
        }
    }

    PestAndDiseaseDetailScreen(
        modifier = modifier,
        isLoading = state.isLoading,
        language = state.language,
        pestAndDisease = state.pestAndDisease ?: GOLDEN_APPLE_SNAIL.toNew(),
        onBack = {
            onBack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PestAndDiseaseDetailScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = true,
    language: String,
    pestAndDisease: PestAndDisease,
    onBack : () -> Unit
) {
    val info = if (language == "en") {
        pestAndDisease.information.en
    } else {
        pestAndDisease.information.tl
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val firstWord = info.title.substringBefore("–").trim()
    val lastWord = info.title.substringAfter("–").trim()

    Scaffold(
        modifier = modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {

            LakbayCollapsingToolbar(
                modifier = Modifier.shimmer(
                    shimmering = isLoading,
                    shape = RoundedCornerShape(0.dp)
                ),
                scrollBehavior = scrollBehavior,
                images = pestAndDisease.images,
                title = {
                    Text(
                        text = firstWord,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {

                }

            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(
                    state = rememberScrollState()
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = firstWord,
                modifier = Modifier.shimmer(
                    shimmering = isLoading,
                    shape = RoundedCornerShape(0.dp)
                ),
                style = MaterialTheme.typography.titleLarge
            )

            Text(lastWord, style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.shimmer(
                    shimmering = isLoading,
                    shape = RoundedCornerShape(0.dp)
                ),
                )

            val description = info.description
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
                modifier = Modifier.shimmer(
                    shimmering = isLoading,
                    shape = RoundedCornerShape(0.dp)
                ),
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Image(
                    painter = painterResource(id = R.drawable.plant_book),
                    contentDescription = firstWord,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(24.dp).shimmer(
                        shimmering = isLoading,
                        shape = RoundedCornerShape(0.dp)
                    )
                )
                Text(
                    text = "Symptoms",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.shimmer(
                        shimmering = isLoading,
                        shape = RoundedCornerShape(0.dp)
                    )
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            val symptoms = info.symptoms
            Column(
                modifier = Modifier.padding(
                    start = 12.dp
                ).shimmer(
                    shimmering = isLoading,
                    shape = RoundedCornerShape(0.dp)
                )
            ){
                symptoms.forEach {
                    Text(
                        text = "- $it",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify,
                    )
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Image(
                    painter = painterResource(id = R.drawable.protection),
                    contentDescription = firstWord,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(24.dp).shimmer(
                        shimmering = isLoading,
                        shape = RoundedCornerShape(0.dp)
                    )

                )
                Text(
                    text = "Prevention And Control",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.shimmer(
                        shimmering = isLoading,
                        shape = RoundedCornerShape(0.dp)
                    )
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))

            val prevetion = info.prevention
            Column(
                modifier = Modifier.shimmer(
                    shimmering = isLoading,
                    shape = RoundedCornerShape(0.dp)
                )
            ) {
                prevetion.forEach {
                    val title = it.title
                    val texts = it.text
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,

                        )
                        Spacer(
                            modifier = Modifier.padding(
                                top = 4.dp
                            )
                        )
                        texts.forEach {
                            Text(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                ),
                                text = "- $it",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Justify
                            )
                        }
                    }

                }
            }
        }

    }
}

@Preview
@Composable
private fun PestAndDiseasesDetailPrev() {
    CropSamaricaTheme {
        PestAndDiseaseDetailScreen(
            pestAndDisease = GOLDEN_APPLE_SNAIL.toNew(),
            onBack = {},
            language = "en"
        )
    }

}