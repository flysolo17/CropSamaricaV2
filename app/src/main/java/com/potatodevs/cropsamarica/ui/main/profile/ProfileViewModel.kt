package com.potatodevs.cropsamarica.ui.main.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.repositories.auth.AuthRepository
import com.potatodevs.cropsamarica.repositories.user.UserRepository
import com.potatodevs.cropsamarica.ui.config.AppRouter

import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents
import com.potatodevs.cropsamarica.ui.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private var _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()
    private var _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()
    init {
        viewModelScope.launch {
            val uid = authRepository.getCurrentUser()?.uid
            if ( uid != null) {
                userRepository.getUser(uid).onStart {
                    _state.value = _state.value.copy(isLoading = true)
                }.onEach { user ->
                    _state.update { it.copy(
                        isLoading = false,
                        user = user)
                    }
                }.launchIn(this)
            }
        }


    }

    fun events(e : ProfileEvents) {
        when(e) {
            ProfileEvents.OnLogout -> {
                viewModelScope.launch {
                    authRepository.logout().onSuccess {
                        _oneTimeEvents.trySend(OneTimeEvents.ShowToast("Logout Successful"))
                        _oneTimeEvents.trySend(OneTimeEvents.Navigate(AppRouter.Auth))

                    }
                }
            }

            is ProfileEvents.OnChangeName -> changeName(e.name,e.result)


            is ProfileEvents.OnChangeProfile -> changeProfile(e.uri)
        }
    }

    private fun changeProfile(uri: Uri) {
        viewModelScope.launch {
            userRepository.uploadProfile(uri).onSuccess {
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it))
            }.onFailure {
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message ?: "Unknown Error"))
            }
        }
    }



    private fun changeName(
        name: String,
        result: (UIState<String>) -> Unit
    ): Job {
        return viewModelScope.launch {
            userRepository.changeName(state.value.user?.id ?: "", name) {
                result.invoke(it)
            }
        }
    }
}