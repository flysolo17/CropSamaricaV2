package com.potatodevs.cropsamarica.ui.main.pest

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.potatodevs.cropsamarica.models.pests.PestAndDisease
import com.potatodevs.cropsamarica.models.rice.RiceStage

import com.potatodevs.cropsamarica.ui.theme.shimmer
import com.potatodevs.cropsamarica.ui.utils.getIcon

@Composable
fun PestScreen(
    modifier: Modifier = Modifier,

    viewModel: PestViewModel = hiltViewModel(),
    onViewDetails : (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    PestScreen(

        isLoading = state.isLoading,
        pests = state.pests,
        onViewDetails = onViewDetails,
        language = state.language
    )

}
@Composable
fun PestScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    pests: List<PestAndDisease>,
    onViewDetails: (String) -> Unit,
    language : String
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(text = "Pest And Diseases ", style = MaterialTheme.typography.titleMedium)
        }
        val stages = RiceStage.entries

        items(stages) { stage ->
            val filteredPestAndDiseases = pests.filter { it.stages.contains(stage) }
            if (filteredPestAndDiseases.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                ) {

                    Row(
                        modifier = Modifier.padding(
                            bottom = 8.dp
                        ),
                        verticalAlignment = androidx.compose.ui.Alignment.Bottom,
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
                            8.dp
                        )
                    ) {
                        Image(
                            painter = painterResource(id = stage.getIcon()),
                            contentDescription = stage.name,
                            modifier = Modifier.size(32.dp).shimmer(shimmering = isLoading)
                        )
                        Text(
                            text = stage.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.shimmer(shimmering = isLoading)
                        )
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(
                                state = androidx.compose.foundation.rememberScrollState()
                            ),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
                            8.dp
                        ),
                    ) {
                        filteredPestAndDiseases.forEach {
                            PestAndDiseaseItem(
                                language = language,
                                pestAndDisease = it,
                                isLoading = isLoading,
                                onClick = {
                                    onViewDetails(it.id) }
                                )
                        }
                    }
                }
            }
        }
    }
}

