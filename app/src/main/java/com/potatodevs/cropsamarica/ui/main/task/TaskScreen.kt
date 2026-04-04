package com.potatodevs.cropsamarica.ui.main.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentDataType.Companion.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.potatodevs.cropsamarica.R
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.ui.common.LoadingScreen
import com.potatodevs.cropsamarica.ui.common.NoTaskFound
import com.potatodevs.cropsamarica.ui.common.TaskCard
import com.potatodevs.cropsamarica.ui.main.task.components.CreateTaskBottomSheet
import com.potatodevs.cropsamarica.ui.main.task.components.ViewTaskBottomSheet
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme
import kotlinx.coroutines.launch

@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,

    viewModel: TasksViewModel = hiltViewModel()
) {


    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val selectedTask = state.selectedTask
    val riceFields = state.riceFields
    if (selectedTask != null) {
        ViewTaskBottomSheet(
            task = selectedTask,
            onDismiss = {
                events(TaskEvent.OnTaskSelected(null))
            },
            onDelete = { id ->
                events(TaskEvent.OnDeleteTask(id))
            },
            onEdit = { task ->
                events(TaskEvent.OnUpdateTask(task))
            }
        )
    }
    if (riceFields.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            NoTaskFound {

            }
        }
    } else {
        TaskScreen(
            modifier = modifier,
            riceFields = riceFields,
            tasks = state.tasks,
            isLoading = state.isLoading,
            events = events
        )
    }


}
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    riceFields : List<RiceFieldWithRiceType>,
    tasks : List<Task>,
    isLoading : Boolean = false,

    events : (TaskEvent) -> Unit
) {
    val tasks = tasks

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        riceFields.size
    }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(
                        horizontal = 16.dp
                    )
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.task), style = MaterialTheme.typography.titleMedium)
                CreateTaskBottomSheet(
                    riceFields = riceFields.mapNotNull { it.riceField },
                    onSave = { task, result ->
                        events(TaskEvent.OnCreateTask(
                            task = task,
                            result= result
                        ))},
                    selectedRiceField = riceFields[pagerState.currentPage].riceField!!
                )
            }
        }

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
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
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) {page ->
            val riceField = riceFields[page].riceField
            val tasks = tasks.filter { it.fieldId == riceField?.id && it.stage == riceField.stage}
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    NoTaskFound {

                    }
                }

            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(tasks) { task ->
                        TaskCard(task = task, onClick = {
                            events(TaskEvent.OnTaskSelected(task))
                        })
                    }
                }
            }

        }
    }


}



@Preview(
    showBackground = true,
)
@Composable
private fun TaskScrenPrev() {
    CropSamaricaTheme {

       TaskScreen(
            riceFields = emptyList(),
            tasks = emptyList(),
            isLoading = false,
            events = {},
        )
    }

}