package com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.repositories.auth.AuthRepository
import com.potatodevs.cropsamarica.repositories.ricefield.RiceFieldRepository
import com.potatodevs.cropsamarica.repositories.tasks.TaskRepository
import com.potatodevs.cropsamarica.repositories.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class ViewCropViewModel @Inject constructor(
    private val riceFieldRepository: RiceFieldRepository,
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository,
): ViewModel() {
    private var _state = MutableStateFlow(ViewCropState())
    val state : StateFlow<ViewCropState> = _state.asStateFlow()

    init {
        getFarmer()
    }
    fun events(e : ViewCropEvents) {
        when(e) {
            is ViewCropEvents.OnGetCrop -> {
                getCrop(e.id)

            }

            ViewCropEvents.OnGetFarmer -> getFarmer()
            is ViewCropEvents.OnGetTasks -> getTasks(e.id)
        }
    }

    private fun getTasks(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                tasks = _state.value.tasks.copy(isLoading = true)
            )
            taskRepository.getFertilizerApplications(id).onSuccess {
                _state.value = _state.value.copy(
                    tasks = _state.value.tasks.copy(
                        isLoading = false,
                        tasks = it
                    )
                )
            }
        }
    }

    private fun getCrop(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            riceFieldRepository.getById(id).onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false,
                    crop = it
                )
            }
        }
    }
    private fun getFarmer() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                farmer = _state.value.farmer.copy(isLoading = true)
            )

            userRepository.getFarmer().onSuccess {
                _state.value = _state.value.copy(
                    farmer = _state.value.farmer.copy(
                        isLoading = false,
                        user = it
                    )
                )
            }.onFailure {
                _state.value = _state.value.copy(
                    farmer = _state.value.farmer.copy(
                        isLoading = false
                    )
                )
            }
        }

    }
}