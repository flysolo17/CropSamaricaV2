package com.potatodevs.cropsamarica.ui.notifications.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.models.Notifications
import com.potatodevs.cropsamarica.repositories.notification.NotificationRepository
import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ViewNotificationState(
    val isLoading : Boolean = false,
    val notification : Notifications? = null,
)

sealed interface ViewNotificationEvents {
    data class LoadNotification(val id : String) : ViewNotificationEvents
    data class DeleteNotification(val id : String) : ViewNotificationEvents
}

@HiltViewModel
class ViewNotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
): ViewModel() {

    private var _state = MutableStateFlow(ViewNotificationState())
    val state = _state.asStateFlow()
    private var _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()

    fun events(event : ViewNotificationEvents) {
        when(event) {
            is ViewNotificationEvents.DeleteNotification -> deleteNotification(event.id)
            is ViewNotificationEvents.LoadNotification -> getNotification(event.id)
        }
    }

    private fun deleteNotification(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            notificationRepository.deleteNotification(id).onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false
                )
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it))
                _oneTimeEvents.send(OneTimeEvents.NavigateBack)
            }.onFailure {
                _state.value = _state.value.copy(
                    isLoading = false
                )
            }

        }
    }

    private fun getNotification(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            notificationRepository.getNotificationById(id).onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false,
                    notification = it
                )
            }.onFailure {
                _state.value = _state.value.copy(
                    isLoading = false
                )
            }
        }
    }


}