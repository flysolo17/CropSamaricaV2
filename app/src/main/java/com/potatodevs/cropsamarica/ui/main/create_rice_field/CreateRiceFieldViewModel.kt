package com.potatodevs.cropsamarica.ui.main.create_rice_field

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.rice.RiceStage
import com.potatodevs.cropsamarica.models.rice.SoilTypes
import com.potatodevs.cropsamarica.models.rice.getHarvestDate
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.repositories.riceTypes.RiceTypeRepository
import com.potatodevs.cropsamarica.repositories.ricefield.RiceFieldRepository
import com.potatodevs.cropsamarica.repositories.tasks.TaskRepository
import com.potatodevs.cropsamarica.repositories.weather.WeatherRepository
import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents
import com.potatodevs.cropsamarica.ui.utils.getRiceStage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@HiltViewModel
class CreateRiceFieldViewModel @Inject constructor(
    private val riceTypeRepository: RiceTypeRepository,
    private val weatherRepository: WeatherRepository,
    private val riceFieldRepository: RiceFieldRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private var _state = MutableStateFlow(CreateRiceFieldState())
    val state : StateFlow<CreateRiceFieldState> = _state.asStateFlow()

    private val _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()
    init {
        getRiceTypes()
    }
    fun events(e : CreateRiceFieldEvents) {
        when(e) {
            is CreateRiceFieldEvents.OnAreaChange -> {
                _state.value = _state.value.copy(areaSize = e.area)
            }
            is CreateRiceFieldEvents.OnBarangayChange -> {
                _state.value = _state.value.copy(barangay = e.barangay)
            }
            is CreateRiceFieldEvents.OnMunicipalityChange -> {
                _state.value = _state.value.copy(municipality = e.municipality)
            }
            is CreateRiceFieldEvents.OnNameChange -> {
                _state.value = _state.value.copy(name = e.name)
            }
            is CreateRiceFieldEvents.OnPlantedDateChange -> {
                _state.value = _state.value.copy(plantedDate = e.plantedDate)
            }
            is CreateRiceFieldEvents.OnVarietyChange -> {
                _state.value = _state.value.copy(variety = e.variety)
            }
            CreateRiceFieldEvents.Submit -> {
                submit()
            }

            is CreateRiceFieldEvents.OnIrrigationTypeChange -> {
                _state.value = _state.value.copy(irrigationType = e.irrigationType)
            }
            is CreateRiceFieldEvents.OnSoilTypeChange -> {
                _state.value = _state.value.copy(soilTypes = e.soilType)
            }

            is CreateRiceFieldEvents.OnImageChange -> {
                _state.value = _state.value.copy(selectedImage = e.image)
            }

            is CreateRiceFieldEvents.OnGetWeather -> {
                getWeather(
                    location = e.location
                )
            }

            is CreateRiceFieldEvents.OnCreateTask -> saveTasks(e.tasks)
        }

    }

    private fun saveTasks(tasks: List<Task>) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            taskRepository.insertAll(tasks).onSuccess {
                _state.value = _state.value.copy(isLoading = false,recommendations = emptyList())
                _oneTimeEvents.send(OneTimeEvents.ShowToast("Successfully created tasks"))
                _oneTimeEvents.send(OneTimeEvents.NavigateBack)

            }.onFailure {
                _state.value = _state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message ?: "Something went wrong"))
                Log.d("CreateRiceFieldViewModel", "saveTasks: ${it.message}")
                _oneTimeEvents.send(OneTimeEvents.NavigateBack)

            }
        }
    }

    private fun getWeather(location: String) {
        viewModelScope.launch {
            weatherRepository.getDailyForecast(location,3).onSuccess { response ->
                val dailyHighLow = response.forecast.forecastday.map { dayData ->
                    "High ${dayData.day.maxtemp_c} - Low ${dayData.day.mintemp_c}"
                }
                _state.value = _state.value.copy(dailyHighLow = dailyHighLow)
            }
        }
    }

    private fun submit() {
        val data = state.value
        val location = "${data.barangay}, ${data.municipality!!.name}, Mindoro"

        val stage = data.plantedDate?.getRiceStage() ?: RiceStage.SEEDLING
        val harvestDate = data.plantedDate?.getHarvestDate(
            data.variety!!
        )
        _state.value = _state.value.copy(isLoading  = true)

        val riceField = RiceField(
            name = data.name,
            location = location,
            plantedDate = data.plantedDate?.time ?: System.currentTimeMillis(),
            areaSize = data.areaSize.toDoubleOrNull() ?: 1.0,
            variety = data.variety?.id ?: "",
            irrigationType = data.irrigationType,
            soilType = data.soilTypes ?: SoilTypes.CLAY,
            expectedHarvestDate = harvestDate?.time,
            stage = stage,
        )

        viewModelScope.launch {
            riceFieldRepository.create(
                riceField = riceField,
                selectedRiceType = _state.value.variety!!,
                dailyHighLow = _state.value.dailyHighLow
            ).onSuccess {
                Log.d("CreateRiceFieldViewModel", "submit: $it")
                _state.value = _state.value.copy(isLoading = false,recommendations = it)
                _oneTimeEvents.send(OneTimeEvents.ShowToast("Successfully created rice field"))

                if (it.isEmpty()) {
                    _oneTimeEvents.send(OneTimeEvents.NavigateBack)
                }
            }.onFailure {
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message ?: "Something went wrong"))

                Log.d("CreateRiceFieldViewModel", "submit: ${it.message}")
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    fun getRiceTypes() {
        viewModelScope.launch {
            val riceTypes = riceTypeRepository.getRiceTypes()
            _state.value = _state.value.copy(riceTypes = riceTypes)

        }
    }
}