package com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.rice.RiceStage
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.ui.common.LoadingScreen
import com.potatodevs.cropsamarica.ui.errors.ErrorScreen
import com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop.components.DeleteCropDialog
import com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop.components.StageItem
import com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop.components.ViewAboutInfo
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme
import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents
import com.potatodevs.cropsamarica.ui.utils.showToast


@Composable
fun ViewCropScreen(
    modifier: Modifier = Modifier,
    id : String,
    onBack : () -> Unit,
    viewModel: ViewCropViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val oneTimeEvents = viewModel.oneTimeEvents
    val context = LocalContext.current
    LaunchedEffect(id) {
        id.isNotEmpty().let {
            events(ViewCropEvents.GetRiceField(id))
        }
    }

    LaunchedEffect(oneTimeEvents) {
        oneTimeEvents.collect { event ->
            when (event) {
                is OneTimeEvents.Navigate -> {
                    onBack()
                }
                OneTimeEvents.NavigateBack -> {
                  onBack()
                }

                is OneTimeEvents.ShowToast -> {
                    context.showToast(event.message)

                }
            }
        }
    }
    ViewCropScreen(
        modifier = modifier,
        riceFieldWithRiceType = state.riceField,
        isLoading = state.isLoading,

        selectedStage = state.selectedTab,
        onBack = onBack,
        onStageSelected = {
            events(ViewCropEvents.OnStageSelected(it))
        },
        onDeleteCrop = {
            events(ViewCropEvents.OnDeleteCrop(it))
        },

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewCropScreen(
    modifier: Modifier = Modifier,
    riceFieldWithRiceType: RiceFieldWithRiceType?,
    isLoading: Boolean = false,


    selectedStage: RiceStage,
    onDeleteCrop : (String) -> Unit,
    onStageSelected : (RiceStage) -> Unit = {},
    onBack : () -> Unit
) {
    val riceField = riceFieldWithRiceType?.riceField
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = riceField?.name.orEmpty())
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (riceField != null) {
                        DeleteCropDialog(
                            name = riceField.name,
                        ) {
                            onDeleteCrop(riceField.id)
                        }
                        ViewAboutInfo(
                            type = riceFieldWithRiceType.riceType?.name.orEmpty(),
                            riceField = riceField
                        )
                    }
                }
            )

        }
    ) {
        if (isLoading) {
            LoadingScreen(
                modifier = modifier
                    .fillMaxSize()
                    .padding(it),

            )
        } else if (riceField != null) {
            val stages = RiceStage.entries
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(it),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),

                ) {
                val stages = RiceStage.entries
                itemsIndexed(
                    items = stages,
                    key = { _, stage -> stage.ordinal }
                ) { index, stage ->
                    StageItem(
                        selected = index == selectedStage.ordinal,
                        isLast = index == stages.size - 1,
                        stage = stage,
                        isCurrentStage = selectedStage == stage,
                        onClick = {
                            onStageSelected(stage)
                        }
                    )
                }
            }
        } else {
            ErrorScreen {
                onBack()
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