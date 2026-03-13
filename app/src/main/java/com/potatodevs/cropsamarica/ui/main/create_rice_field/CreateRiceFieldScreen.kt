package com.potatodevs.cropsamarica.ui.main.create_rice_field

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.potatodevs.cropsamarica.models.Municipality
import com.potatodevs.cropsamarica.models.rice.IrrigationType
import com.potatodevs.cropsamarica.models.rice.RiceType
import com.potatodevs.cropsamarica.models.rice.SoilTypes
import com.potatodevs.cropsamarica.ui.auth.components.BarangaySelector
import com.potatodevs.cropsamarica.ui.auth.components.MunicipalitySelector
import com.potatodevs.cropsamarica.ui.main.common.CollapsingToolbar
import com.potatodevs.cropsamarica.ui.main.common.DateSelector
import com.potatodevs.cropsamarica.ui.main.common.ImagePicker
import com.potatodevs.cropsamarica.ui.main.common.IrrigationSelector
import com.potatodevs.cropsamarica.ui.main.common.RecommendationDialog
import com.potatodevs.cropsamarica.ui.main.common.RiceTypeSelector
import com.potatodevs.cropsamarica.ui.main.common.SoilTypeSelector
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme
import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents
import com.potatodevs.cropsamarica.ui.utils.showToast
import java.util.Date

@Composable
fun CreateRiceFieldScreen(
    modifier: Modifier = Modifier,
    onBack : () -> Unit,
    viewModel: CreateRiceFieldViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val oneTimeEvents = viewModel.oneTimeEvents
    val context = LocalContext.current
    LaunchedEffect(oneTimeEvents) {
        oneTimeEvents.collect {
            when(it) {
                is OneTimeEvents.Navigate -> {
                    onBack()
                }
                OneTimeEvents.NavigateBack -> {
                    onBack()
                }
                is OneTimeEvents.ShowToast -> {
                    context.showToast(
                        it.message
                    )
                }
            }
        }

    }
    if (state.recommendations.isNotEmpty()) {
        RecommendationDialog(
            tasks = state.recommendations,
            onDismiss = {
                onBack()
            },
            onCreateTask = {
                events(CreateRiceFieldEvents.OnCreateTask(it))

            }
        )
    }
    LaunchedEffect(state.municipality, state.barangay) {
        if (state.municipality != null && state.barangay.isNotEmpty()) {
            val location = "${state.barangay}, ${state.municipality!!.name}"
            events(CreateRiceFieldEvents.OnGetWeather(location))
        }
    }
    CreateRiceFieldScreen(
        onBack = onBack,
        isLoading = state.isLoading,
        areaSize = state.areaSize,
        plantedDate = state.plantedDate,
        name = state.name,
        municipality = state.municipality,
        barangay = state.barangay,
        riceTypes = state.riceTypes,
        selectedRiceType = state.variety,
        soilType = state.soilTypes,
        irrigationType = state.irrigationType,
        selectedImageUri = state.selectedImage,
        events = events
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRiceFieldScreen(
    modifier: Modifier = Modifier,
    isLoading : Boolean = false,
    name : String,
    areaSize : String,
    municipality: Municipality?,
    plantedDate : Date?,
    barangay : String,
    riceTypes : List<RiceType>,
    selectedRiceType : RiceType?,
    soilType : SoilTypes?,
    irrigationType: IrrigationType,
    selectedImageUri : Uri? = null,
    events : (CreateRiceFieldEvents) -> Unit = {},
    onBack : () -> Unit
) {

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    enabled = !isLoading,
                    onClick = {
                        events(CreateRiceFieldEvents.Submit)
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(6.dp)
                    ){
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        } else {
                            Text(
                                text = "Submit",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                }
            }
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Text(
                        "Create Rice Field",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }
    ) { innerPadding ->
        val color = TextFieldDefaults.colors()
        val shape = MaterialTheme.shapes.medium
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                Text(
                    "Ricefield Information".uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.inverseSurface
                    )
                )
            }
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = {
                        events(CreateRiceFieldEvents.OnNameChange(it))
                    },
                    label = { Text("Rice field name *") },
                    shape = shape,
                    colors = color,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = androidx.compose.ui.text.input.ImeAction.Next
                    ),
                    singleLine = true
                )
            }
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                MunicipalitySelector(
                    selectedMunicipality = municipality,
                    onMunicipalitySelected = {
                        events(CreateRiceFieldEvents.OnMunicipalityChange(it))
                    }
                )
            }
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                BarangaySelector(
                    selectedBarangay = barangay,
                    barangays =municipality?.barangays ?: emptyList(),
                    onBarangaySelected = {
                        events(CreateRiceFieldEvents.OnBarangayChange(it))
                    }
                )
            }
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                Text(
                    "Crop Information".uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.inverseSurface
                    )
                )
            }
            item {
                DateSelector(
                    selected = plantedDate,
                    onSelected = {
                        events(CreateRiceFieldEvents.OnPlantedDateChange(it))
                    }
                )
            }
            item {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = areaSize,
                    onValueChange = {
                        events(CreateRiceFieldEvents.OnAreaChange(it))
                    },
                    label = { Text("Area size *") },
                    shape = shape,
                    colors = color,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = androidx.compose.ui.text.input.ImeAction.Next
                    )
                )
            }
            item(
                span = { GridItemSpan(2) }
            ){
                RiceTypeSelector(
                    selectedRiceType = selectedRiceType,
                    riceTypes = riceTypes,
                    onRiceTypeSelected = { variety ->
                        events(CreateRiceFieldEvents.OnVarietyChange(variety))
                    }
                )
            }
            item(
                span = { GridItemSpan(2) }
            ){
                SoilTypeSelector(
                    selected = soilType,
                    onSelected = {
                        events(CreateRiceFieldEvents.OnSoilTypeChange(it))
                    }
                )
            }
            item(
                span = { GridItemSpan(2) }
            ){
                IrrigationSelector(
                    selected = irrigationType,
                    onSelected = {
                        events(CreateRiceFieldEvents.OnIrrigationTypeChange(it))
                    }
                )
            }
            item(
                span = { GridItemSpan(2) }) {
                ImagePicker(
                    selectedImageUri = selectedImageUri,
                    onImageSelected = {
                        events(CreateRiceFieldEvents.OnImageChange(it))
                    }
                )
            }
        }
    }
}


@Preview
@Composable
private fun CreateRiceFieldScreenPrev() {
    CropSamaricaTheme {
        CreateRiceFieldScreen(
            onBack = {}
        )
    }
}