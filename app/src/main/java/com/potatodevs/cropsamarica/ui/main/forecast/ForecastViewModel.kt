package com.potatodevs.cropsamarica.ui.main.forecast

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.models.reminder.Reminder
import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.weather.SevenDayForecast
import com.potatodevs.cropsamarica.models.weather.SevenDayWeatherResponse
import com.potatodevs.cropsamarica.repositories.reminder.ReminderRepository
import com.potatodevs.cropsamarica.repositories.ricefield.RiceFieldRepository
import com.potatodevs.cropsamarica.repositories.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val reminderRepository: ReminderRepository,
    private val riceFieldRepository: RiceFieldRepository
): ViewModel() {

    private var _state = MutableStateFlow(ForecastState())
    val state = _state.asStateFlow()

    fun events(e : ForecastEvents) {
        when(e) {
            is ForecastEvents.OnGetWeather -> {
                getWeather(e.location)
            }
            is ForecastEvents.OnGetRiceField -> {
                getRiceField(e.id)
            }

            is ForecastEvents.OnGenerateReminder -> generateReminder(
                e.riceField,
                e.weather
            )

            is ForecastEvents.GetRemindersToday -> getRemindersToday(e.id)
            is ForecastEvents.OnNotify -> notify(e.reminder)
        }
    }

    private fun notify(reminder: Reminder) {
        viewModelScope.launch {
            reminderRepository.createReminders(reminder)
        }
    }

    private fun getRemindersToday(id: String) {
        reminderRepository.getRemindersToday(id)
            .onEach {

                _state.value = _state.value.copy(
                    reminderState = ReminderState(
                        isLoading = false,
                        reminders = it
                    )
                )
                Log.d("AyaViewModel", "reminders: ${it}")
        }.launchIn(viewModelScope)
    }

    private fun generateReminder(
        riceField: RiceFieldWithRiceType,
        weather: SevenDayWeatherResponse
    ) {

        viewModelScope.launch {
            _state.value = _state.value.copy(
                aiReminderState = AiReminderState(
                    isLoading = true
                )
            )
            reminderRepository.generateReminder(
                riceField = riceField,
                forecast = weather,
            ).onSuccess {
                _state.value = _state.value.copy(
                    aiReminderState = AiReminderState(
                        isLoading = false,
                        reminders = it
                    )
                )
            }.onFailure {
                _state.value = _state.value.copy(
                    aiReminderState = AiReminderState(
                        isLoading = false,
                        error = it.message
                    )
                )
            }
        }
    }

    private fun getWeather(location: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                weatherState = WeatherState(
                    isLoading = true
                )
            )
            weatherRepository.getSevenDayWeather(location)
                .onSuccess {
                    _state.value = _state.value.copy(
                        weatherState = WeatherState(
                            isLoading = false,
                            weather = it
                        )
                    )
                }
        }
    }

    private fun getRiceField(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                riceFieldState = RiceFieldState(
                    isLoading = true
                )
            )
            riceFieldRepository.getById(id).onSuccess {
                _state.value = _state.value.copy(
                    riceFieldState = RiceFieldState(
                        isLoading = false,
                        riceField = it
                    )
                )
            }

        }
    }


}