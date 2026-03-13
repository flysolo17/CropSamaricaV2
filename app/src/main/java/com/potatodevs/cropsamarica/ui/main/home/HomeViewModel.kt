package com.potatodevs.cropsamarica.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.datastore.FieldDataStore
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.rice.RiceStage
import com.potatodevs.cropsamarica.models.weather.DailyForecast
import com.potatodevs.cropsamarica.models.weather.toDailyForecastUI
import com.potatodevs.cropsamarica.repositories.ricefield.RiceFieldRepository
import com.potatodevs.cropsamarica.repositories.tasks.TaskRepository
import com.potatodevs.cropsamarica.repositories.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val  fieldDataStore: FieldDataStore,
    private val riceFieldRepository: RiceFieldRepository,
    private val weatherRepository: WeatherRepository,
    private val taskRepository: TaskRepository
): ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
    fun events(e : HomeEvents) {
        when(e) {
            is HomeEvents.OnGetWeather -> {
                getWeather(e.location)
            }

            is HomeEvents.OnGetTasks -> {
                getTasks(e.id, e.stage)
            }

            is HomeEvents.OnGetAnnouncemnt -> getAnnouncement(e.riceFieldWithRiceType, e.weather)
        }
    }

    private fun getAnnouncement(
        riceFieldWithRiceType: RiceFieldWithRiceType,
        weather: DailyForecast
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    announcement = it.announcement.copy(
                        isLoading = true
                    )

                )
            }
            riceFieldRepository.generateAnnouncement(riceFieldWithRiceType,weather).onSuccess {data ->
               _state.update {
                   it.copy(
                       announcement = it.announcement.copy(
                           isLoading = false,
                           announcement = data
                       )
                   )
               }
           }

        }
    }

    private fun getTasks(id: String, stage: RiceStage) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    tasks = it.tasks.copy(
                        isLoading = true
                    )
                )
            }
            taskRepository.getTasksByCropIdAndStage(id, stage)
                .onSuccess { data ->
                    _state.update {
                        it.copy(
                            tasks = it.tasks.copy(
                                tasks = data,
                                isLoading = false
                            )
                        )
                    }
                }.onFailure {

                }

        }
    }

    private fun getWeather(location: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    weather = it.weather.copy(
                        isLoading = true
                    )
                )
            }
            weatherRepository.getDailyForecast(location,1).onSuccess { data ->
                _state.update {
                    it.copy(
                        weather = it.weather.copy(
                            weather = data.toDailyForecastUI(),
                            isLoading = false
                        )
                    )
                }
            }.onFailure {
                _state.update {
                    it.copy(
                        weather = it.weather.copy(
                            isLoading = false
                        )
                    )
                }
            }
        }
    }
}