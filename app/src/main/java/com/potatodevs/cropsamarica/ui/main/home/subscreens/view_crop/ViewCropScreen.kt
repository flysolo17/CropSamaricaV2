package com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.ui.main.home.TaskState
import com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop.components.FarmInformationCard
import com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop.components.FarmerCard
import com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop.components.FertilizerCard
import com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop.components.HarverInformationCard
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme


@Composable
fun ViewCropScreen(
    modifier: Modifier = Modifier,
    id : String,
    onBack : () -> Unit,
    viewModel: ViewCropViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    LaunchedEffect(id) {
        if (id.isNotBlank()) {
            events(ViewCropEvents.OnGetCrop(id))
            events(ViewCropEvents.OnGetTasks(id))
        }

    }
    ViewCropScreen(
        farmer = state.farmer,
        isLoading = state.isLoading,
        crop = state.crop,
        tasks = state.tasks,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewCropScreen(
    modifier: Modifier = Modifier,
    farmer : FarmerState,
    isLoading : Boolean,
    crop : RiceFieldWithRiceType ?,
    tasks : FertilizerState,
    onBack : () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Crop Report")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                FarmerCard(
                    farmer = farmer
                )
            }
            item {
                FarmInformationCard(
                    crop = crop ?: RiceFieldWithRiceType(),
                    isLoading = isLoading,
                )
            }
            item {
                FertilizerCard(
                    tasks = tasks.tasks,
                    isLoading = tasks.isLoading
                )
            }
            item {
                HarverInformationCard(
                    crop = crop ?: RiceFieldWithRiceType(),
                    isLoading = isLoading
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription   = "",
                            modifier = Modifier.size(36.dp)
                        )
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Disclamer", style = MaterialTheme.typography.titleSmall)
                            Text("Harvest estimates are for planning purposes only. Actual may vary due to field conditions", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun ViewCropScreenPrev() {
    CropSamaricaTheme {


    }

}