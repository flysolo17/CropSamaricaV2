package com.potatodevs.cropsamarica.ui.main.home.subscreens.survey

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.repositories.survey.SurveyRepository
import com.potatodevs.cropsamarica.repositories.tasks.TaskRepository
import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class SurveyViewModel
    @Inject constructor(
        private val surveyRepository: SurveyRepository,
        private val tasksRepository: TaskRepository
    ): ViewModel() {
        private var _state = MutableStateFlow(SurveyState())
        val state = _state.asStateFlow()
    private val _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()
    fun events(e : SurveyEvents) {
        when(e) {
            is SurveyEvents.OnGenerateSurvey -> {
                generateSurvey(e.id)
            }

            is SurveyEvents.OnChangeAnswer -> {
                answerChanged(e.index, e.answer)
            }
            is SurveyEvents.OnImageChange -> {
                val updatedList = _state.value.selectedImage.toMutableList().apply {
                    add(e.image)
                }
                _state.value = _state.value.copy(selectedImage = updatedList)
            }

            SurveyEvents.OnSubmit -> {
                submit()
            }

            is SurveyEvents.OnSaveTasks -> {
                saveTasks(e.tasks)
            }
        }
    }

    private fun saveTasks(tasks: List<Task>) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            tasksRepository.insertAll(tasks).onSuccess {
                _state.value = _state.value.copy(isLoading = false,recommendations = emptyList())
                _oneTimeEvents.send(OneTimeEvents.ShowToast("Successfully created tasks"))
                _oneTimeEvents.send(OneTimeEvents.NavigateBack)

            }.onFailure {
                _state.value = _state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message ?: "Something went wrong"))
                Log.d("CreateRiceFieldViewModel", "saveTasks: ${it.message}")
                _oneTimeEvents.send(OneTimeEvents.NavigateBack)

            }
        }
    }

    private fun answerChanged(index: Int, answer: String) {
        _state.value = _state.value.copy(
            questionsWithAnswers = _state.value.questionsWithAnswers.mapIndexed { i, questionWithAnswers ->
                if (i == index) {
                    questionWithAnswers.copy(answer = answer)
                } else {
                    questionWithAnswers
                }
            }
        )
    }

    private fun submit() {

        val survey = _state.value.questionsWithAnswers
        val crop = _state.value.survey?.survey?.crop
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            surveyRepository.generateRecommendationForNextStage(
                survey = survey,
                crop = crop ?: RiceFieldWithRiceType()
            ).onSuccess {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        recommendations = it
                    )
                }

                if (it.isEmpty()) {
                    _oneTimeEvents.send(OneTimeEvents.NavigateBack)
                    _oneTimeEvents.send(OneTimeEvents.ShowToast("No recommendations"))
                    return@onSuccess
                }
            }

        }
    }

    private fun generateSurvey(id: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    survey = currentState.survey?.copy(isLoading = true)
                        ?: QuestionState(isLoading = true)
                )
            }
            surveyRepository.generateSurvey(id)
                .onSuccess { generatedSurvey ->
                    _state.update { currentState ->
                        val questions = generatedSurvey.questions.map {
                            QuestionWithAnswers(it)
                        }


                        currentState.copy(
                            questionsWithAnswers = questions,
                            survey = QuestionState(
                                isLoading = false,
                                survey = generatedSurvey,
                                error = null
                            )
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update { currentState ->
                        currentState.copy(
                            survey = QuestionState(
                                isLoading = false,
                                error = exception.message ?: "Unknown Error"
                            )
                        )
                    }
                }
        }
    }

}