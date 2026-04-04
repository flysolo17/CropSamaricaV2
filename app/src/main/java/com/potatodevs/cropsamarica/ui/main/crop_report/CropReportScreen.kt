package com.potatodevs.cropsamarica.ui.main.crop_report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.potatodevs.cropsamarica.R
import com.potatodevs.cropsamarica.models.User
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.ui.main.crop_report.components.FarmInformationCard
import com.potatodevs.cropsamarica.ui.main.crop_report.components.FarmerCard
import com.potatodevs.cropsamarica.ui.main.crop_report.components.FertilizerCard
import com.potatodevs.cropsamarica.ui.main.crop_report.components.HarverInformationCard
import com.potatodevs.cropsamarica.ui.main.home.TaskState
import kotlinx.coroutines.launch


@Composable
fun CropReportScreen(
    modifier: Modifier = Modifier,
    user: User?,
    riceFields : List<RiceFieldWithRiceType>,
     viewModel: CropReportViewModel = hiltViewModel()
) {
    val state by  viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val pagerState = rememberPagerState(
        initialPage = 0,
    ) {
        riceFields.size
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = riceFields) {
        if (riceFields.isNotEmpty()) {
            events(CropReportEvents.OnGetFertilizerTasks(
                ids = riceFields.mapNotNull { it.riceField?.id }
            ))
        }
    }
    Scaffold(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (riceFields.isEmpty()) {
                Text(text = "No Rice Fields Found")
            } else {
                Text(
                    text = stringResource(R.string.crop_report),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    edgePadding = 0.dp,

                    ) {
                    riceFields.forEachIndexed { index, rice ->
                        val selected = pagerState.currentPage == index
                        Tab(
                            selected = selected,

                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },

                            text = {
                                Text(
                                    text = rice.riceField?.name ?: "",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                            }
                        )
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val riceField = riceFields[page]
                    CropReportContent(
                        tasks = state.tasks,
                        user = user,
                        riceField = riceField
                    )
                }
            }
        }
    }
}


@Composable
fun CropReportContent(
    modifier: Modifier = Modifier,
    tasks : FertilizerState,
    user: User?,
    riceField : RiceFieldWithRiceType,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        item {
            FarmerCard(
                farmer = FarmerState(
                    user = user
                ),
            )
        }
        item {
            FarmInformationCard(
                crop = riceField,
                isLoading = false
            )
        }
        item {
            FertilizerCard(
                tasks = tasks.tasks.filter {
                    it.fieldId == riceField.riceField?.id
                },
                isLoading = tasks.isLoading
            )
        }
        item {
            HarverInformationCard(
                crop = riceField,
                isLoading = false
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
