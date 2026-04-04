package com.potatodevs.cropsamarica.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.datastore.FieldDataStore
import com.potatodevs.cropsamarica.repositories.auth.AuthRepository
import com.potatodevs.cropsamarica.repositories.ricefield.RiceFieldRepository
import com.potatodevs.cropsamarica.repositories.survey.SurveyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val riceFieldRepository: RiceFieldRepository,
    private  val riceFieldDataStore: FieldDataStore,
    private val surveyRepository: SurveyRepository

) : ViewModel() {
    private var _state = MutableStateFlow(
        MainState()
    )
    val state = _state.asStateFlow()
    init {


        observeUser()
    }
    private fun observeUser() {
        authRepository.listenToUser()
            .onEach { user ->
                _state.update { current ->
                    current.copy(user = user)
                }
            }
            .launchIn(viewModelScope)
    }
    fun events(e : MainEvents) {
        when(e) {
            is MainEvents.SelectRiceField -> {
                _state.value = _state.value.copy(selectedRiceField = e.riceField)
            }
            is MainEvents.GetRiceFields -> {
                getRiceFields(e.uid)
            }
        }
    }

    private fun getRiceFields(uid: String) {
        riceFieldRepository.getAllByUid(uid)
            .onStart {
                _state.value = _state.value.copy(isLoading = true)
            }
            .onEach { data ->
                val selected = data.getOrNull(0)
                selected?.riceField?.id?.let {
                    riceFieldDataStore.setSelectedField(it)
                }
                _state.update {
                    it.copy(
                        isLoading = false,
                        riceFields = data,
                        selectedRiceField = selected
                    )
                }
            }.launchIn(viewModelScope)
    }
}