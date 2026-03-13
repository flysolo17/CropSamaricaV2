package com.potatodevs.cropsamarica.ui.main.pest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.models.pests.PestAndDisease
import com.potatodevs.cropsamarica.repositories.pests.PestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class PestState(
    val isLoading : Boolean = false,
    val pests : List<PestAndDisease> = emptyList()
)


@HiltViewModel
class PestViewModel @Inject constructor(
    private val pestRepository: PestRepository
) : ViewModel() {
    private var _state = MutableStateFlow(PestState())
    val state = _state.asStateFlow()

    init {
        fetchPests()
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