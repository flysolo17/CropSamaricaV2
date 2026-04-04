package com.potatodevs.cropsamarica.ui.main.pest.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.datastore.LocaleManager
import com.potatodevs.cropsamarica.models.pests.PestAndDisease
import com.potatodevs.cropsamarica.repositories.pests.PestRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PestAndDiseaseDetailState(
    val isLoading: Boolean = false,
    val pestAndDisease: PestAndDisease? = null,
    val language: String = "en"
)
sealed interface PestAndDiseaseEvents {
    data class OnGetPestAndDisease(val id: String) : PestAndDiseaseEvents
}

@HiltViewModel
class PestAndDiseaseDetailViewModel @Inject constructor(
    private val pestRepository: PestRepository,
    private val localeManager: LocaleManager
) : ViewModel() {

    private var _state = MutableStateFlow(PestAndDiseaseDetailState())
    val state = _state.asStateFlow()
    init {
        localeManager.getSavedLanguageCode().onEach { code ->
            _state.update {
                it.copy(
                    language = code
                )
            }
        }.launchIn(viewModelScope)


    }

    fun events(e: PestAndDiseaseEvents) {
        when (e) {
            is PestAndDiseaseEvents.OnGetPestAndDisease -> getPestAndDisease(e.id)
        }
    }

    private fun getPestAndDisease(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = pestRepository.getById(id)

            result.fold(
                onSuccess = { pest ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            pestAndDisease = pest,

                        )
                    }
                },
                onFailure = { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
            )
        }
    }
}