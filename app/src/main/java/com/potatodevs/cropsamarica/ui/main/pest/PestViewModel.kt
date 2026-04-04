package com.potatodevs.cropsamarica.ui.main.pest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.datastore.LocaleManager
import com.potatodevs.cropsamarica.models.pests.PestAndDisease
import com.potatodevs.cropsamarica.repositories.pests.PestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class PestState(
    val isLoading : Boolean = false,
    val pests : List<PestAndDisease> = emptyList(),
    val language : String = "en"
)


@HiltViewModel
class PestViewModel @Inject constructor(
    private val pestRepository: PestRepository,
    private val localeManager: LocaleManager
) : ViewModel() {
    private var _state = MutableStateFlow(PestState())
    val state = _state.asStateFlow()

    init {
        fetchPests()
        localeManager.getSavedLanguageCode().onEach { code ->
            _state.update {
                it.copy(
                    language = code
                )
            }
        }.launchIn(viewModelScope)
    }
    private fun fetchPests() {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true
            ) }
            pestRepository.getAllPests().onSuccess { data ->
                _state.update {
                    it.copy(
                        isLoading = false, pests = data
                    )
                }
            }
        }
    }
}