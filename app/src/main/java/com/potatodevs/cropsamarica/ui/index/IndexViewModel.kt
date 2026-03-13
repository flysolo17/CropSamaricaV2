package com.potatodevs.cropsamarica.ui.index

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.models.User
import com.potatodevs.cropsamarica.repositories.auth.AuthRepository
import com.potatodevs.cropsamarica.repositories.riceTypes.RiceTypeRepository
import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class IndexState(
    val isLoading : Boolean = false,
    val uid : String? = null,
    val user : User ? = null
)
sealed interface IndexEvents {
    data object OnGetCurrentUser : IndexEvents
}
@HiltViewModel
class IndexViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val riceTypeRepository: RiceTypeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(IndexState())
    val state: StateFlow<IndexState> = _state.asStateFlow()

    private var _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()
    init {
        viewModelScope.launch {
            riceTypeRepository.addYield()
        }
        events(IndexEvents.OnGetCurrentUser)
    }
    fun events(e : IndexEvents) {
        when(e) {
            IndexEvents.OnGetCurrentUser -> {
                getUser()
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(1000)
            val user = authRepository.getCurrentUser()
            if (user !== null) {
                _oneTimeEvents.send(OneTimeEvents.ShowToast("Successfully Logged in."))
            }
            _state.update { it.copy(isLoading = false, uid = user?.uid) }
        }
    }


}