package com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.repositories.auth.AuthRepository
import com.potatodevs.cropsamarica.repositories.ricefield.RiceFieldRepository
import com.potatodevs.cropsamarica.repositories.tasks.TaskRepository
import com.potatodevs.cropsamarica.repositories.user.UserRepository
import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


@HiltViewModel
class ViewCropViewModel @Inject constructor(
    private val riceFieldRepository: RiceFieldRepository,

    private val taskRepository: TaskRepository,
): ViewModel() {
    private var _state = MutableStateFlow(ViewCropState())
    val state : StateFlow<ViewCropState> = _state.asStateFlow()
    private var _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()

    fun events(e : ViewCropEvents) {
        when(e) {
            is ViewCropEvents.GetRiceField -> initializeData(
                riceFieldId = e.riceFieldId
            )

            is ViewCropEvents.OnStageSelected -> _state.value = _state.value.copy(
                selectedTab = e.stage
            )

            is ViewCropEvents.OnDeleteCrop -> deleteCrop(e.id)
        }
    }

    private fun deleteCrop(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            riceFieldRepository.deleteCropField(id).onSuccess {
                _state.value = _state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it))
                _oneTimeEvents.send(OneTimeEvents.NavigateBack)
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message.toString()))
            }
        }
    }

    private fun initializeData(riceFieldId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
           riceFieldRepository.getById(riceFieldId).onSuccess {
               _state.value = _state.value.copy(
                   isLoading = false,
                   riceField = it
               )
           }.onFailure {
               _state.value = _state.value.copy(isLoading = false)
           }

        }



    }


}