package com.potatodevs.cropsamarica.ui.main.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.potatodevs.cropsamarica.R
import com.potatodevs.cropsamarica.models.NotificationStatus
import com.potatodevs.cropsamarica.models.Notifications
import com.potatodevs.cropsamarica.models.User
import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.rice.RiceStage
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.ui.common.LoadingScreen
import com.potatodevs.cropsamarica.ui.common.NoTaskFound
import com.potatodevs.cropsamarica.ui.common.TaskCard
import com.potatodevs.cropsamarica.ui.main.MainViewModel
import com.potatodevs.cropsamarica.ui.main.home.components.AnnouncementCard
import com.potatodevs.cropsamarica.ui.main.home.components.NextStageCard
import com.potatodevs.cropsamarica.ui.main.home.components.NoRiceFieldContent
import com.potatodevs.cropsamarica.ui.main.home.components.ProfileImage
import com.potatodevs.cropsamarica.ui.main.home.components.RiceFieldCard
import com.potatodevs.cropsamarica.ui.main.home.components.SelectedRiceFieldCard
import com.potatodevs.cropsamarica.ui.main.home.components.WeatherCard
import com.potatodevs.cropsamarica.ui.theme.shimmer
import com.potatodevs.cropsamarica.ui.utils.getRiceStage
import com.potatodevs.cropsamarica.ui.utils.showToast
import java.util.Date
import kotlin.compareTo
import kotlin.toString

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onViewProfile : () -> Unit,
    isLoading : Boolean,
    selectedRiceField: RiceFieldWithRiceType? = null,
    user : User? = null,
    onCreateRiceField : () -> Unit,
    toggleDrawer : () -> Unit,
    navigateToCropReports : (id : String) -> Unit,
    onNextStage : (id : String) -> Unit,
    navigateToWeather : (id : String) -> Unit,
    navigateToNotification : () -> Unit,
    viewModel : HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    LaunchedEffect(selectedRiceField) {
        selectedRiceField?.riceField?.location?.let {
            events(HomeEvents.OnGetWeather(it))
            val stage = selectedRiceField.riceField.stage
            events(HomeEvents.OnGetTasks(
                id = selectedRiceField.riceField.id,
                stage = stage))
        }
    }
    LaunchedEffect(selectedRiceField, state.weather.weather) {
        if (selectedRiceField != null && state.weather.weather != null) {
            events(HomeEvents.OnGetAnnouncemnt(
                riceFieldWithRiceType = selectedRiceField,
                weather = state.weather.weather!!
            ))
        }
    }

    HomeScreen(
        navigateToNotification = navigateToNotification,
        toggleDrawer = toggleDrawer,
        onViewProfile = onViewProfile,
        onCreateRiceField = onCreateRiceField,
        modifier = modifier,
        isLoading = isLoading,
        user = user,
        selectedRiceField = selectedRiceField,
        weather = state.weather,
        notifications = state.notifications,
        tasks = state.tasks,
        onViewRiceField = {
            navigateToCropReports(it)
        },
        language = state.language,
        navigateToWeather = navigateToWeather,
        announcements = state.announcement,
        onNextStage = onNextStage
    )
}

@Composable
fun HomeScreen(
    toggleDrawer : () -> Unit,
    onViewProfile : () -> Unit,
    navigateToWeather : (id : String) -> Unit,
    modifier: Modifier = Modifier,
    onCreateRiceField : () -> Unit,
    isLoading : Boolean,
    user : User?,
    notifications : List<Notifications>,
    selectedRiceField : RiceFieldWithRiceType?,
    weather : WeatherState,
    tasks : TaskState,
    announcements : AnnouncementState,
    language : String,
    onViewRiceField : (id : String) -> Unit,
    navigateToNotification : () -> Unit,
    onNextStage : (id : String) -> Unit
) {
    if (!isLoading && selectedRiceField == null) {
        NoRiceFieldContent(
            onCreate = onCreateRiceField,
            content = {
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val badgeCount = 0
                    BadgedBox(
                        badge = {
                        }
                    ) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.shimmer(shimmering = isLoading, shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications"
                            )
                        }
                    }


                    ProfileImage(
                        modifier = Modifier.shimmer(shimmering = isLoading, shape = CircleShape),
                        profile = user?.profile,
                        name = user?.name ?: "Unknown User",
                        imageSize = 40.dp,
                        onClick = {
                            onViewProfile()
                        }
                    )
                }
            }
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SelectedRiceFieldCard(
                        modifier = Modifier.shimmer(shimmering = isLoading),
                        selectedRiceField = selectedRiceField?.riceField,
                        onClick = toggleDrawer
                    )
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val badgeCount = notifications.filter {
                            it.status == NotificationStatus.unseen.name
                        }.size
                        BadgedBox(
                            badge = {
                                Badge {
                                    if (badgeCount > 0) {
                                        Badge {
                                            Text(badgeCount.toString())
                                        }
                                    }
                                }

                            }
                        ) {
                            IconButton(
                                onClick = {
                                    navigateToNotification()
                                },
                                modifier = Modifier.shimmer(shimmering = isLoading, shape = CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications"
                                )
                            }
                        }

                        ProfileImage(
                            modifier = Modifier.shimmer(shimmering = isLoading, shape = CircleShape),
                            profile = user?.profile,
                            name = user?.name ?: "Unknown User",
                            imageSize = 40.dp,
                            onClick = {
                                onViewProfile()
                            }
                        )
                    }
                }
            }
            item {
                AnnouncementCard(
                    language = language,
                    isLoading = announcements.isLoading || isLoading,
                    announcement = announcements.announcement
                )
            }

            item {
                WeatherCard(
                    modifier = Modifier.shimmer(shimmering = weather.isLoading || isLoading, shape = MaterialTheme.shapes.large),
                    weather = weather.weather,
                    onClick = {
                        navigateToWeather(selectedRiceField?.riceField?.id.orEmpty())
                    }
                )
            }

            item {
                RiceFieldCard(
                    modifier = Modifier.shimmer(shimmering = isLoading, shape = MaterialTheme.shapes.large),
                    data = selectedRiceField,
                    onClick = {
                        onViewRiceField(selectedRiceField?.riceField?.id.orEmpty())
                    }
                )
            }
            val readyForNextStage = selectedRiceField?.riceField?.let {
                val currentStage = Date(it.plantedDate).getRiceStage()
                it.stage != RiceStage.MATURE && currentStage != it.stage
            } ?: false
            if (readyForNextStage) {
                item {
                    NextStageCard(
                        stage = selectedRiceField.riceField.stage,
                        onNextStage = {
                            onNextStage(selectedRiceField.riceField.id)
                        }
                    )
                }
            }
            item {
                Text(
                    stringResource(R.string.current_task),
                    modifier = Modifier.shimmer(shimmering = tasks.isLoading || isLoading),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            items(tasks.tasks, key = {it.id}) {
                TaskCard(
                    task = it,
                    isLoading = tasks.isLoading || isLoading,
                )
            }
            if (tasks.tasks.isEmpty() && !tasks.isLoading && !isLoading) {
                item {
                    NoTaskFound(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                    }
                }
            }

        }
    }
}