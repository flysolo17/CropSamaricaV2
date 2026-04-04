package com.potatodevs.cropsamarica.ui.main.crop_report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.repositories.tasks.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class CropReportViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private var _state = MutableStateFlow(
        CropReportState()
    )
    val state = _state.asStateFlow()


    fun events(e : CropReportEvents) {
        when(e) {
            is CropReportEvents.OnGetFertilizerTasks -> {
                getFertilizerTasks(e.ids)
            }

        }
    }

    private fun getFertilizerTasks(ids: List<String>) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                tasks = _state.value.tasks.copy(
                    isLoading = true
                )
            )
            taskRepository.getFertilizerTasks(ids)
                .onSuccess { tasks ->
                    _state.value = _state.value.copy(
                        tasks = _state.value.tasks.copy(
                            isLoading = false,
                            tasks = tasks
                        )
                    )
                }

        }
    }


}